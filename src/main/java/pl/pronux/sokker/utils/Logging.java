package pl.pronux.sokker.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
	private static ConsoleHandler console = null;

	private static FileHandler file = null;

	final private static Logger logger = Logger.getLogger("MyLogger"); //$NON-NLS-1$

	static {
		// create a new handler to write to the console
		console = new ConsoleHandler();
		// create a new handler to write to a named file
		try {
			if (new File("sokker.log").exists()) { //$NON-NLS-1$
				file = new FileHandler("sokker.log", true); //$NON-NLS-1$
			} else {
				file = new FileHandler("sokker.log", false); //$NON-NLS-1$
			}
			file.setFormatter(new SimpleFormatter());
		} catch (IOException ioe) {
			logger.warning("Could not create a file..."); //$NON-NLS-1$
		}

		// add the handlers to the logger
		logger.addHandler(console);
		logger.addHandler(file);
	}

	public static void dispose() {
		logger.removeHandler(file);
		file.close();
	}

	protected Throwable e;

	protected String message;

	public Logging(Level level, String message) {
		logger.log(level, message);
		this.message = message;
	}

	public Logging(Level level, String message, Throwable e) {
		logger.log(level, message, e);
		this.e = e;
		this.message = message;
	}
	
	public Logging() {
	}

}
