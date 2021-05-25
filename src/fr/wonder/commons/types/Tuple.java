package fr.wonder.commons.types;

import java.util.Objects;

public class Tuple<A, B> {
	
	public A a;
	public B b;
	
	public Tuple(A first, B second) {
		this.a = first;
		this.b = second;
	}
	
	@Override
	public String toString() {
		return Objects.toString(a) + " " + Objects.toString(b);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Tuple))
			return false;
		Tuple<?, ?> t = (Tuple<?, ?>) o;
		return  (a == null ? t.a == null : a.equals(t.a)) &&
				(b == null ? t.b == null : b.equals(t.b));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}

}
