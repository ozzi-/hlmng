package hlmng.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {
	
	private static final DateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
	private static final DateFormat formatterTimeMillisecs = new SimpleDateFormat("HH:mm:ss.SSS");
	
	
	/**
	 * @return yyyy-MM-dd
	 */
	public static String getCurrentDate(){
		Date dateTime = new Date(System.currentTimeMillis()); 
		return formatterDate.format(dateTime);
	}
	/**
	 * @return yyyy-MM-dd HH-mm-ss
	 */
	public static String getCurrentDateTime(){
		Date dateTime = new Date(System.currentTimeMillis()); 
		return formatterDateTime.format(dateTime);
	}
	/**
	 * @return HH-mm-ss
	 */
	public static String getCurrentTime(){
		Date dateTime = new Date(System.currentTimeMillis()); 
		return formatterTime.format(dateTime);
	}
	
	/**
	 * @return HH-mm-ss.sss
	 */
	public static String getCurrentTimeMillisecs(){
		Date dateTime = new Date(System.currentTimeMillis()); 
		return formatterTimeMillisecs.format(dateTime);
	}
	
}
