package fr.wonder.commons.loggers;

import java.io.PrintStream;

public class NullLogger extends Logger {

	public NullLogger() {
		super(null);
	}
	
	@Override
	public void setLogLevel(int level) {
		// pass
	}
	
	@Override
	public int getLogLevel() {
		return 0;
	}
	
	@Override
	public Logger redirectOut(PrintStream stream) {
		throw new IllegalAccessError();
	}

	@Override
	public void log(String s, int level) {
		throw new IllegalAccessError();
	}

	@Override
	public void info(String s) {
		throw new IllegalAccessError();
	}

	@Override
	public void warn(String s) {
		throw new IllegalAccessError();
	}

	@Override
	public void debug(String s) {
		throw new IllegalAccessError();
	}

	@Override
	public void err(String s) {
		throw new IllegalAccessError();
	}

}
