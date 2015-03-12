package hlmng;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Example usage: 		Log.addEntry(Level.INFO, "USER RESSOURCE " + id + " GET");
 *
 */
public class Log {
	
	private static Logger logger=null;
	private static boolean debug=true;
	
	protected static void startLogger() {
		logger = Logger.getLogger("hlmng");
		FileHandler fh = null;
		try {
			fh = new FileHandler("MyLogFile.log", true);
		} catch (SecurityException e) {
			System.err
					.println("security exception whilst trying to create logger");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("I/O exception whilst trying to create logger");
			e.printStackTrace();
		}
		logger.addHandler(fh);
		logger.setLevel(Level.ALL);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
		addEntry(Level.INFO,"Successfully started logger");
	}
	
	
	public static void addEntry(Level level, String message){
			if (logger == null) {
				startLogger();
			}
			if(debug){
				System.err.println(getTime()+" | "+level+" | "+message);
			}else{
				logger.log(level, message);				
			}
	}

	
	public static String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
