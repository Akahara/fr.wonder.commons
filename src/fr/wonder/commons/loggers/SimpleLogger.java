package fr.wonder.commons.loggers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleLogger extends ImediateLogger {
	
	protected DateTimeFormatter timeFormater = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public SimpleLogger(String name) {
		super(name);
	}
	
	public SimpleLogger(String name, int logLevel) {
		super(name, logLevel);
	}
	
	public void setTimeFormatter(DateTimeFormatter formater) {
		try {
			if(formater != null)
				formater.format(LocalDateTime.now());
			timeFormater = formater;
		} catch(Exception e) {
			merr(e, "Invalid time formatter.");
		}
	}
	
	public synchronized void log(String s, int level) {
		super.log(getThreadHeader() + nameHeader + s, level);
	}
	
	public synchronized void info(String s) {
		super.info(getThreadHeader() + nameHeader + s);
	}
	
	public synchronized void warn(String s) {
		super.warn(getThreadHeader() + nameHeader + s);
	}

	public synchronized void debug(String s) {
		super.debug(getThreadHeader() + nameHeader + s);
	}
	
	public synchronized void err(String s) {
		super.err(getThreadHeader() + nameHeader + s);
	}
	
	private String getThreadHeader() {
		return "[" + Thread.currentThread().getName() + (timeFormater == null ? "":" "+timeFormater.format(LocalDateTime.now()) + "] ");
	}
}
