public class LinkedQueue<E> { // MichaelScott's Queue

	// the queue node
	private static class Node<V> {
		final AtomicReference<Node<V>> next;
		final V data;

		Node(V data) {
			next = new AtomicReference<Node<V>>(null);
			this.data = data;
		}
	}

	// the head and tail references
	private final AtomicReference<Node<E>> head;
	private final AtomicReference<Node<E>> tail;

	public LinkedQueue() {
		Node<E> sentinel = new Node<E>(null);
		head = new AtomicReference<Node<E>>(sentinel);
		tail = new AtomicReference<Node<E>>(sentinel);
	}

	// enqueue a datum
	public void enqueue(E data) {
		Node<E> newNode = new Node<E>(data);

		while (true) {
			Node<E> observedTail = tail.get();
			Node<E> observedTailNext = observedTail.next.get();
			if (observedTail == tail.get()) {	// confirm that we have a good tail, to prevent CAS failures
				if (observedTailNext != null) { /** step A **/
					// queue in intermediate state, so advance tail for some other thread
					tail.compareAndSet(observedTail, observedTailNext);		/** step B **/
				} else {
					// queue in quiescent state, try inserting new node
					if (observedTail.next.compareAndSet(null, newNode)) {	/** step C **/
						// advance the tail
						tail.compareAndSet(observedTail, newNode);	/** step D **/
						break;
					}
				}
			}
		}
	}
	
	public E dequeue (){
		while (true){
			Node <E> first = head.get();
			Node <E> last = tail.get();
			Node <E> next = first.next.get();
			
			if(first == head.get()){
				if(first == last) {
					if(next == null) return null;
					tail.compareAndSet(last,next);
				} else {
					E item = next.data;
					if(head.compareAndSet(first,next))
						return item;
					
				}
			}
		}
    }
	
	
	
	
}