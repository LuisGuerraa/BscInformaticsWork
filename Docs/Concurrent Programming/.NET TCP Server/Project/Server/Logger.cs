using NLog;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Server
{
    internal class Logger
    {
        private const int MAX_WAIT_TIME = 50;
        private readonly ITransferQueue<string> queue;
        private readonly Thread thread;
        private readonly static string dstFileName = $"{Environment.CurrentDirectory}\\logs.txt";
        private readonly List<Task> writes;
        private readonly CancellationToken cToken;
        const int BUFFER_SIZE = 4 * 1024;

        public Logger(CancellationToken cToken = default)
        {
            this.cToken = cToken;
            writes = new List<Task>();
            queue = new TransferQueueV2<string>();
            thread = new Thread(CheckLogs)
            {
                Priority = ThreadPriority.Lowest,
                IsBackground = true
            };
            thread.Start();
        }

        public void Log(string report)
        {
            queue.TransferAsync(report, MAX_WAIT_TIME, cToken);
        }

        private async void CheckLogs()
        {
            try
            {
                do
                {
                    string report = await queue.TakeAsync(MAX_WAIT_TIME, cToken);
                    if (report == null) continue;
                    writes.Add(SequentialCopyAsync(report));
                } while (!cToken.IsCancellationRequested);
            } catch (OperationCanceledException)
            {
                ////server shutting down
            }
            Task.WaitAll(writes.ToArray());
        }

        static Task SequentialCopyAsync(string report)
        {
            const bool AsyncMode = true;

            UnicodeEncoding uniencoding = new UnicodeEncoding();

            byte[] toReport = uniencoding.GetBytes(report);

            using (FileStream dst = new FileStream(dstFileName,
                                                        FileMode.Append, FileAccess.Write,
                                                        FileShare.None, BUFFER_SIZE, AsyncMode))
            {

                return dst.WriteAsync(toReport, 0, toReport.Length);
            }
        }
    }
}