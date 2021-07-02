package fr.wonder.commons.utils;

import java.util.Arrays;
import java.util.Map;
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
		if (o.getClass().isArray())
			return Arrays.deepToString((Object[]) o);
		return o.toString();
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
	
}
