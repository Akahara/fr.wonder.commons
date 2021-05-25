package fr.wonder.commons.exceptions;

import java.io.IOException;

public class SerializationException extends IOException {

	private static final long serialVersionUID = 4999660071831398840L;

	public SerializationException() {
		super();
	}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializationException(Throwable cause) {
		super(cause);
	}

}
