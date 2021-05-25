package fr.wonder.commons.events;

import java.util.function.Consumer;

public interface Obserbable<T> {
	
	public void addListener(Consumer<T> consumer);
	public void removeListener(Consumer<T> consumer);
	
}
