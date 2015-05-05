package settings;



public class HLMNGSettings {
	
	static boolean production=false;
	
	public static final String restAppPath="https://localhost:8443/hlmng/rest";
	public static final int secretStrengthInBit = 130;
	
	/**
	 * Data base config
	 */
	public static final String jdbcPath="jdbc:mysql://127.0.0.1/hlmng";
	public static final String jdbcUser="user";
	public static final String jdbcPassword="pw12";
	
	/**
	 * Rest API URL config
	 * Example: hostname:port/rest/pub
	 */
	public static final String pubURL="/pub";
	public static final String admURL="/adm";
	
	
	/**
	 * Cache Control max-age in Sekunden
	 */
	public static final int cacheTime=1800;


	/**
	 * Lifetime of a action in millisecs
	 */
	public static final int actionGraceTime=5000;
	/**
	 * How many actions are allowed in the grace time
	 */
	public static final int maxActionsAllowed=5;

	
	/**
	 * QR Codes
	 */
	public static final String qrFileRootDir = (production?"/home/student/qr/":"/home/ozzi/qr/");
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
