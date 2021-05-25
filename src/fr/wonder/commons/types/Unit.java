package fr.wonder.commons.types;

import java.util.Objects;

public class Unit<T> {
	
	public T val;
	
	public Unit(T val) {
		this.val = val;
	}
	
	public Unit() {
		this(null);
	}
	
	@Override
	public String toString() {
		return Objects.toString(val);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Unit))
			return false;
		Unit<?> t = (Unit<?>) o;
		return (val == null ? t.val == null : val.equals(t.val));
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(val);
	}
}
