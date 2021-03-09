using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace Server
{
    internal enum MessageType
    {
        PUT, TRANSFER
    }
    internal class TransferQueue<E> : ITransferQueue<E>
    {
        private class Message 
        {
            internal MessageType type;
            internal E data;
            internal SemaphoreAsync semaphore = new SemaphoreAsync(1, 1);
            
            public Message(E data = default, MessageType type = MessageType.PUT)
            {
                this.data = data;
                this.type = type;
            }
        }
        private readonly LinkedList<Message> queue;
        private readonly object _lock = new object();
        private readonly SemaphoreAsync filledSlots;

        public TransferQueue(int capacity = int.MaxValue)
        {
            queue = new LinkedList<Message>();
            filledSlots = new SemaphoreAsync(0, capacity);
        }

        public bool Put(E item, CancellationToken cToken = default)
        {
            if(cToken.IsCancellationRequested)
            {
                return false;
            }
            lock (_lock)
            {
                queue.AddFirst(new Message(item));
                filledSlots.Release();
                return true;
            }
        }

        public async Task<E> TakeAsync(int timeout = Timeout.Infinite,
                                    CancellationToken cToken = default)
        {
            if(!await filledSlots.WaitAsync(timeout:timeout, cToken: cToken))
            {
                return default;
            }
            lock(_lock)
            {
                LinkedListNode<Message> node = queue.First;
                queue.RemoveFirst();
                if(node.Value.type == MessageType.TRANSFER)
                {
                    node.Value.semaphore.Release();
                }
                return node.Value.data;
            }
        }

        public async Task<bool> TransferAsync(E item, int timeout = Timeout.Infinite, CancellationToken cToken = default)
        {
            Message message;
            lock (_lock)
            {
                message = new Message(item, MessageType.TRANSFER);
                //acquire semaphore of message
                message.semaphore.Acquire();
                queue.AddFirst(message);
                filledSlots.Release();
            }
            //wait until a Take() releases the semaphore
            if(!await message.semaphore.WaitAsync(timeout: timeout, cToken: cToken))
            {
                //timeout
                lock(_lock)
                {
                    queue.Remove(message);
                }
                return false;
            }
            //transfer completed
            return true;
        }
    }
}