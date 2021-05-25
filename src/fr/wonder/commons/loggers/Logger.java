package fr.wonder.commons.loggers;

import java.io.Flushable;
import java.io.PrintStream;

public abstract class Logger implements Flushable {
	
	public static final int LEVEL_DEBUG = 0;
	public static final int LEVEL_INFO = 100;
	public static final int LEVEL_WARN = 200;
	public static final int LEVEL_ERROR = 300;
	
	protected static final String traceLineHeader = "    at ";
	
	protected final String nameHeader;
	
	protected int logLevel;
	protected PrintStream out = System.out;
	
	private static Logger defaultLogger = new SimpleLogger(null, LEVEL_INFO);
	
	public Logger(String name, int logLevel) {
		this.nameHeader = name == null ? "" : name + ": ";
		this.logLevel = logLevel;
	}
	
	public Logger(String name) {
		this(name, LEVEL_INFO);
	}
	
	public static Logger getDefault() {
		return defaultLogger;
	}

	public static void setDefault(Logger logger) {
		defaultLogger = logger;
	}
	
	public void setLogLevel(int level) {
		logLevel = level;
	}
	
	public int getLogLevel() {
		return logLevel;
	}
	
	public Logger redirectOut(PrintStream stream) {
		if(stream == null)
			throw new NullPointerException("The target stream of cannot be null.");
		else
			out = stream;
		return this;
	}
	
	@Override
	public void flush() {
		out.flush();
	}
	
	public void close() {
		if(out != System.out)
			out.close();
	}
	
	public LogStream asStream(int logLevel) {
		return new LogStream(this, logLevel);
	}
	
	public abstract void log(String s, int level);
	
	public abstract void info(String s);
	
	public abstract void warn(String s);
	
	public abstract void debug(String s);
	
	public abstract void err(String s);

	public void mwarn(String s, int traceSize) {
		warn(s);
		StackTraceElement[] trace = new Exception().getStackTrace();
		traceSize++;
		for(int i = 1; i < trace.length && i < traceSize; i++) {
			warn(traceLineHeader + trace[i].toString());
		}
	}
	
	public void err(Throwable t, String s) {
		err(s == null ? t.toString() : s + ": " + t.toString());
	}
	
	public void printStackTrace() {
		merr("StackTraceReport");
	}
	
	public void merr(String s) {
		merr(new Exception(), s);
	}
	
	public void merr(Throwable t) {
		merr(t, null);
	}
	
	public void merr(Throwable t, String s) {
		err(t, s);
		while(true) {
			for(StackTraceElement e : t.getStackTrace())
				err(traceLineHeader + e.toString());
			t = t.getCause();
			if(t == null)
				break;
			err("Caused by: " + t);
		}
	}
	
}
