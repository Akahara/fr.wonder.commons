package fr.wonder.commons.loggers;

import java.io.IOException;
import java.io.OutputStream;

public class LogStream extends OutputStream {

	private final Logger logger;
	private final int level;
	
	private final StringBuffer buffer = new StringBuffer();
	
	public LogStream(Logger logger, int level) {
		this.logger = logger;
		this.level = level;
	}

	@Override
	public void write(int b) throws IOException {
		if(b == '\n')
			flush();
		else
			buffer.append((char) b);
	}
	
	@Override
	public void flush() {
		logger.log(buffer.toString(), level);
		buffer.setLength(0);
	}
	
}
