package fr.wonder.commons.exceptions;

public class RecursionThresholdExceededException extends Error {

	private static final long serialVersionUID = 8194923040530331164L;

	public RecursionThresholdExceededException() {
        super();
    }

    public RecursionThresholdExceededException(String message) {
        super(message);
    }

    public RecursionThresholdExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecursionThresholdExceededException(Throwable cause) {
        super(cause);
    }

    protected RecursionThresholdExceededException(
				String message,
				Throwable cause,
                boolean enableSuppression,
                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
