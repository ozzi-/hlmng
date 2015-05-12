package settings;

public class HTTPCodes {
	public static final int ok=200;
	public static final int badRequest=400;
	public static final int unauthorized=401;
	public static final int forbidden=403;
	public static final int notFound=404;
	public static final int requestEntityTooLarge=413;
	public static final int unsupportedMediaType=415;
	public static final int unprocessableEntity=422; // or "exists already"
	public static final int locked=423;
	public static final int failedDependency=424;
	public static final int tooManyRequests=429;
	public static final int internalServerError=500;
}
