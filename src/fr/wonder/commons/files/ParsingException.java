package fr.wonder.commons.files;

public class ParsingException extends Exception {

	private static final long serialVersionUID = -7675001893153524631L;

	public ParsingException() {
	}

	public ParsingException(String message) {
		super(message);
	}

	public ParsingException(Throwable cause) {
		super(cause);
	}

	public ParsingException(String message, Throwable cause) {
		super(message, cause);
	}

}
