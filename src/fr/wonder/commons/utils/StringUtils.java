package fr.wonder.commons.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

	/**
	 * A {@code Pattern} object that can be used to find string literals in a
	 * string.<br>
	 * The pattern will look for quotes enclosing the text S with {@code S} being a
	 * string containing no {@code "} character except for those preceded by an odd
	 * number of {@code \} (escape char).
	 * 
	 * <p>
	 * Beware that only double quotes are matched.
	 * 
	 * <p>
	 * For example <blockquote>
	 * 
	 * <pre>
	 * String text = "Text: \"citation of \\\"The book\\\"\"";
	 * Matcher m = STRING_PATTERN.matcher(text);
	 * m.find();
	 * System.out.println(m.group());
	 * </blockquote>
	 * </pre>
	 * 
	 * will print <blockquote>
	 * 
	 * <pre>
	 * citation of "The book"
	 * </blockquote>
	 * </pre>
	 */
	public static final Pattern STRING_PATTERN = Pattern
			.compile("\"(([^\"\\\\]*)|(\\\\(\\\\\\\\)*[^\\\\]))*(\\\\\\\\)*\"");
	
	/**
	 * The word pattern is just "\w+"
	 */
	public static final Pattern WORD_PATTERN = Pattern
			.compile("\\w+");
	
	/**
	 * Default char sets, initialized at first use only.
	 */
	public static class Charsets {
		
		/** a-z A-Z */
		public static final char[] CHARSET_ALPHABET = 
				( "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvwxyz").toCharArray();
		
		/** 0-9 */
		public static final char[] CHARSET_NUMBERS =
				"0123456789".toCharArray();
		
		/** , . ; : ! ? */
		public static final char[] CHARSET_PONCTUATION =
				",.;:!?".toCharArray();

		/** ( ) { } [ ] ' " & */
		public static final char[] CHARSET_SECTIONS =
				"(){}[]\'\"&".toCharArray();
		
		/** + - / * % &lt; &gt; = & | ~ */
		public static final char[] CHARSET_OPERATORS =
				"+-*/%<>=&|~".toCharArray();
		
		/**
		 * <p>
		 * Not a static variable to avoid storing useless data, if you must use this
		 * value multiple times you should probably cache it yourself
		 * 
		 * <p>
		 * This value contains all the characters that can be used to name variables in
		 * most programming languages :
		 * <ul>
		 * <li>The alphabet (a-z + A-Z)</li>
		 * <li>All digits (0-9)</li>
		 * <li>The underscore (_)</li>
		 * </ul>
		 */
		public static final char[] getCharsetVarName() {
			return
				( "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvwxyz"
				+ "_0123456789").toCharArray();
		}
	}
	
	/**
	 * This method will remove all white spaces ("\\s" regex) from the given text
	 * without modifying quoted text, see {@link #STRING_PATTERN} for the detection
	 * of quoted text.
	 * 
	 * @param text the text to strip of white spaces
	 * @return the stripped text
	 */
	public static String stripContent(String text) {
		Matcher m = STRING_PATTERN.matcher(text);
		String stripped = "";
		int start = 0;
		while (m.find(start)) {
			stripped += text.substring(start, m.start()).replaceAll("\\s", "");
			stripped += m.group();
			start = m.end();
		}
		stripped += text.substring(start).replaceAll("\\s", "");
		return stripped;
	}

	public static String escape(String text, char quoteSymbol) {
		return text
				.replaceAll("\\", "\\\\")
				.replaceAll(String.valueOf(quoteSymbol), "\\" + quoteSymbol);
	}

	public static String toString(Map<?, ?> map) {
		return map.keySet().stream().map(k -> k + "=" + deepToString(map.get(k)))
				.collect(Collectors.joining(", ", "{", "}"));
	}

	/**
	 * Returns the deep String representation of object {@code o}.
	 * <p>
	 * If {@code o} is {@code null}, {@code "null"} is returned, if
	 * {@code o} is an array, {@link Arrays#deepToString(Object[])}
	 * is returned, otherwise {@code o.toString()} is returned.
	 * 
	 * @param o the object
	 * @return the string representation of the object
	 */
	public static String deepToString(Object o) {
		if (o == null)
			return "null";
		if (o.getClass().isArray()) {
			// o cannot be casted to Object[], use a temporary object array
			String repr = Arrays.deepToString(new Object[] {o});
			return repr.substring(2, repr.length()-2);
		}
		return o.toString();
	}

	@SafeVarargs
	public static <T> String join(String delimiter, T... objects) {
		return join(delimiter, objects, String::valueOf);
	}
	
	public static String join(String delimiter, Collection<?> objects) {
		return join(delimiter, objects, String::valueOf);
	}
	
	public static <T> String join(String delimiter, T[] objects, Function<T, String> function) {
		String[] strings = new String[objects.length];
		ArrayOperator.map(objects, strings, function);
		return String.join(delimiter, strings);
	}
	
	public static <T> String join(String delimiter, Collection<T> objects, Function<T, String> function) {
		String[] strings = objects.stream().map(function).toArray(String[]::new);
		return String.join(delimiter, strings);
	}
	
	/**
	 * Returns whether {@code c} is a letter (a-z or A-Z)
	 * 
	 * @param c the character
	 * @return {@code true} if {@code c} is a letter
	 */
	public static boolean isLetterChar(char c) {
		return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
	}
	
	/**
	 * Returns whether {@code c} is a digit (0-9)
	 * 
	 * @param c the character
	 * @return {@code true} if {@code c} is a digit
	 */
	public static boolean isDigitChar(char c) {
		return '0' <= c && c <= '9';
	}
	
	/**
	 * Returns whether {@code c} is a word character, that is if it matches the "\w"
	 * regex.
	 * 
	 * @param c the character to test
	 * @return {@code true} if {@code c} is a word character
	 */
	public static boolean isWordChar(char c) {
		return isLetterChar(c) || c == '_' || isDigitChar(c);
	}
	
	/**
	 * Creates a string representation of an object recursively.
	 * 
	 * <p>
	 * This method explores all fields of the given object (recursively)
	 * and appends them to a string representation. This method <b>does not</b>
	 * handle recursive data structures, if there is a cycle in the data
	 * (obj1 has a field with value obj2 and obj2 has a field with value obj1)
	 * this method will cause a stack overflow.
	 * 
	 * <p>
	 * Note that if the object's class is not exported by its module most fields
	 * won't be accessed and "{}" will be returned.
	 */
	public static String toObjectString(Object o) {
		if(o == null)
			return "null";
		Class<?> clazz = o.getClass();
		StringBuilder sb = new StringBuilder();
		if(Number.class.isAssignableFrom(clazz) || clazz == Boolean.class) {
			return o.toString();
		} else if(clazz == String.class) {
			return '"' + (String) o + '"';
		} else if(clazz.isArray()) {
			int len = Array.getLength(o);
			if(len == 0)
				return "[]";
			sb.append('[');
			for(int i = 0; i < len; i++) {
				sb.append(toObjectString(Array.get(o, i)));
				sb.append(", ");
			}
			sb.setLength(sb.length()-2);
			sb.append(']');
		} else if(clazz.isEnum()) {
			return o.toString();
		} else {
			Field[] fields = clazz.getFields();
			if(fields.length == 0)
				return "{}";
			sb.append('{');
			for(Field f : fields) {
				try {
					if(!f.trySetAccessible())
						continue;
					sb.append(f.getName() + "=");
					sb.append(toObjectString(f.get(o)));
					sb.append(", ");
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException("Unable to access field " + f.getName(), e);
				}
			}
			if(sb.length() > 1)
				sb.setLength(sb.length()-2);
			sb.append("}");
		}
		return sb.toString();
	}
	
	public static String[] splitWithQuotes(String text, String separator) throws IllegalArgumentException {
		return splitWithQuotes(text, separator, new char[] { '\'', '"' });
	}
	// FIX this method currently DOES NOT work, use cautiously or not at all (fix soon)
	public static String[] splitWithQuotes(String text, String separator, char[] quoteMarkers) throws IllegalArgumentException {
		if(text.isEmpty())
			return new String[0];
		
		int[] q = new int[quoteMarkers.length];
		
		for(int i = 0; i < q.length; i++)
			q[i] = text.indexOf(quoteMarkers[i]);
		
		int mq = minIdx(q);
		int s = text.indexOf(separator);
		if(q[mq] == -1 && s == -1)
			return new String[] { text };
		List<String> parts = new ArrayList<>();
		int l = 0;
		do {
			if(q[mq] != -1 && q[mq] < s) {
				int l2 = text.indexOf(quoteMarkers[mq], q[mq]+1);
				if(l2 == -1)
					throw new IllegalArgumentException("Unfinished quote begining at position " + l);
				if(l != q[mq])
					parts.add(text.substring(l, q[mq]));
				parts.add(text.substring(q[mq]+1, l2));
				l = l2+1;
			} else {
				if(l != s)
					parts.add(text.substring(l, s));
				l = s+separator.length();
			}
			
			for(int i = 0; i < q.length; i++)
				if(q[i] != -1 && q[i] < l)
					q[i] = text.indexOf(quoteMarkers[i], l);
			if(s != -1 && s < l)
				s = text.indexOf(separator, l);
		} while(q[mq] != -1 || s != -1);
		if(l != text.length())
			parts.add(text.substring(l));
		return parts.toArray(String[]::new);
	}
	
	/** Utility method used by {@link #splitWithQuotes(String, String, char[])} */
	private static int minIdx(int[] m) {
		int idx = 0, v = m[0];
		for(int i = 1; i < m.length; i++) {
			int mi = m[i];
			if(v == -1 || (mi >= 0 && m[i] < v)) {
				v = mi;
				idx = i;
			}
		}
		return idx;
	}
	
}
