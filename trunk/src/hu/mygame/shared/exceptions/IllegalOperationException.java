package hu.mygame.shared.exceptions;

public class IllegalOperationException extends Exception {
	private static final long serialVersionUID = 1L;

	public IllegalOperationException() {
		super();
	}

	public IllegalOperationException(String s) {
		super(s);
	}
}
