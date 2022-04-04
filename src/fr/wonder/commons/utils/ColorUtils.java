package fr.wonder.commons.utils;

public class ColorUtils {

	public static final class ANSI {
		
		public static final char CODEPOINT = '\u001b';
		public static final String
				MARKER = String.valueOf(CODEPOINT),
				WHITE  = "\u001b[38;5;15m",
				GRAY   = "\u001b[38;5;250m",
				RED    = "\u001b[38;5;196m",
				YELLOW = "\u001b[38;5;11m",
				RESET  = "\u001b[0m";
	}
	
	public static String stripAnsi(String text) {
		return stripAnsi(new StringBuilder(text)).toString();
	}
	
	public static StringBuilder stripAnsi(StringBuilder text) {
		int pos;
		while((pos = text.lastIndexOf(ANSI.MARKER)) != -1) {
			int m = text.indexOf("m", pos);
			if(m == -1)
				throw new IllegalArgumentException("Malformed ANSI sequence");
			text.delete(pos, m+1);
		}
		return text;
	}
	
}
