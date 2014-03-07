package cz.jakubmaly.schematronassert.schematron.serialization;

public class SchemaSerializationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SchemaSerializationException() {
		super();
	}

	public SchemaSerializationException(String message, Throwable cause, boolean enableSuppression, boolean vritableStackTrace) {
		super(message, cause, enableSuppression, vritableStackTrace);
	}

	public SchemaSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SchemaSerializationException(String message) {
		super(message);
	}

	public SchemaSerializationException(Throwable cause) {
		super(cause);
	}
}