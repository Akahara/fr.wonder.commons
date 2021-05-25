package fr.wonder.commons.exceptions;

public class AssertionException extends Exception {

	private static final long serialVersionUID = -7082280797240090857L;
	
	public AssertionException() {
        super();
    }

    public AssertionException(String message) {
        super(message);
    }

    public AssertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertionException(Throwable cause) {
        super(cause);
    }

    protected AssertionException(
				String message,
				Throwable cause,
                boolean enableSuppression,
                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
