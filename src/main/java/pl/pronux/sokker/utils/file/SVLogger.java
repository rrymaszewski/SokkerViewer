package pl.pronux.sokker.utils.file;

import java.util.logging.Level;

import pl.pronux.sokker.utils.Logging;

public class SVLogger extends Logging {

	public SVLogger(Level level, String message) {
		super(level, message);
	}

	public SVLogger(Level level, String message, Throwable e) {
		super(level, message, e);
	}
	
	// public Logger getLogger() {
	// return logger;
	// }

	// public void addMessage(Level level, String message, Throwable e) {
	//
	// printMessage(message, e);
	// }
	//
//	 public void addMessage(Level level, String message) {
//	 logger.log(level, message);
//	 printMessage(message);
//	 }

//	public void showError() {
//		if (e == null) {
//			printMessage(message);
//		} else {
//			printMessage(message, e);
//		}
//	}
//
//	private void printMessage(final String message, final Throwable e) {
//		new BugReporter().createErrorMessage(message, e);
//	}
//
//	private void printMessage(final String message) {
//		new BugReporter().createErrorMessage(message);
//	}

}
