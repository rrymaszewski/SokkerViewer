package pl.pronux.sokker.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

	private static boolean initialize = false;
	private static FileHandler fileHandler;

	public static void info(Class<?> cls, String msg) {
		log(cls, Level.INFO, msg);
	}

	public static void warning(Class<?> cls, String msg) {
		log(cls, Level.WARNING, msg);
	}

	public static void error(Class<?> cls, String msg) {
		log(cls, Level.SEVERE, msg);
	}

	public static void info(Class<?> cls, String msg, Throwable t) {
		log(cls, Level.INFO, msg, t);
	}

	public static void warning(Class<?> cls, String msg, Throwable t) {
		log(cls, Level.WARNING, msg, t);
	}

	public static void error(Class<?> cls, String msg, Throwable t) {
		log(cls, Level.SEVERE, msg, t);
	}

	private static void log(Class<?> cls, Level level, String msg, Throwable t) {
		if (!initialize) {
			init();
			initialize = true;
		}
		Logger logger = Logger.getLogger(cls.getCanonicalName());
		logger.log(level, msg, t);
	}

	private static void log(Class<?> cls, Level level, String msg) {
		log(cls, level, msg, null);
	}

	private static void init() {
		Logger logger = Logger.getLogger("");
		// create a new handler to write to a named file
		try {
			fileHandler = new FileHandler("sokker.log", true); //$NON-NLS-1$
			fileHandler.setFormatter(new SimpleFormatter());
		} catch (IOException ioe) {
			logger.warning("Could not create a file..."); //$NON-NLS-1$
		}

		// add the handlers to the logger
		logger.addHandler(fileHandler);
	}

	public static void close() {
		Logger.getLogger("").removeHandler(fileHandler);
		fileHandler.close();
	}
}
