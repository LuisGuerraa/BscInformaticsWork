using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Server
{
    public class TransferQueueV2<T> : ITransferQueue<T>
    {
        /**
        * The base type used with the types that represent the
        * async take and transfer requests.
        */
        internal class AsyncRequest<V> : TaskCompletionSource<V>
        {

            internal readonly CancellationToken cToken;
            internal CancellationTokenRegistration cTokenRegistration;
            internal Timer timer;
            const int PENDING = 0, LOCKED = 1;
            private volatile int _lock; // the lock state


            // Initialize a async waiter
            internal AsyncRequest(CancellationToken cToken)
            {
                this.cToken = cToken;
                this._lock = PENDING;
            }

            internal bool TryLock()
            {
                return _lock == PENDING &&
                Interlocked.CompareExchange(ref _lock, LOCKED, PENDING)
                == PENDING;
            }

            /**
            * Disposes resources associated with this async acquire.
            *
            * Note: when this method is called we are sure that the fields "timer"
            * and "cTokenRegistration" are correctly affected
            */
            internal void Dispose(bool canceling = false)
            {
                // The CancellationTokenRegistration is disposed off after the cancellation
                // handler returns.
                if (!canceling && cToken.CanBeCanceled)
                    cTokenRegistration.Dispose();
                timer?.Dispose();
            }
        }

        /**
        * Types used to represent with async take and async transfer requests.
        *
        * Note: This types are used to fix the types parameter of the AsyncRequest<V>.
        */
        private class AsyncTake : AsyncRequest<T>
        {
            internal AsyncTake(CancellationToken cToken) : base(cToken)
            { }

        }

        private class AsyncTransfer : AsyncRequest<bool>
        {
            internal AsyncTransfer(CancellationToken cToken) : base(cToken)
            { }
        }

        /**
        * The type used to hold a message and the underlying transferer, if one exists.
        */

        private class Message
        {
            internal readonly T message;
            internal readonly AsyncTransfer transfer;
            internal readonly bool isTransfer;

            internal Message(T message, AsyncTransfer transfer = null)
            {
                this.message = message;
                this.transfer = transfer;
                isTransfer = transfer != null;
            }
        }
        
        /**
		 *  Completed tasks use to return true and false results
		 */
        private static readonly Task<bool> trueTask = Task.FromResult<bool>(true);
        private static readonly Task<bool> falseTask = Task.FromResult<bool>(false);

        /**
		 * Delegates used as cancellation handlers for asynchrounous requests 
		 */
        private readonly Action<object> cancellationTakeHandler;
        private readonly TimerCallback timeoutTakeHandler;
        private readonly Action<object> cancellationTransferHandler;
        private readonly TimerCallback timeoutTransferHandler;

        // The global lock
        private readonly object theLock;

        // Available messages with or without associated async transfer requests.
        private readonly LinkedList<Message> pendingMessages;

        // The queue of pending async take requests.
        private readonly LinkedList<AsyncTake> asyncTakes;

        public TransferQueueV2()
        {
            theLock = new object();
            pendingMessages = new LinkedList<Message>();
            asyncTakes = new LinkedList<AsyncTake>();
            cancellationTransferHandler = new Action<object>((acquireNode) => AcquireTransferCancellationHandler(acquireNode, true));
            timeoutTransferHandler = new TimerCallback((acquireNode) => AcquireTransferCancellationHandler(acquireNode, false));
            cancellationTakeHandler = new Action<object>((acquireNode) => AcquireTakeCancellationHandler(acquireNode, true));
            timeoutTakeHandler = new TimerCallback((acquireNode) => AcquireTakeCancellationHandler(acquireNode, false));
        }

        private void AcquireTransferCancellationHandler(object _acquireNode, bool canceling)
        {
            LinkedListNode<Message> acquireNode = (LinkedListNode<Message>)_acquireNode;
            AsyncTransfer acquire = acquireNode.Value.transfer;
            if (acquire.TryLock())
            {
                // To access shared mutable state we must acquire the lock
                lock (theLock)
                {
                    pendingMessages.Remove(acquireNode);
                }

                // Release the resources associated with the async acquire.
                acquire.Dispose(canceling);

                // Complete the TaskCompletionSource to RanToCompletion (timeout)
                // or Canceled final state.
                if (canceling)
                    acquire.SetCanceled();
                else
                    acquire.SetResult(false);
            }
        }

        private void AcquireTakeCancellationHandler(object _acquireNode, bool canceling)
        {
            LinkedListNode<AsyncTake> acquireNode = (LinkedListNode<AsyncTake>)_acquireNode;
            AsyncTake acquire = acquireNode.Value;
            if (acquire.TryLock())
            {
                // To access shared mutable state we must acquire the lock
                lock (theLock)
                {
                    asyncTakes.Remove(acquireNode);
                }

                // Release the resources associated with the async acquire.
                acquire.Dispose(canceling);

                // Complete the TaskCompletionSource to RanToCompletion (timeout)
                // or Canceled final state.
                if (canceling)
                    acquire.SetCanceled();
                else
                    acquire.SetResult(default);
            }
        }

        public bool Put(T item, CancellationToken cToken = default)
        {
            if (cToken.IsCancellationRequested) {
                return false;
            }
            lock(theLock)
            {
                if(asyncTakes.Count > 0)
                {
                    LinkedListNode<AsyncTake> requestNode = asyncTakes.First;
                    AsyncTake request = requestNode.Value;
                    if (request.TryLock())
                    {
                        asyncTakes.RemoveFirst();
                        request.TrySetResult(item);
                    }
                    return true;
                } 
                else
                {
                    pendingMessages.AddLast(new Message(item));
                    return true;
                }
            }
        }

        public Task<T> TakeAsync(int timeout = Timeout.Infinite, CancellationToken cToken = default)
        {
            lock (theLock)
            {
                //check current content of messages
                if(pendingMessages.Count > 0)
                {
                    LinkedListNode<Message> node = pendingMessages.First;
                    Message message = node.Value;
                    if (message.isTransfer)
                    {
                        if (message.transfer.TryLock())
                        {
                            message.transfer.TrySetResult(true);
                        }
                    }
                    pendingMessages.RemoveFirst();
                    return Task.FromResult<T>(node.Value.message);
                }

                //if timeout is immediate return failure
                if (timeout == 0)
                {
                    return Task.FromResult<T>(default);
                }

                //create the request and add it to the list
                AsyncTake request = new AsyncTake(cToken);
                LinkedListNode<AsyncTake> requestNode = asyncTakes.AddLast(request);

                if(timeout != Timeout.Infinite)
                {
                    request.timer = new Timer(timeoutTakeHandler, requestNode, timeout, Timeout.Infinite);
                }
                /**
				 * If the cancellation token is already in the canceled state, the cancellation
				 * handler will run immediately and synchronously, which *causes no damage* because
				 * this processing is terminal and the implicit locks can be acquired recursively.
				 */
                if (cToken.CanBeCanceled)
                    request.cTokenRegistration = cToken.Register(cancellationTakeHandler, requestNode);

                // Return the Task<bool> that represents the async acquire
                return request.Task;
            }
        }

        public Task<bool> TransferAsync(T item, int timeout = Timeout.Infinite, CancellationToken cToken = default)
        {
            lock (theLock)
            {
                //check current content of requests
                if (asyncTakes.Count > 0)
                {
                    LinkedListNode<AsyncTake> node = asyncTakes.First;
                    AsyncTake request = node.Value;
                    if (request.TryLock())
                    {
                        asyncTakes.RemoveFirst();
                        request.TrySetResult(item);
                    }
                    return trueTask;
                }

                //if timeout is immediate return failure
                if (timeout == 0)
                {
                    return falseTask;
                }

                //create the request and add it to the list
                AsyncTransfer acquire = new AsyncTransfer(cToken);
                Message transfer = new Message(item, acquire);
                LinkedListNode<Message> transferNode = pendingMessages.AddLast(transfer);

                if (timeout != Timeout.Infinite)
                {
                    acquire.timer = new Timer(timeoutTransferHandler, transferNode, timeout, Timeout.Infinite);
                }
                /**
				 * If the cancellation token is already in the canceled state, the cancellation
				 * handler will run immediately and synchronously, which *causes no damage* because
				 * this processing is terminal and the implicit locks can be acquired recursively.
				 */
                if (cToken.CanBeCanceled)
                    acquire.cTokenRegistration = cToken.Register(cancellationTransferHandler, transferNode);

                // Return the Task<bool> that represents the async acquire
                return acquire.Task;
            }
        }
    }
}
