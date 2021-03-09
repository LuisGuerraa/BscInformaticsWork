class SafeBoundedLazy<E> {
	
	private static class ValueHolder<V> {
		V value;
		int availableLives;

		ValueHolder(V value, int lives) {
			this.value = value;
			availableLives = lives;
		}
		ValueHolder() {}	
	}
	
	
	// Configuration arguments
	private final Supplier<E> supplier;
	private final int lives;
	/**
		* The possible states:
		* null: means UNCREATED
		* CREATING and ERROR: mean exactly that
		* != null && != ERROR && != CREATING: means CREATED
	*/

	private final ValueHolder<E> ERROR = new ValueHolder<>();
	private final ValueHolder<E> CREATING = new ValueHolder<>();
	
	// The current state
	private final AtomicReference<ValueHolder<E>> state = new AtomicReference(null); // null -> valor inicial default de state
	
	// When the synchronizer is in ERROR state, the exception is hold here
	Throwable errorException;
	
		// Construct a BoundedLazy
	public SafeBoundedLazy(Supplier<E> supplier, int lives) {
		if (lives < 1)
			throw new IllegalArgumentException();
		this.supplier = supplier;
		this.lives = lives;
	}
	
	// Returns an instance of the underlying type
	public Optional<E> get() throws Throwable {
		
		while (true) {
			
			ValueHolder observedValue = state.get();			
			
			if (observedValue == ERROR){
				if(state.compareAndSet(observedValue,observedValue)) // 2 parametros têm de ser do mesmo tipo
					throw errorException;
			}	
			if (observedValue == null) {
				if(state.compareAndSet(observedValue,CREATING)){
					try {
						E value = supplier.get();
						ValueHolder updatedState = null;
						if (lives > 1) {
							updatedState = new ValueHolder<E>(value, lives - 1); //lives remaining
						}
						state.set(updatedState);
						return Optional.of(value);
					
					} catch (Throwable ex) {
						errorException = ex;
						state.set(ERROR);
						throw ex;
					}
				}
			} else if (observedValue == CREATING) {
					while (true){// spin until state != CREATING
						if(state.compareAndSet(observedValue,observedValue)) // Creating , creating era igual
							Thread.yield();
						else break;
						
					}
			}else { // state is CREATED: we have at least one life
				
					Optional<E> retValue = Optional.of(state.value);
					int updatedLives = state.availableLives -1;
					ValueHolder updatedValue = new ValueHolder (state.value,updatedLives);  
					if (updatedLives == 0)
						updatedValue = null;
						
						/*if(!state.compareAndSet(observedValue,null)){
							continue;
						}	*/
					 if(state.compareAndSet(observedValue,updatedValue))
						 return retValue
			}
		}
	}
}