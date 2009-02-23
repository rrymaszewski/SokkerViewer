package pl.pronux.sokker.exceptions;

public class SVException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SVException(String msg) {
		super(msg);
	}

	public SVException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public SVException() {
		super();
	}
	
}
