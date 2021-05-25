package fr.wonder.commons.exceptions;

public class GenerationException extends Exception {

	private static final long serialVersionUID = 1164717709743046894L;

	public GenerationException() {
	}

	public GenerationException(String message) {
		super(message);
	}

	public GenerationException(Throwable cause) {
		super(cause);
	}

	public GenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
