package fr.wonder.commons.utils;

public class Assertions {

	public static boolean assertTrue(boolean b, String e) {
		if(!b)
			throw new AssertionError(e);
		return b;
	}
	
	public static boolean assertTrue(boolean b) {
		return assertTrue(b, null);
	}
	
	public static boolean assertFalse(boolean b, String e) {
		if(b)
			throw new AssertionError(e);
		return b;
	}
	
	public static boolean assertFalse(boolean b) {
		return assertFalse(b, null);
	}
	
	public static <T> T assertNonNull(T o, String e) {
		if(o == null)
			throw new AssertionError(e);
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
			throw new AssertionError(e);
	}
	
}
