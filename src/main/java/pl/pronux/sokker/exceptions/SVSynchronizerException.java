package pl.pronux.sokker.exceptions;

public class SVSynchronizerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466427641109536175L;

	public SVSynchronizerException() {
		super();
	}
	
	public SVSynchronizerException(String msg) {
		super(msg);
	}
	
	public SVSynchronizerException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
