package fr.wonder.commons.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventDispatcher<T> implements Obserbable<T> {
	
	private final List<Consumer<T>> listeners = new ArrayList<>();
	
	public void dispatchEvent(T event) {
		for(Consumer<T> listener : listeners)
			listener.accept(event);
	}
	
	public void addListener(Consumer<T> listener) {
		listeners.add(listener);
	}
	
	public void removeListener(Consumer<T> listener) {
		listeners.remove(listener);
	}
	
}
