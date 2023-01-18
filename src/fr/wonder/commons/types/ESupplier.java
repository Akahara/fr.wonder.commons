package fr.wonder.commons.types;

public interface ESupplier<T, E extends Throwable> {

	T get() throws E;
	
}
