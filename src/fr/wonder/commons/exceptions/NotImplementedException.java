package fr.wonder.commons.exceptions;

/**
 * Used where some code has not yet been implemented but shall be (soon?).
 * 
 * <p>
 * Do not use this exception as a marker for an unreachable section but rather a
 * section that will be written later.
 * 
 * <p>
 * This exception is intended as an additional "todo" marker which can be
 * searched for through code. If not implicit the user should add comments to
 * why they throw an unimplemented exception in the first place.
 */
public class NotImplementedException extends Error {

	private static final long serialVersionUID = 4422736032832254876L;

	public NotImplementedException() {
		super();
	}

	public NotImplementedException(String message) {
		super(message);
	}

	public NotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotImplementedException(Throwable cause) {
		super(cause);
	}

	protected NotImplementedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
