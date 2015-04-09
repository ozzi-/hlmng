package hlmng;

public class FileSettings {
	
	static boolean production=false;
	/**
	 * QR Codes
	 */
	public static final String qrFileRootDir = (production?"/home/student/qr/":"/home/student/qr/");

	/**
	 * Media 
	 */
	public static final String mediaFileRootDir = (production?"/home/student/media/":"/home/ozzi/media/");
	

	/**
	 * Logging
	 * Set sysErr true to redirect log output to system.error.
	 * Set sysErr to false to write log output into a (numbered) logfile.
	 */
	public static final boolean logSysErr = true;
	public static final String logFileRootDir = (production?"/home/student/logs/":"/home/ozzi/logs/");
	
	/**
	 * GCM
	 */
	public static final String gcmURL = "https://android.googleapis.com/gcm/send"; 
	public static final String apiKey = "AIzaSyD62VEqAESTccbNguLB5RI5OwfbbiOp4B8";
	
	/**
	 * Querybuilder
	 */
	public static final int selectLimit = 15;

}
