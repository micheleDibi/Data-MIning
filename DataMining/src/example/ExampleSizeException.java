package example;

public class ExampleSizeException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ExampleSizeException() { }

	public ExampleSizeException(String msg) {
		super(msg);
	}
	
}
