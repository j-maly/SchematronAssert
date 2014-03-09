package cz.jakubmaly.schematronassert;

public class SchemaBuilderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SchemaBuilderException() {
		super();
	}

	public SchemaBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SchemaBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public SchemaBuilderException(String message) {
		super(message);
	}

	public SchemaBuilderException(Throwable cause) {
		super(cause);
	}
}
