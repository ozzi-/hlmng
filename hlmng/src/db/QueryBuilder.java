package db;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import settings.HLMNGSettings;
import log.Log;

/**
 * This class dynamically builds SQL Prepared Queries.
 * There are several types of statements available, see the enum opType.
 * <b>The mapping is done via the name.</b> This means that the class name and fields have to be the same as in the DB.
 */
public class QueryBuilder {
	

	/**
	 * list			= select * from table
	 * listLimit	= select * from table limit {x} 
	 * get			= select * from table where tableID = ?
	 * delete		= delete * from table where tableID = ? 
	 * add			= insert into table (fieldName1, fieldName2 ...) values ( ? , ? , ...) 
	 * update		= update table set fieldName1=?, fieldName2=?, ... where tableID = ? 
	 */
	public enum opType {
		list,listLimit , get, delete, add, addid, update
	}

	
	/**
	 * Builds a map for every "FK" (foreign key) containing field.
	 * The map contains a simple select * from query with a 'where' clause for said FK.
	 * Map<FKName,Query> -> select * from tableName where tableName.*FK* = ?
	 * 
	 * @param className
	 */
	public static Map<String, String> buildFKQuery(String className, String classPath) {
		Map<String, String> map = new HashMap<String, String>();
		String tableName = className.toLowerCase();
		String tableID = tableName + "ID";
		Class<?> cls;
		try {
			cls = Class.forName(classPath+"." + className);
			Field[] methods = cls.getDeclaredFields();
			for (Field field : methods) {
				if (field.getName().contains("FK")) {
					map.put(field.getName(), "SELECT * FROM " + tableName
							+ " WHERE " + field.getName() + " = ?;");
					map.put(field.getName()+"_limited", "SELECT * FROM " + tableName
							+ " WHERE " + field.getName() + " = ?  ORDER BY "+tableID+" DESC LIMIT "+HLMNGSettings.selectLimit+";");
				}
			}
		} catch (ClassNotFoundException e) {
			Log.addEntry(Level.SEVERE, "Missing model while building fk query! Model name: "+className);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * Builds a SQL Prepared Query
	 * 
	 * @param The class name to be used, be sure that its the same name as declared in the DB.
	 * @param type of the desired statement
	 * @return
	 */
	public static String buildQuery(String className, String classPath, opType type) {
		String query = null;
		String tableName = className.toLowerCase();
		String tableID = tableName + "ID";
		Class<?> cls;
		try {
			cls = Class.forName(classPath+"."+className);
			Field[] methods = cls.getDeclaredFields();
			String classID = className.toLowerCase() + "ID";
			switch (type) {
				case list:
					query = "SELECT * FROM " + tableName + ";";
					break;
				case listLimit:
					query = "SELECT * FROM " + tableName + " ORDER BY "
							+tableID+" DESC LIMIT "+HLMNGSettings.selectLimit+";";
					break;
				case add:
					query = "INSERT INTO " + tableName + " ";
					query += buildFieldsString(methods, classID, false,false);
					query += buildValuesString(methods,false);
					break;
				case addid:
					query = "INSERT INTO " + tableName + " ";
					query += buildFieldsString(methods, classID, false,true);
					query += buildValuesString(methods,true);
					break;
				case delete:
					query = "DELETE FROM " + tableName + " WHERE " + tableID
							+ " = ?;";
					break;
				case get:
					query = "SELECT * FROM " + tableName + " WHERE " + tableID
							+ " = ?;";
					break;
				case update:
					query = "UPDATE " + tableName + " SET ";
					query += buildFieldsString(methods, classID, true,false);
					query += " WHERE " + tableID + " = ? ;";
					break;
				default:
					throw new IllegalArgumentException("Unkown query type");
			}
		} catch (ClassNotFoundException e) {
			Log.addEntry(Level.SEVERE, "Missing model while building query! Model name: "+className);
			e.printStackTrace();
		}
		return query;
	}

	/**
	 * Creates something like "( fieldName1, fieldName2, ...)" or if boolean 'set'
	 * is true "fieldname1=?, fieldName2=?, ..."
	 * 
	 * @param methods
	 * @param the Name of the ID field of the DB / ID field in the model
	 * @param true for update statement, false for insert
	 * @return the desired partial query string
	 */
	private static String buildFieldsString(Field[] methods, String classID, boolean set, boolean idset) {
		int i = 0;
		String fieldsString = (set) ? "" : "( ";
		for (Field field : methods) {
			if ((!classID.toLowerCase().equals(field.getName().toLowerCase())|| idset) && !(DB.getDontMapFields().contains(field.getName()))) {
				if (i != 0) {
					fieldsString += " , " + field.getName()
							+ ((set) ? "=?" : "");
				} else {
					fieldsString += field.getName() + ((set) ? "=?" : "");
				}
				i++;
			}
		}
		fieldsString += (set) ? "" : " )";
		return fieldsString;
	}

	/**
	 * Creates something like "values ( ? , ? , ? )" , assuming there are 3
	 * methods in the array param
	 * 
	 * @param methods
	 * @return
	 */
	private static String buildValuesString(Field[] methods, boolean idset) {
		int i = 0;
		String valuesString = " values (";
		int dontMap = countDontMapFields(methods)-((idset)?1:0);
		for (int j = 0; j < methods.length - dontMap; j++) {
			if (i != 0) {
				valuesString += " , ?";
			} else {
				valuesString += " ? ";
			}
			i++;
		}
		valuesString += ");";
		return valuesString;
	}
	
	private static int countDontMapFields(Field[] methods) {
		int dontMap=1; // The first one is the ID and doesn't have to be mapped
		for (Field field : methods) {
			if(DB.getDontMapFields().contains(field.getName())){ // will be injected later
				dontMap++;
			}
		}
		return dontMap;
	}

}
