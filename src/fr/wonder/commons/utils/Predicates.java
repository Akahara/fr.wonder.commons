package fr.wonder.commons.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class Predicates {

	public static boolean isNull(Object o) {
		return o == null;
	}
	
	public static <T> Predicate<T> equalilty(T other) {
		return t -> Objects.equals(t, other);
	}
	
	public static <T> Predicate<T> isOneOf(Collection<T> objects) {
		return t -> objects.contains(t);
	}
	
	@SafeVarargs
	public static <T> Predicate<T> isOneOf(T... objects) {
		Set<T> set = new HashSet<>(objects.length);
		for(T o : objects)
			set.add(o);
		return isOneOf(set);
	}
	
}
