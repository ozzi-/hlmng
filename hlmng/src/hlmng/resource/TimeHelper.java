package hlmng.resource;

import java.sql.Time;
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
	
	public static class TimePart {
	    int hours = 0;
	    int minutes = 0;
	    int seconds = 0;
	    
	    public static TimePart parse(String in) {
	        if (in != null) {
	            String[] arr = in.split(":");
	            TimePart tp = new TimePart();
	            tp.hours = ((arr.length >= 1) ? Integer.parseInt(arr[0]) : 0);
	            tp.minutes = ((arr.length >= 2) ? Integer.parseInt(arr[1]) : 0);
	            tp.seconds = ((arr.length >= 3) ? Integer.parseInt(arr[2]) : 0);
	            return tp;
	        }
	        return null;
	    }
	    
	    public TimePart sub(TimePart tp) {
	    	this.seconds -= tp.seconds;
	        int of = 0;
	        while (this.seconds < 0) {
	            of++;
	            this.seconds += 60;
	        }
	        this.minutes -= tp.minutes + of;
	        of = 0;
	        while (this.minutes < 0) {
	            of++; 
	            this.minutes += 60;
	        }
	        this.hours -= tp.hours + of;
	        if(this.hours < 0) {
	            throw new IllegalStateException("Can't go lower than 00:00:00 - no negative time supported");
	        }
	        return this;
		}

	    public TimePart add(TimePart tp) {
	        this.seconds += tp.seconds;
	        int of = 0;
	        while (this.seconds >= 60) {
	            of++;
	            this.seconds -= 60;
	        }
	        this.minutes += tp.minutes + of;
	        of = 0;
	        while (this.minutes >= 60) {
	            of++;
	            this.minutes -= 60;
	        }
	        this.hours += tp.hours + of;
	        return this;
	    }
	    /**
	     * @return 1 if >, -1 if <, 0 if ==
	     */
	    public int compareTo(TimePart other){
			if(this.hours>other.hours){
				return 1;
			}
			if(this.hours<other.hours){
				return -1;
			}
			if(this.hours==other.hours){
				if(this.minutes>other.minutes){
					return 1;
				}
				if(this.minutes<other.minutes){
					return -1;
				}
				if(this.hours==other.hours){
					if(this.seconds>other.seconds){
						return 1;
					}
					if(this.seconds<other.seconds){
						return -1;
					}
					if(this.seconds==other.seconds){
						return 0;
					}
				}
			}
			return 0;
	    }

	    @Override
	    public String toString() {
	        return String.format("%02d:%02d:%02d", hours, minutes,seconds);
	    }

		
	}
}
