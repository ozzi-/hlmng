package settings;



public class HLMNGSettings {
	
	static boolean production=false;
	

	/**
	 * Lifetime of a action in millisecs
	 */
	public static final int actionGraceTime=5000;
	/**
	 * How many actions are allowed in the grace time
	 */
	public static final int maxActionsAllowed=5;
	//TODO add to documentation ^ ^ 

	
	/**
	 * QR Codes
	 */
	public static final String qrFileRootDir = (production?"/home/student/qr/":"/home/student/qr/");
	public static final int qrCodeWidth=500;
	public static final int qrCodeHeight=500;
	

	/**
	 * Media 
	 */
	public static final String mediaFileRootDir = (production?"/home/student/media/":"/home/ozzi/media/");
	public static final double maxMediaImageSizeMB = 3.0;

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
