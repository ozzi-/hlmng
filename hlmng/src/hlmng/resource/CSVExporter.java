package hlmng.resource;

import hlmng.model.ModelHelper;

import java.util.List;

public class CSVExporter {
	private String csv;
	public CSVExporter() {
	    csv="";
	}
	/**
	 * Adds all field names
	 * @param obj
	 */
	public <T> void addHeader(T obj){
		csv+=ModelHelper.objToCSV(obj,true)+"\n";
	}
	public void addValue(String value,boolean newline){
		csv+=(value+(newline?";\n":";"));
	}
	/**
	 * Adds all field values
	 * @param obj
	 */ 
	public <T> void addLine(T obj){
		csv+=ModelHelper.objToCSV(obj,false)+"\n";
	}
	/**
	 * Adds field values of all objects
	 * @param list
	 */
	public void addList(List<Object> list) {
		if(list!=null && list.size()!=0){
			addHeader(list.get(0));
			for (Object object : list) {
				addLine(object);
			}			
		}
	}
	public String toString(){
		return csv;
	}
}
