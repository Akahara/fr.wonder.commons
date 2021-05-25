package fr.wonder.commons.exceptions;

public class CompilationException extends Error {

	private static final long serialVersionUID = -9186433415615913979L;
	
	public CompilationException() {
        super();
    }

    public CompilationException(String message) {
        super(message);
    }

    public CompilationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompilationException(Throwable cause) {
        super(cause);
    }

    protected CompilationException(
				String message,
				Throwable cause,
                boolean enableSuppression,
                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
