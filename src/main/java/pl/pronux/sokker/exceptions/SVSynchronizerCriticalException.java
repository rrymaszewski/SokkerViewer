package pl.pronux.sokker.exceptions;

public class SVSynchronizerCriticalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466427641109536175L;

	public SVSynchronizerCriticalException() {
		super();
	}
	
	public SVSynchronizerCriticalException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public SVSynchronizerCriticalException(String msg) {
		super(msg);
	}
}
