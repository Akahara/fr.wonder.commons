package fr.wonder.commons.types;

import java.util.Objects;

public class Triplet<A, B, C> {
	
	public A a;
	public B b;
	public C c;
	
	public Triplet(A first, B second, C third) {
		this.a = first;
		this.b = second;
		this.c = third;
	}
	
	@Override
	public String toString() {
		return Objects.toString(a) + " " + Objects.toString(b) + " " + Objects.toString(c);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Triplet))
			return false;
		Triplet<?, ?, ?> t = (Triplet<?, ?, ?>) o;
		return Objects.equals(a, t.a) &&
				Objects.equals(b, t.b) &&
				Objects.equals(c, t.c);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(a, b, c);
	}
	
}
