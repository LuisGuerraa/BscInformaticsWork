import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;


public class TransferQueue<E>{
	
	private final LinkedQueue<E> messages;
	private volatile int waiters; //only take
	private final Lock lock;
	private final Condition okToAcquire;
	
	public TransferQueue <E> (){
		messages = new LinkedQueue<E>();
		waiters = 0;
		lock = new ReentrantLock();
		okToAcquire = lock.newCondition();	
	}
	
	
	 public void put(E message) {
		 messages.enqueue(message);
		 
		 if(waiters >0){
			 lock.lock();
			 try{
				 if(waiters > 0) okToAcquire.signal();
			 } finally {
				 lock.unlock();
			 }
		 }
		 
	 }
	 
	public E take(long timeout) throws InterruptedException{
		 
		E value = messages.dequeue();
		 
		if(value != null)
			 return value;
		 
		if (timeout == 0)
			return null;
		
		boolean timed = timeout > 0;
        long nanosTimeout = timed ? TimeUnit.MILLISECONDS.toNanos(timeout) : 0L;
		lock.lock();
		try {
			waiters++;
			try {		
				do {
					E valueTemp = messages.dequeue()
					if(value != null)
						return value;
				
					if (timed && nanosTimeout <= 0)
						return null;
					if (timed)
						nanosTimeout = okToAcquire.awaitNanos(nanosTimeout);
					else
						okToAcquire.await();
				} while (true);
			} finally {
				waiters--;
			}	
		} finally {
			lock.unlock();
		}
	}
		
	
}

