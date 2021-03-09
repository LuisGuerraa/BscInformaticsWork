/**
 *
 *  ISEL, LEIC, Concurrent Programming
 *
 *  Semaphore with asynchronous and synchronous interface
 *
 *  Carlos Martins, December 2019
 *
 **/

using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
namespace Server
{
	public class SemaphoreAsync
	{

		// The type used to represent each asynchronous acquire
		private class AsyncAcquire : TaskCompletionSource<bool>
		{
			internal readonly int acquires;
			internal readonly CancellationToken cToken;
			internal CancellationTokenRegistration cTokenRegistration;
			internal Timer timer;
			const int PENDING = 0, LOCKED = 1;
			private volatile int _lock;     // the request lock

			internal AsyncAcquire(int acquires, CancellationToken cToken) : base()
			{
				this.acquires = acquires;
				this.cToken = cToken;
				this._lock = PENDING;
			}

			/**
			 * Tries to lock the request in order to satisfy it.
			 */
			internal bool TryLock()
			{
				return _lock == PENDING &&
					   Interlocked.CompareExchange(ref _lock, LOCKED, PENDING) == PENDING;
			}

			/**
			 * Disposes resources associated with this async acquire.
			 *
			 * Note: when this method is called we are sure that the fields "timer"
			 *       and "cTokenRegistration" are correctly affected
			 */
			internal void Dispose(bool canceling = false)
			{
				// The CancellationTokenRegistration is disposed off after the
				// cancellation handler is called.
				if (!canceling && cToken.CanBeCanceled)
					cTokenRegistration.Dispose();
				timer?.Dispose();
			}
		}

		// The lock - we do not use the monitor functionality
		private readonly object theLock = new object();

		// available and maximum number of permits	
		private int permits;
		private readonly int maxPermits;

		// the request queue
		private readonly LinkedList<AsyncAcquire> asyncAcquires;

		/**
		 * Delegates used as cancellation handlers for asynchrounous requests 
		 */
		private readonly Action<object> cancellationHandler;
		private readonly TimerCallback timeoutHandler;

		/**
		 *  Completed tasks use to return true and false results
		 */
		private static readonly Task<bool> trueTask = Task.FromResult<bool>(true);
		private static readonly Task<bool> falseTask = Task.FromResult<bool>(false);

		/**
		 * Constructor
		 */
		public SemaphoreAsync(int initial = 0, int maximum = Int32.MaxValue)
		{
			// Validate arguments
			if (initial < 0 || initial > maximum)
				throw new ArgumentOutOfRangeException("initial");
			if (maximum <= 0)
				throw new ArgumentOutOfRangeException("maximum");
			// Construct delegates used to describe the two cancellation handlers.
			cancellationHandler = new Action<object>((acquireNode) => AcquireCancellationHandler(acquireNode, true));
			timeoutHandler = new TimerCallback((acquireNode) => AcquireCancellationHandler(acquireNode, false));
			// Initialize the semaphore
			permits = initial;
			maxPermits = maximum;
			asyncAcquires = new LinkedList<AsyncAcquire>();
		}

		/**
		 * Auxiliary methods
		 */

		/**
		 * Returns the list of all pending async acquires that can be satisfied with
		 * the current number of permits owned by the semaphore.
		 *
		 * Note: Tis method is called when the current thread owns the lock.
		 */
		private List<AsyncAcquire> SatisfyPendingAsyncAcquires()
		{
			List<AsyncAcquire> satisfied = null;
			while (asyncAcquires.Count > 0)
			{
				AsyncAcquire acquire = asyncAcquires.First.Value;
				// Check if available permits allow satisfy this request
				if (acquire.acquires > permits)
					break;
				// Remove the request from the queue
				asyncAcquires.RemoveFirst();
				// Try lock the request and complete it if succeeded
				if (acquire.TryLock())
				{
					permits -= acquire.acquires;
					if (satisfied == null)
						satisfied = new List<AsyncAcquire>(1);
					satisfied.Add(acquire);
				}
			}
			return satisfied;
		}

		/**
		 * Complete the tasks associated to the satisfied requests.
		 *
		 *  Note: This method is called when calling thread does not own the lock.
		 */
		private void CompleteSatisfiedAsyncAcquires(List<AsyncAcquire> toComplete)
		{
			if (toComplete != null)
			{
				foreach (AsyncAcquire acquire in toComplete)
				{
					// Dispose the resources associated with the async acquirer and
					// complete its task with success.
					acquire.Dispose();
					acquire.SetResult(true);    // complete the associated request's task
				}
			}
		}

		/**
		 * Try to cancel an async acquire request
		 */
		private void AcquireCancellationHandler(object _acquireNode, bool canceling)
		{
			LinkedListNode<AsyncAcquire> acquireNode = (LinkedListNode<AsyncAcquire>)_acquireNode;
			AsyncAcquire acquire = acquireNode.Value;
			if (acquire.TryLock())
			{
				List<AsyncAcquire> satisfied = null;
				// To access shared mutable state we must acquire the lock
				lock (theLock)
				{
					if (acquireNode.List != null)
						asyncAcquires.Remove(acquireNode);
					if (asyncAcquires.Count > 0 && permits >= asyncAcquires.First.Value.acquires)
						satisfied = SatisfyPendingAsyncAcquires();
				}
				// Complete the satisfied async acquires
				CompleteSatisfiedAsyncAcquires(satisfied);

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

		/**
		 * Asynchronous Task-based Asynchronous Pattern (TAP) interface.
		 */

		/**
		 * Acquires one or more permits asynchronously enabling, optionally,
		 * a timeout and/or cancellation.
		 */
		public Task<bool> AcquireAsync(int acquires = 1, int timeout = Timeout.Infinite,
									   CancellationToken cToken = default(CancellationToken))
		{
			lock (theLock)
			{
				if (asyncAcquires.Count == 0 && permits >= acquires)
				{
					permits -= acquires;
					return trueTask;
				}
				// if the acquire was specified as immediate, return failure
				if (timeout == 0)
					return falseTask;

				// If a cancellation was already requested return a task in the Canceled state
				if (cToken.IsCancellationRequested)
					return Task.FromCanceled<bool>(cToken);

				// Create a request node and insert it in requests queue
				AsyncAcquire acquire = new AsyncAcquire(acquires, cToken);
				LinkedListNode<AsyncAcquire> acquireNode = asyncAcquires.AddLast(acquire);

				/**
				 * Activate the specified cancelers owning the lock.
				 */

				/**
				 * Since the timeout handler, that runs on a thread pool's worker thread,
				 * acquires the lock before access the "acquirer.timer" and "acquirer.cTokenRegistration"
				 * these assignements will be visible to timer handler.
				 */
				if (timeout != Timeout.Infinite)
					acquire.timer = new Timer(timeoutHandler, acquireNode, timeout, Timeout.Infinite);

				/**
				 * If the cancellation token is already in the canceled state, the cancellation
				 * handler will run immediately and synchronously, which *causes no damage* because
				 * this processing is terminal and the implicit locks can be acquired recursively.
				 */
				if (cToken.CanBeCanceled)
					acquire.cTokenRegistration = cToken.Register(cancellationHandler, acquireNode);

				// Return the Task<bool> that represents the async acquire
				return acquire.Task;
			}
		}

		/**
		 * Wait until acquire multiple permits asynchronously enabling, optionally,
		 * a timeout and/or cancellation.
		 */
		public Task<bool> WaitAsync(int acquires = 1, int timeout = Timeout.Infinite,
									CancellationToken cToken = default(CancellationToken))
		{
			return AcquireAsync(acquires, timeout, cToken);
		}

		/**
		 * Releases the specified number of permits
		 */
		public void Release(int releases = 1)
		{
			// A list to hold temporarily satisfied asynchronous operations 
			List<AsyncAcquire> satisfied = null;
			lock (theLock)
			{
				if (permits + releases > maxPermits)
					throw new InvalidOperationException("Exceeded the maximum number of permits");
				permits += releases;
				satisfied = SatisfyPendingAsyncAcquires();
			}
			// Complete satisfied requests without owning the lock
			CompleteSatisfiedAsyncAcquires(satisfied);
		}

		/**
		 *	Synchronous interface implemented using the asynchronous TAP interface.
		 */

		/**
		 * Try to cancel an asynchronous request identified by its task.
		 *
		 * Note: This is used to implement the synchronous interface.
		 */
		private bool CancelAcquireByTask(Task<bool> acquireTask)
		{
			AsyncAcquire acquire = null;
			List<AsyncAcquire> satisfied = null;
			// To access the shared mutable state we must acquire the lock
			lock (theLock)
			{
				foreach (AsyncAcquire _acquire in asyncAcquires)
				{
					if (_acquire.Task == acquireTask)
					{
						if (_acquire.TryLock())
						{
							acquire = _acquire;
							asyncAcquires.Remove(_acquire);
						}
						break;
					}
				}
				// If the new state od semaphore allows waiting acquires, satisfy them
				if (asyncAcquires.Count > 0 && permits >= asyncAcquires.First.Value.acquires)
					satisfied = SatisfyPendingAsyncAcquires();
			}
			CompleteSatisfiedAsyncAcquires(satisfied);

			if (acquire != null)
			{
				// Dispose the resources associated with this async acquire and complete
				// its task to the Canceled state.
				acquire.Dispose();
				acquire.SetCanceled();
			}
			return acquire != null;
		}


		/**
		 * Acquire multiple permits synchronously, enabling, optionally, a timeout
		 * and/or cancellation.
		 */
		public bool Acquire(int acquires = 1, int timeout = Timeout.Infinite,
							CancellationToken cToken = default(CancellationToken))
		{
			Task<bool> acquireTask = AcquireAsync(acquires, timeout, cToken);
			try
			{
				return acquireTask.Result;
			}
			catch (ThreadInterruptedException)
			{
				// The acquirer thread was interrupted!
				//  Try to cancel the async acquire operation
				if (CancelAcquireByTask(acquireTask))
					throw;

				// We known that the async acquire was already completed or cancelled,
				// so return the underlying result, ignoring possible interrupts.
				try
				{
					do
					{
						try
						{
							return acquireTask.Result;
						}
						catch (ThreadInterruptedException)
						{
							// ignore interrupts while waiting fro task's result
						}
						catch (AggregateException ae)
						{
							throw ae.InnerException;
						}
					} while (true);
				}
				finally
				{
					// Anyway re-assert the interrupt on the current thead.
					Thread.CurrentThread.Interrupt();
				}
			}
			catch (AggregateException ae)
			{
				// The acquire thrown an exception, propagate it synchronously
				throw ae.InnerException;
			}
		}

		/**
		  * Wait until acquire multiple permits synchronously, enabling, optionally,
		  * a timeout and/or cancellation.
		  */
		public bool Wait(int acquires = 1, int timeout = Timeout.Infinite,
						 CancellationToken cToken = default(CancellationToken))
		{
			return Acquire(acquires, timeout, cToken);
		}

		/**
		 * Return the current number of available permits
		 */
		public int CurrentCount
		{
			get { lock (theLock) return permits; }
		}
	}
}