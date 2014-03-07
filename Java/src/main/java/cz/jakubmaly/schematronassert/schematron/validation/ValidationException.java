package cz.jakubmaly.schematronassert.schematron.validation;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean vritableStackTrace) {
		super(message, cause, enableSuppression, vritableStackTrace);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}
}
