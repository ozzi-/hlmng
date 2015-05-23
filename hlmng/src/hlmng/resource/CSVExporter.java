package hlmng.resource;

import hlmng.model.ModelHelper;

import java.util.List;

public class CSVExporter {
	private String csv;
	public CSVExporter() {
	    csv="";
	}
	public <T> void addHeader(T obj){
		csv+=ModelHelper.objToCSV(obj,true)+"\n";
	}
	public void addValue(String value,boolean newline){
		csv+=(value+(newline?";\n":";"));
	}
	public <T> void addLine(T obj){
		csv+=ModelHelper.objToCSV(obj,false)+"\n";
	}
	public void addList(List<Object> list) {
		addHeader(list.get(0));
		for (Object object : list) {
			addLine(object);
		}
	}
	public String toString(){
		return csv;
	}
}
