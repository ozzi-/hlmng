package log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import settings.HLMNGSettings;

/**
 * Example usage: 
 * Log.addEntry(Level.INFO, "MESSAGE", causingObject); 
 */
public class Log {

	private static Logger logger;

	protected static void startLogger() { 
		logger = Logger.getLogger("hlmng");
		FileHandler fh = null;
		try {  
			fh = new FileHandler(HLMNGSettings.logFileRootDir+"/hlmng%u.log", false);
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			Formatter formatter = new FormatterS();
			fh.setFormatter(formatter);
			addEntry(Level.INFO, "Successfully started logger");
		} catch (SecurityException e) { 
			System.err.println("security exception whilst trying to create logger");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("I/O exception whilst trying to create logger");
			e.printStackTrace();
		}
	
	}

	
	public static void addEntry(Level level, String message) {
		startLoggerIfNeeded();
		logMessage(level, message);
	}


	private static void logMessage(Level level, String message) {
		if (HLMNGSettings.logSysErr) {
			System.err.println(getTime() + " | " + level + " | " + message);
		} else {
			logger.log(level, message);
		}
	}
	
	@SuppressWarnings("unused")
	private static void startLoggerIfNeeded() {
		if (logger == null && !HLMNGSettings.logSysErr) {
			startLogger();
		}
	}

	public static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
