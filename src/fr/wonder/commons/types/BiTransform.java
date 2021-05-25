package fr.wonder.commons.types;

import java.util.function.Function;

public class BiTransform<T, K> extends Tuple<Function<T, K>, Function<K, T>> {
	
	public BiTransform(Function<T, K> function, Function<K, T> reciprocity) {
		super(function, reciprocity);
	}
	
	public K apply(T x) {
		return a.apply(x);
	}
	
	public T applyRec(K y) {
		return b.apply(y);
	}
	
}
