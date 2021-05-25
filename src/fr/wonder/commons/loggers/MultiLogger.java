package fr.wonder.commons.loggers;

import java.util.ArrayList;
import java.util.List;

public class MultiLogger extends Logger {
	
	private final List<Logger> loggers = new ArrayList<>();
	
	public MultiLogger() {
		super(null);
	}
	
	public MultiLogger addLogger(Logger l) {
		if(l == null)
			throw new NullPointerException();
		if(!loggers.contains(l))
			loggers.add(l);
		return this;
	}
	
	public void removeLogger(Logger l) {
		loggers.remove(l);
	}
	
	@Override
	public void log(String s, int level) {
		for(Logger l : loggers)
			l.log(s, level);
	}

	@Override
	public void info(String s) {
		for(Logger l : loggers)
			l.info(s);
	}

	@Override
	public void warn(String s) {
		for(Logger l : loggers)
			l.warn(s);
	}

	@Override
	public void debug(String s) {
		for(Logger l : loggers)
			l.debug(s);
	}

	@Override
	public void err(String s) {
		for(Logger l : loggers)
			l.err(s);
	}
	
	@Override
	public void close() {
		for(Logger l : loggers)
			l.close();
	}

}
