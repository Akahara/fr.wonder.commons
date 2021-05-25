package fr.wonder.commons.types;

@FunctionalInterface
public interface ERunnable<T extends Throwable> {
	
	public static final ERunnable<?> NOOP = () -> {};
	
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> ERunnable<T> noop() {
		return (ERunnable<T>) NOOP;
	}
	
	public void run() throws T;
	
}
