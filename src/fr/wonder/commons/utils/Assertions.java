package fr.wonder.commons.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Assertions {
	
	private static AssertionError assertionError(String s) {
		AssertionError e = new AssertionError(s == null ? "" : s);
		StackTraceElement[] trace = e.getStackTrace();
		int removeCount = 1;
		String className = Assertions.class.getName();
		while(trace[removeCount].getClassName().equals(className))
			removeCount++;
		e.setStackTrace(Arrays.copyOfRange(trace, removeCount, trace.length));
		return e;
	}
	
	public static boolean assertTrue(boolean b, String e) {
		if(!b)
			throw assertionError(e);
		return b;
	}
	
	public static boolean assertTrue(boolean b) {
		return assertTrue(b, null);
	}
	
	public static boolean assertFalse(boolean b, String e) {
		if(b)
			throw assertionError(e);
		return b;
	}
	
	public static boolean assertFalse(boolean b) {
		return assertFalse(b, null);
	}
	
	public static <T> T assertNonNull(T o, String e) {
		if(o == null)
			throw assertionError(e);
		return o;
	}
	
	public static <T> T assertNonNull(T o) {
		return assertNonNull(o, null);
	}
	
	@SafeVarargs
	public static <T> void assertNonNull(String e, T... os) {
		assertNonNull(os, e);
		for(Object o : os)
			assertNonNull(o, e);
	}

	public static void assertNull(Object o, String e) {
		if(o != null)
			throw assertionError(e);
	}
	
	public static void assertNull(Object o) {
		assertNull(o, null);
	}

	public static <T> T[] assertEmpty(T[] c, String e) {
		if(c == null || c.length != 0)
			throw assertionError(e);
		return c;
	}
	
	public static <T> T[] assertEmpty(T[] c) {
		return assertEmpty(c, null);
	}

	public static <T extends Collection<?>> T assertEmpty(T c, String e) {
		if(c == null || !c.isEmpty())
			throw assertionError(e);
		return c;
	}
	
	public static <T extends Collection<?>> T assertEmpty(T c) {
		return assertEmpty(c, null);
	}
	
	public static <T extends Map<?, ?>> T assertEmpty(T c, String e) {
		if(c == null || !c.isEmpty())
			throw assertionError(e);
		return c;
	}
	
	public static <T extends Map<?, ?>> T assertEmpty(T c) {
		return assertEmpty(c, null);
	}

	public static <T extends Collection<?>> T assertFilled(T c, String e) {
		if(c == null || c.isEmpty())
			throw assertionError(e);
		return c;
	}

	public static <T> T[] assertFilled(T[] c, String e) {
		if(c == null || c.length == 0)
			throw assertionError(e);
		return c;
	}
	
	public static <T> T[] assertFilled(T[] c) {
		return assertFilled(c, null);
	}
	
	public static <T extends Collection<?>> T assertFilled(T c) {
		return assertFilled(c, null);
	}
	
	public static <T extends Map<?, ?>> T assertFilled(T c, String e) {
		if(c == null || c.isEmpty())
			throw assertionError(e);
		return c;
	}
	
	public static <T extends Map<?, ?>> T assertFilled(T c) {
		return assertFilled(c, null);
	}

	@SafeVarargs
	public static <T> void assertIn(T obj, String e, T... objects) {
		for(T t : objects) {
			if(Objects.equals(obj, t))
				return;
		}
		throw assertionError(e);
	}

	@SafeVarargs
	public static <T> void assertIn(T obj, T... objects) {
		assertIn(obj, null, objects);
	}
	
	public static <T> void assertIn(T obj, String e, Collection<? extends T> objects) {
		for(T t : objects) {
			if(Objects.equals(obj, t))
				return;
		}
		throw assertionError(e);
	}
	
	public static <T> void assertIn(T obj, Collection<? extends T> objects) {
		assertIn(obj, null, objects);
	}

}
