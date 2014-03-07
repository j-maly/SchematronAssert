package cz.jakubmaly.schematronassert.svrl.serialization;

public class SvrlDeserializationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SvrlDeserializationException() {
		super();
	}

	public SvrlDeserializationException(String message, Throwable cause, boolean enableSuppression, boolean vritableStackTrace) {
		super(message, cause, enableSuppression, vritableStackTrace);
	}

	public SvrlDeserializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SvrlDeserializationException(String message) {
		super(message);
	}

	public SvrlDeserializationException(Throwable cause) {
		super(cause);
	}
}