package fr.wonder.commons.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class Assertions {
	
	static AssertionError error(String s) {
		AssertionError e = new AssertionError(s);
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
			throw error(e);
		return b;
	}
	
	public static boolean assertTrue(boolean b) {
		return assertTrue(b, null);
	}
	
	public static boolean assertFalse(boolean b, String e) {
		if(b)
			throw error(e);
		return b;
	}
	
	public static boolean assertFalse(boolean b) {
		return assertFalse(b, null);
	}
	
	public static <T> T assertNonNull(T o, String e) {
		if(o == null)
			throw error(e);
		return o;
	}
	
	public static <T> T assertNonNull(T o) {
		return assertNonNull(o, null);
	}
	
	public static void assertNonNull(String e, Object... os) {
		assertNonNull(os, e);
		for(Object o : os)
			assertNonNull(o, e);
	}

	public static void assertNull(Object o, String e) {
		if(o != null)
			throw error(e);
	}
	
	public static void assertNull(Object o) {
		assertNull(o, null);
	}

	public static <T> T[] assertEmpty(T[] c, String e) {
		if(c == null || c.length != 0)
			throw error(e);
		return c;
	}
	
	public static <T> T[] assertEmpty(T[] c) {
		return assertEmpty(c, null);
	}

	public static <T extends Collection<?>> T assertEmpty(T c, String e) {
		if(c == null || !c.isEmpty())
			throw error(e);
		return c;
	}
	
	public static <T extends Collection<?>> T assertEmpty(T c) {
		return assertEmpty(c, null);
	}
	
	public static <T extends Map<?, ?>> T assertEmpty(T c, String e) {
		if(c == null || !c.isEmpty())
			throw error(e);
		return c;
	}
	
	public static <T extends Map<?, ?>> T assertEmpty(T c) {
		return assertEmpty(c, null);
	}

	public static <T extends Collection<?>> T assertFilled(T c, String e) {
		if(c == null || c.isEmpty())
			throw error(e);
		return c;
	}

	public static <T> T[] assertFilled(T[] c, String e) {
		if(c == null || c.length == 0)
			throw error(e);
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
			throw error(e);
		return c;
	}
	
	public static <T extends Map<?, ?>> T assertFilled(T c) {
		return assertFilled(c, null);
	}

}
