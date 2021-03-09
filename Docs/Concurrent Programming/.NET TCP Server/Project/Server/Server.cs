using System.Collections.Generic;
using System.Net.Sockets;
using System.Net;
using System.Threading;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System;
using System.IO;
using System.Threading.Tasks;

namespace Server
{
    public class Server
    {
        private readonly TcpListener server;
        private readonly int maxReqs;
        private readonly CancellationTokenSource cts;
        private readonly Dictionary<string, Func<ITransferQueue<JObject>, Request, CancellationToken, Task<Response>>> methods;
        private readonly Dictionary<string, ITransferQueue<JObject>> queues;
        private readonly HashSet<Task> startedTasks;
        private static readonly JsonSerializer serializer = new JsonSerializer();
        private int counter;
        private readonly Logger _logger;

        public Server(int port = 1234, int maxReqs = 10)
        {
            this.queues = new Dictionary<string, ITransferQueue<JObject>>();
            this.cts = new CancellationTokenSource();
            this.maxReqs = maxReqs;
            this._logger = new Logger(cts.Token);
            this.startedTasks = new HashSet<Task>();
            this.methods = new Dictionary<string, Func<ITransferQueue<JObject>, Request, CancellationToken, Task<Response>>>();
            methods.Add("PUT", (selectedQ, request, cToken) => Task.FromResult(Put(selectedQ, request, cToken)));
            methods.Add("CREATE", (_, req, __) => Task.FromResult<Response>(Create(req)));
            methods.Add("TRANSFER", TransferAsync);
            methods.Add("TAKE", TakeAsync);
            methods.Add("SHUTDOWN", (_, req, ___) => ShutdownAsync(req));
            server = new TcpListener(IPAddress.Loopback, port);
            server.Start();
        }

        private async Task<Response> TransferAsync(ITransferQueue<JObject> selectedQ, Request request, CancellationToken cToken)
        {
            if (selectedQ == null) return FormatResponse(request.Payload, status: (int)HttpStatusCode.BadRequest);

            bool success = await selectedQ.TransferAsync(request.Payload, timeout: Convert.ToInt32(request.Headers["timeout"]), cToken: cToken);
            return FormatResponse(request.Payload, success ? (int)HttpStatusCode.OK : (int)HttpStatusCode.RequestTimeout);
        }

        public async void Start()
        {
            CancellationToken ct = cts.Token;

            do
            {
                try
                {
                    TcpClient client = await server.AcceptTcpClientAsync();
                    int id = Interlocked.Increment(ref counter);
                    startedTasks.Add(ProcessConnectionAsync(id, client, ct));
                    if (startedTasks.Count >= maxReqs)
                    {
                        if (startedTasks.RemoveWhere(task => task.IsCompleted) == 0)
                        {
                            startedTasks.Remove(await Task.WhenAny(startedTasks));
                        }
                    }
                }
                catch (ObjectDisposedException)
                {
                    //All good don't panic, it means the server as stopped accepting requests.
                } 
                catch(Exception e)
                {
                    Log(e.Message);
                }
            } while (!cts.IsCancellationRequested);
            if (startedTasks.Count > 0)
                await Task.WhenAll(startedTasks);
        }

        private async Task ProcessConnectionAsync(int id, TcpClient client, CancellationToken ct)
        {
            try
            {
                using (client)
                {
                    var stream = client.GetStream();
                    var reader = new JsonTextReader(new StreamReader(stream))
                    {
                        // To support reading multiple top-level objects
                        SupportMultipleContent = true
                    };
                    var writer = new JsonTextWriter(new StreamWriter(stream));
                    while (true)
                    {
                        try
                        {
                            // to consume any bytes until start of object ('{')
                            do
                            {
                                await reader.ReadAsync(ct);
                                Log($"advanced to {reader.TokenType}");
                            } while (reader.TokenType != JsonToken.StartObject
                                     && reader.TokenType != JsonToken.None);

                            if (reader.TokenType == JsonToken.None)
                            {
                                Log($"[{id}] reached end of input stream, ending.");
                                return;
                            }

                            Log("Reading object");
                            var json = await JObject.LoadAsync(reader, ct);
                            Log($"Object read, canceled? {ct.IsCancellationRequested}");
                            var request = json.ToObject<Request>();
                            Log(request.ToString());
                            Response response = null;
                            
                            Func<ITransferQueue<JObject>, Request, CancellationToken, Task<Response>> targetMethod;
                            if (methods.TryGetValue(request.Method, out targetMethod))
                            {
                                queues.TryGetValue(request.Path, out ITransferQueue<JObject> selectedQ);
                                Log($"selectedQ exists? = {selectedQ == null}");
                                response = await targetMethod.Invoke(selectedQ, request, ct);
                            }
                            else response = new Response()
                            {
                                Status = (int)HttpStatusCode.MethodNotAllowed,
                                Payload = request.Payload
                            };
                            serializer.Serialize(writer, response);
                            await writer.FlushAsync(ct);
                        }
                        catch (JsonReaderException e)
                        {
                            Log($"[{id}] Error reading JSON: {e.Message}, ending");
                            var response = new Response
                            {
                                Status = (int) HttpStatusCode.OK,
                            };
                            serializer.Serialize(writer, response);
                            await writer.FlushAsync(ct);
                            // close the connection because an error may not be recoverable by the reader
                            return;
                        }
                        catch (Exception e)
                        {
                            Log($"[{id}] Exception: {e.Message}, ending");
                            return;
                        }
                    }
                }
            }
            finally
            {
                Interlocked.Decrement(ref counter);
                Log($"Ended connection {id}");
            }
        }

        private async Task<Response> TakeAsync(ITransferQueue<JObject> selectedQ, Request request, CancellationToken ct = default)
        {
            if (selectedQ == null) return FormatResponse(request.Payload, status: (int)HttpStatusCode.BadRequest);

            JObject payload = await selectedQ.TakeAsync(timeout: Convert.ToInt32(request.Headers["timeout"]),cToken: ct);
            bool failure = payload == null;
            if (failure) payload = request.Payload;
            return FormatResponse(payload, failure ? (int)HttpStatusCode.RequestTimeout : (int)HttpStatusCode.OK);
        }

        private Response Put(ITransferQueue<JObject> selectedQ, Request req, CancellationToken ct = default)
        {
            if (selectedQ == null) return FormatResponse(req.Payload, status: (int)HttpStatusCode.BadRequest);

            bool success = selectedQ.Put(req.Payload, cToken: ct);
            return FormatResponse(req.Payload, status: success ? (int)HttpStatusCode.OK : (int)HttpStatusCode.InternalServerError);
        }

        private Response FormatResponse(JObject payload = default, int status = (int) HttpStatusCode.OK)
        {
            Response response = new Response()
            {
                Status = status,
                Payload = payload
            };
            if (payload != null)
            {
                response.Headers.Add("content-type", "application/json");
            }
            return response;
        }

        public Response Create(Request request)
        {
            queues[request.Path] = new TransferQueueV2<JObject>();
            return FormatResponse(request.Payload, (int) HttpStatusCode.Created);
        }

        public async Task<Response> ShutdownAsync(Request req)
        {
            server.Stop();
            cts.Cancel();
            int timeout = Convert.ToInt32(req.Headers["timeout"]);
            Task waitAll = Task.WhenAll(startedTasks);
            int status;
            if (await Task.WhenAny(waitAll, Task.Delay(timeout)) == waitAll)
            {
                status = (int) HttpStatusCode.OK;
            }
            else //timeout
            {
                status = (int)HttpStatusCode.RequestTimeout;
            }
            return FormatResponse(req.Payload, status: status);
        }

        private void Log(string report)
        {
            _logger.Log(report);
        }
    }
}
