package db;

/**
 * This class is here for entries we don't wan't to be escaped by the ESAPI.
 * If the check fails they have to be escaped.
 */
public class SafeValues {
	private int length;
	private String regex;
	
	public SafeValues(int length, String regex) {
		this.length=length;
		this.regex=regex;	
	}
	public boolean isSafeString(Object value){
		if(value instanceof String) {
			String valueS = (String) value;
			if(valueS.length()==length){
				if(valueS.matches(regex)){
					return true;
				}
			}			
		}
		return false;
	}
}
