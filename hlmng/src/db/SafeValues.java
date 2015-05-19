package db;

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
