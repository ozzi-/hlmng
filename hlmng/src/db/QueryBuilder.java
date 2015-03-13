package db;

import java.lang.reflect.Field;

public class QueryBuilder {

	public static String BuildQuery(String className, opType type){
		System.out.println("Building Query for "+className+" for type "+type.toString());
		String query=null;
		String tableName = className.toLowerCase();
		String tableID=tableName+"ID";
		Class<?> cls;
		try {
			cls = Class.forName("hlmng.model."+className);
			Field[] methods = cls.getDeclaredFields();
			String classID = className.toLowerCase()+"ID";		
			if (type==opType.list){
				query="SELECT * FROM "+tableName+";";
			}
			if (type==opType.add){
				query="INSERT INTO "+tableName+" ";
				query += buildFieldsString(methods,classID,false);
				query += buildValuesString(methods);
			}
			if (type==opType.delete){
				query="DELETE FROM "+tableName+" WHERE "+tableID+" = ?;";
			}
			if (type==opType.get){
				query="SELECT * FROM "+tableName+" WHERE "+tableID+" = ?;";
			}
			if(type==opType.update){
				query="UPDATE "+tableName+" SET ";
				query+=buildFieldsString(methods, classID,true);
				query+=" WHERE "+tableID+" = ? ;";
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return query;
	}

	private static String buildFieldsString(Field[] methods,String classID,boolean set) {
		int i=0;
		String fieldsString=(set)?"":"( ";
		for (Field field : methods) {
			if(!classID.equals(field.getName())){
				if(i!=0){
					fieldsString+=" , "+field.getName()+((set)?"=?":""); 													
				}else{
					fieldsString+=field.getName()+((set)?"=?":"");
				}
				i++;
			}
		}
		fieldsString+=(set)?"":" )";
		return fieldsString;
	}

	private static String buildValuesString(Field[] methods) {
		int i=0;
		String valuesString=" values (";
		for (int j=0; j<methods.length-1 ; j++) {
			if(i!=0){
				valuesString+=" , ?"; 													
			}else{
				valuesString+=" ? ";
			}
			i++;
		}
		valuesString+=");";
		return valuesString;
	}
	
	public enum opType{
		list,get,delete,add,update
	}

}
