package fr.wonder.commons.loggers;

public class AnsiLogger extends ImediateLogger {

	private static final String ANSI = "\u001b[38;5;";
	private static final String LOG_HEADER = ANSI+"15m[";
	private static final String DEBUG_HEADER = "DBG ";
	private static final String DEBUG_FOOTER = "] "+ANSI+"250m";
	private static final String INFO_HEADER = "INF ";
	private static final String INFO_FOOTER = "] "+ANSI+"253m";
	private static final String WARN_HEADER = "WRN ";
	private static final String WARN_FOOTER = "] "+ANSI+"11m";
	private static final String ERROR_HEADER = "ERR ";
	private static final String ERROR_FOOTER = "] "+ANSI+"196m";
	private static final String LOG_FOOTER = "\u001b[0m";

	public AnsiLogger(String name) {
		super(name);
	}
	
	public AnsiLogger(String name, int logLevel) {
		super(name, logLevel);
	}
	
	public synchronized void log(String s, int level) {
		if(logLevel <= level) {
			String header = DEBUG_HEADER;
			String footer = DEBUG_FOOTER;
			if(level >= LEVEL_ERROR) {
				header = ERROR_HEADER;
				footer = ERROR_FOOTER;
			} else if(level >= LEVEL_WARN) {
				header = WARN_HEADER;
				footer = WARN_FOOTER;
			} else if(level >= LEVEL_INFO) {
				header = INFO_HEADER;
				footer = INFO_FOOTER;
			}
			super.log(LOG_HEADER + header + getThreadHeader() + footer + nameHeader + s, level);
		}
	}
	
	public synchronized void info(String s) {
		super.info(LOG_HEADER + INFO_HEADER + getThreadHeader() + INFO_FOOTER + nameHeader + s + LOG_FOOTER);
	}
	
	public synchronized void warn(String s) {
		super.info(LOG_HEADER + WARN_HEADER + getThreadHeader() + WARN_FOOTER + nameHeader + s + LOG_FOOTER);
	}

	public synchronized void debug(String s) {
		super.debug(LOG_HEADER + DEBUG_HEADER + getThreadHeader() + DEBUG_FOOTER + nameHeader + s + LOG_FOOTER);
	}
	
	public synchronized void err(String s) {
		super.err(LOG_HEADER + ERROR_HEADER + getThreadHeader() + ERROR_FOOTER + nameHeader + s + LOG_FOOTER);
	}
	
	private String getThreadHeader() {
		return Thread.currentThread().getName();
	}
	
}
