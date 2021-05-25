package fr.wonder.commons.exceptions;

public class UnreachableException extends Error {

	private static final long serialVersionUID = 333946789414690579L;
	
	public UnreachableException() {
        super();
    }

    public UnreachableException(String message) {
        super(message);
    }

    public UnreachableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnreachableException(Throwable cause) {
        super(cause);
    }

    protected UnreachableException(
				String message,
				Throwable cause,
                boolean enableSuppression,
                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
