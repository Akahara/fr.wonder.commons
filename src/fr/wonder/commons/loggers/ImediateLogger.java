package fr.wonder.commons.loggers;

/**
 * Prints all information directly to this logger's output stream
 * without any header after the log level validation check.
 */
public class ImediateLogger extends Logger {

	public ImediateLogger(String name) {
		super(name);
	}
	
	public ImediateLogger(String name, int logLevel) {
		super(name, logLevel);
	}

	public synchronized void log(String s, int level) {
		if(logLevel <= level) {
			out.println(s);
		}
	}
	
	public synchronized void info(String s) {
		if(logLevel <= LEVEL_INFO)
			out.println(s);
	}
	
	public synchronized void warn(String s) {
		if(logLevel <= LEVEL_WARN)
			out.println(s);
	}

	public synchronized void debug(String s) {
		if(logLevel <= LEVEL_DEBUG)
			out.println(s);
	}
	
	public synchronized void err(String s) {
		if(logLevel <= LEVEL_ERROR)
			out.println(s);
	}
	
	
}
