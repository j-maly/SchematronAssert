package cz.jakubmaly.schematronassert.schematron.validation;

public class InvalidSchemaException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidSchemaException() {
		super();
	}

	public InvalidSchemaException(String message, Throwable cause, boolean enableSuppression, boolean vritableStackTrace) {
		super(message, cause, enableSuppression, vritableStackTrace);
	}

	public InvalidSchemaException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSchemaException(String message) {
		super(message);
	}

	public InvalidSchemaException(Throwable cause) {
		super(cause);
	}
}
