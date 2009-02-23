package pl.pronux.sokker.exceptions;

public class BadArgumentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5839754217378599930L;

	public BadArgumentException(String msg) {
		super(msg);
	}

	public BadArgumentException() {
		super();
	}
}
