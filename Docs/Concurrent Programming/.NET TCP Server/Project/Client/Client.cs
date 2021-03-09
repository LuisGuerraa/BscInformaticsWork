using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using Server;

namespace Client
{
	/**
 * Represents the payload of the request message
 */
	public class RequestPayload
	{

		public int Number { get; set; }
		public String Text { get; set; }
		public string Method { get; internal set; }

		public override String ToString()
		{
			return $"[ Number: {Number}, Text: {Text}, Method requested: {Method} ]";
		}
	}
    class Client
    {
		private const int SERVER_PORT = 1234;
		private static int requestCount = 0;

		private static JsonSerializer serializer = new JsonSerializer();

		/**
		 * Send a server request and display the response.
		 */
		static async Task SendRequestAndReceiveResponseAsync(string server, RequestPayload payload)
		{
			/**
			 * Create a TcpClient socket in order to connectto the echo server.
			 */
			using (TcpClient connection = new TcpClient())
			{
				try
				{
					// Start a stop watch timer
					Stopwatch sw = Stopwatch.StartNew();

					// connect socket to the echo server.

					await connection.ConnectAsync(server, SERVER_PORT);

					// Create and fill the Request with "payload" as Payload
					Request request = new Request
					{
						Method = payload.Method,
						Path = payload.Text,
						Headers = new Dictionary<String, String>(),
						Payload = (JObject)JToken.FromObject(payload),
					};

					// Add some headers for test purposes 
					request.Headers.Add("agent", "json-client");
					request.Headers.Add("timeout", "10000");

					/**
					 * Translate the message to JSON and send it to the echo server.
					 */

					JsonTextWriter writer = new JsonTextWriter(new StreamWriter(connection.GetStream()));
					serializer.Serialize(writer, request);
					Console.WriteLine($"-->{payload.ToString()}");
					await writer.FlushAsync();

					/**
					 * Receive the server's response and display it.
					 */
					JsonTextReader reader = new JsonTextReader(new StreamReader(connection.GetStream()))
					{
						// To support reading multiple top-level objects
						SupportMultipleContent = true
					};
					try
					{
						// to consume any bytes until start of object ('{')
						do
						{
							await reader.ReadAsync();
						} while (reader.TokenType != JsonToken.StartObject &&
								 reader.TokenType != JsonToken.None);
						if (reader.TokenType == JsonToken.None)
						{
							Console.WriteLine("***error: reached end of input stream, ending.");
							return;
						}
						/**
						 * Read response JSON object
						 */
						JObject jresponse = await JObject.LoadAsync(reader);
						sw.Stop();

						/**
						 * Back to the .NET world
						 */
						Response response = jresponse.ToObject<Response>();
						
						RequestPayload recoveredPayload = response.Payload.ToObject<RequestPayload>();

						Console.WriteLine($"<--{response.ToString()}, elapsed: {sw.ElapsedMilliseconds} ms");

					}
					catch (JsonReaderException jre)
					{
						Console.WriteLine($"***error: error reading JSON: {jre.Message}");
					}
					catch (Exception e)
					{
						Console.WriteLine($"-***error: exception: {e}");
					}
					sw.Stop();
					Interlocked.Increment(ref requestCount);
				}
				catch (Exception ex)
				{
					Console.WriteLine($"--***error:[{payload}] {ex.Message}");
				}
			}
		}

		/**
		 * Send continuously batches of requests until a key is pressed.
		 */
		private const int MAX_DEGREE_OF_PARALLELISM = 1;
		private const int REQ_BATCH_COUNT = 12;

		// use explicitly created tasks
		public static void Main(string[] args)
		{
			bool executeOnce = false;
			const int partial = REQ_BATCH_COUNT / 4;

			// set the minimum thread pool's worker threads to theh MAX_DEGREE_OF_PARALLELISM
			int worker, iocp;
			ThreadPool.GetMinThreads(out worker, out iocp);
			ThreadPool.SetMinThreads(worker < MAX_DEGREE_OF_PARALLELISM ? MAX_DEGREE_OF_PARALLELISM : worker, iocp);

			string text = (args.Length > 0) ? args[0] : "original paths are hard ok";
			
			Task[] tasksCreate = new Task[partial];
			Task[] tasksPut = new Task[partial];
			Task[] tasksTake = new Task[partial];
			Task[] tasksTransfer = new Task[partial];
			Stopwatch sw = Stopwatch.StartNew();
			//do
			//{
			for (int i = 0; i < partial; i++)
			{
				tasksCreate[i] = SendRequestAndReceiveResponseAsync("localhost",
					new RequestPayload { Number = i, Text = text + i, Method = "CREATE" });
			}
			Task.WaitAll(tasksCreate);
			for (int i = 0; i < partial; i++)
			{
				int id = i + partial;
				tasksPut[i] = SendRequestAndReceiveResponseAsync("localhost",
					new RequestPayload { Number = id, Text = text + i, Method = "PUT"});
			}
			for (int i = 0; i < partial; i++)
			{
				int id = i + partial * 2;
				tasksTransfer[i] = SendRequestAndReceiveResponseAsync("localhost",
					new RequestPayload { Number = id, Text = text + i, Method = "TRANSFER" });
			}
			for (int i = 0; i < partial; i++)
			{
				int id = i + partial * 3;
				tasksTake[i] = SendRequestAndReceiveResponseAsync("localhost",
					new RequestPayload { Number = id, Text = text + i, Method = "TAKE" });
			}
			List<Task> tasks = new List<Task>();
			tasks.AddRange(tasksPut);
			tasks.AddRange(tasksTransfer);
			tasks.AddRange(tasksTake);
			//there will be races between put and transfer so timeouts are expected
			Task.WaitAll(tasks.ToArray());
			SendRequestAndReceiveResponseAsync("localhost", new RequestPayload { Number = -3, Text = "should have status 405 operation non existent", Method = "IDKSTFU" }).Wait();
			SendRequestAndReceiveResponseAsync("localhost", new RequestPayload { Number = -1, Text = "shutdown msg", Method = "SHUTDOWN" }).Wait();
			//} while (!(executeOnce || Console.KeyAvailable));
			Console.WriteLine("--completed requests: {0} / {1} ms", Volatile.Read(ref requestCount), sw.ElapsedMilliseconds);
			Console.ReadKey();
		}
	}
}
