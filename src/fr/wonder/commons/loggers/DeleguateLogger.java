package fr.wonder.commons.loggers;

public class DeleguateLogger extends NullLogger {
	
	private String header;
	private Logger logger;
	
	public DeleguateLogger(Logger logger, String header) {
		if(logger == null)
			throw new NullPointerException();
		this.logger = logger;
		this.header = header == null ? "" : header+": ";
	}
	
	@Override
	public void log(String s, int level) {
		logger.log(header+s, level);
	}

	@Override
	public void info(String s) {
		logger.info(header+s);
	}

	@Override
	public void warn(String s) {
		logger.warn(header+s);
	}

	@Override
	public void debug(String s) {
		logger.debug(header+s);
	}

	@Override
	public void err(String s) {
		logger.err(header+s);
	}

}
