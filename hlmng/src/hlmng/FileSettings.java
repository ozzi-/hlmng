package hlmng;

public class FileSettings {
	
	/**
	 * QR Codes
	 */
	public static final String qrFileRootDir = "/home/ozzi/qr/";

	/**
	 * Media 
	 */
	//public static final String mediaFileRootDir = "/home/student/media/";
	public static final String mediaFileRootDir = "/home/ozzi/media/";
	

	/**
	 * Logging
	 * Set sysErr true to redirect log output to system.error.
	 * Set sysErr to false to write log output into a (numbered) logfile.
	 */
	public static final boolean logSysErr = true;
	public static final String logFileRootDir = "/home/ozzi/logs/";
	//public static final String logFileRootDir = "/home/student/logs/";
	
	/**
	 * GCM
	 */
	public static final String gcmURL = "https://android.googleapis.com/gcm/send"; 
	public static final String apiKey = "AIzaSyD62VEqAESTccbNguLB5RI5OwfbbiOp4B8";

}
