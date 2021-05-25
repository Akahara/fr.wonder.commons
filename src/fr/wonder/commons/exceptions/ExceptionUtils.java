package fr.wonder.commons.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	
	public static String toString(Throwable t) {
		StringWriter w = new StringWriter();
		PrintWriter p = new PrintWriter(w);
		t.printStackTrace(p);
		return w.toString();
	}
	
}
