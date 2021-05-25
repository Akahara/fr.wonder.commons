package fr.wonder.commons.exceptions;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import fr.wonder.commons.loggers.Logger;

/**
 * Useful class for error handling, a common use of an error wrapper is as follow:
 * <blockquote><pre>
 * void foo() {
 *   ErrorWrapper errors = new ErrorWrapper("Something happened");
 *   try {
 *     ...
 *     bar(..., errors.subErrors("Unable to bar!"));
 *     errors.assertNoErrors();
 *     ...
 *     // possible reuse of #errors
 *   } catch (CompilationError x) {
 *     errors.dump();
 *   }
 * }
 * 
 * void bar(..., ErrorWrapper errors) {
 *   ...
 *   if(thingsWentWrong)
 *     errors.add("Things went wrong!");
 *   ...
 * }
 * </pre></blockquote>
 */
public class ErrorWrapper {
	
	private final boolean logTraces;
	
	private final String header;
	private final List<String> errors = new ArrayList<>();
	
	private final List<ErrorWrapper> subErrors = new ArrayList<>();

	public ErrorWrapper(String header) {
		this(header, false);
	}
	
	public ErrorWrapper(String header, boolean logTraces) {
		this.header = header;
		this.logTraces = logTraces;
	}
	
	public void add(String s) {
		for(String l : s.split("\n"))
			errors.add(l);
		if(true) {
//			String trace = "";
//			for(StackTraceElement t : new Exception().getStackTrace())
//				trace += "("+t.getFileName()+":"+t.getLineNumber()+") ";
//			errors.add(trace);
			StackTraceElement[] trace = new Exception().getStackTrace();
			for(int i = 1; i < trace.length; i++)
				errors.add(" from " + trace[i].toString());
		}
	}

	public void trace(String s) {
		add(s + ExceptionUtils.toString(new Error()));
	}
	
	public ErrorWrapper subErrrors(String header) {
		ErrorWrapper sub = new ErrorWrapper(header, logTraces);
		subErrors.add(sub);
		return sub;
	}
	
	public boolean noErrors() {
		if(!errors.isEmpty())
			return false;
		for(ErrorWrapper sub : subErrors)
			if(!sub.noErrors())
				return false;
		return true;
	}
	
	public void dump() {
		dump(System.err, 0);
	}
	
	public void dump(PrintStream out) {
		dump(out, 0);
	}
	
	public void dump(Logger logger) {
		dump(logger, 0);
	}
	
	private void dump(PrintStream out, int level) {
		if(noErrors())
			return;
		out.println("| ".repeat(level) + header+":");
		for(String e : errors)
			out.println("| ".repeat(level+1) + e);
		for(ErrorWrapper sub : subErrors)
			sub.dump(out, level+1);
	}
	
	private void dump(Logger logger, int level) {
		if(noErrors())
			return;
		logger.err("| ".repeat(level) + header+":");
		for(String e : errors)
			logger.err("| ".repeat(level+1) + e);
		for(ErrorWrapper sub : subErrors)
			sub.dump(logger, level+1);
	}
	
	/**
	 * A function that uses this method at least once should call it again after the
	 * last {@link #add(String)}, this way the catcher won't have to check if errors
	 * were generated. Usually the simplest is to put an additional
	 * {@code errors.assertNoErrors()} at the end of the function's body.
	 */
	public void assertNoErrors() throws WrappedException {
		if(!noErrors())
			throw new WrappedException(this);
	}
	
	/**
	 * The exception thrown by {@link ErrorWrapper#assertNoErrors()}, it contains
	 * the error wrapper that throw the exception
	 */
	public static class WrappedException extends Exception {

		private static final long serialVersionUID = 2860084951467460601L;
		
		public final ErrorWrapper errors;
		
		private WrappedException(ErrorWrapper wrapper) {
			super("Errors occured");
			this.errors = wrapper;
		}
		
		@Override
		public void printStackTrace(PrintStream stream) {
			super.printStackTrace(stream);
			errors.dump(stream);
		}
		
	}
	
}
