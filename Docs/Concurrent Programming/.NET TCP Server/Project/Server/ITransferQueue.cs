using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Server
{
    interface ITransferQueue<E>
    {
        public bool Put(E item, CancellationToken cToken = default);
        public Task<E> TakeAsync(int timeout = Timeout.Infinite, CancellationToken cToken = default);
        public Task<bool> TransferAsync(E item, int timeout = Timeout.Infinite, CancellationToken cToken = default);
    }
}
