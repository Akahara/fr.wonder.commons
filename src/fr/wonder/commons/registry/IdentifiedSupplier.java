package fr.wonder.commons.registry;

public interface IdentifiedSupplier<T> extends RegistryElement<String> {

	public T get();
	
}
