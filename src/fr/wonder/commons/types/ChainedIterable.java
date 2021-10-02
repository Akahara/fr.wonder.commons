package fr.wonder.commons.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ChainedIterable<T> implements Iterable<T> {
	
	private final List<Iterable<T>> iterators = new ArrayList<>();
	
	public ChainedIterable() {}

	public ChainedIterable(Iterable<T>[] iterators) {
		for(Iterable<T> it : iterators)
			this.iterators.add(it);
	}
	
	public ChainedIterable<T> add(Iterable<T> iterable) {
		this.iterators.add(iterable);
		return this;
	}
	
	public ChainedIterable<T> add(Collection<Iterable<T>> iterables) {
		this.iterators.addAll(iterables);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator() {
		return new ChainedIterator<>(
				iterators.stream()
				.map(Iterable::iterator)
				.toArray(Iterator[]::new));
	}
	
	public static class ChainedIterator<T> implements Iterator<T> {
		
		private final Iterator<T>[] iterators;
		private int currentIterator;
		
		private ChainedIterator(Iterator<T>[] iterators) {
			this.iterators = iterators;
		}
		
		@Override
		public boolean hasNext() {
			while(currentIterator != iterators.length && !iterators[currentIterator].hasNext())
				currentIterator++;
			return currentIterator != iterators.length;
		}
		
		@Override
		public T next() {
			return iterators[currentIterator].next();
		}
		
	}

}