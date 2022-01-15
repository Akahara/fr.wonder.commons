package fr.wonder.commons.loggers;

import java.io.IOException;
import java.io.OutputStream;

public class LogStream extends OutputStream {

	private final Logger logger;
	private final int level;
	
	private final byte[] buffer = new byte[4096];
	private int length;
	
	public LogStream(Logger logger, int level) {
		this.logger = logger;
		this.level = level;
	}

	@Override
	public void write(int b) throws IOException {
		if(b == '\n') {
			if(length == 0)
				logger.log("", level);
			else
				flush();
		} else {
			if(length == buffer.length)
				flush();
			buffer[length++] = (byte) b;
		}
	}
	
	@Override
	public void flush() {
		if(length == 0)
			return;
		logger.log(new String(buffer, 0, length), level);
		length = 0;
	}
	
}
