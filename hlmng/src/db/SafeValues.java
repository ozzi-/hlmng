package db;

import java.util.regex.Pattern;

/**
 * This class is here for entries we don't wan't to be escaped by the ESAPI.
 * If the check fails they have to be escaped.
 */
public class SafeValues {
	private int length;
	private Pattern regex;
	
	
	public SafeValues(int length, String regex) {
		this.length=length;
		// precompiling should give us a performance boost because we use the same regex's all the time
		this.regex=Pattern.compile(regex); 
	}
	public boolean isSafeString(Object value){
		if(value instanceof String) {
			String valueS = (String) value;
			if(valueS.length()==length){
				if(regex.matcher(valueS).matches()){
					return true;
				}
			}			
		}
		return false;
	}
}
