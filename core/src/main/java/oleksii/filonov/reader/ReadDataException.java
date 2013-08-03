package oleksii.filonov.reader;

public class ReadDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ReadDataException(final Throwable cause) {
		super(cause);
	}

	public ReadDataException(final String message) {
		super(message);
	}

	public ReadDataException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
