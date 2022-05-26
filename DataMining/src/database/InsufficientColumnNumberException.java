package database;

public class InsufficientColumnNumberException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InsufficientColumnNumberException(String msg) {
		super(msg);
	}
	
}
