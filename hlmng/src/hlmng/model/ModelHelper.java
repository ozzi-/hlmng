package hlmng.model;

import java.lang.reflect.Field;

public class ModelHelper {
	/**
	 * Checks if two objects are from the same class and have the same field values.
	 * If anything is different false is returned.
	 */
	public static <T> boolean Compare(T a, T b){
		if(a==null || b==null){
			return false;
		}
		if(!(a.getClass().getSimpleName().equals(b.getClass().getSimpleName()))){
			return false;
		}
		Class<? extends Object> clsA = a.getClass();
		Class<? extends Object> clsB = b.getClass();
		Field[] methodsA = clsA.getDeclaredFields();
		Field[] methodsB = clsB.getDeclaredFields();
		if(methodsA.length!=methodsB.length){
			return false;
		}
		for (Field fieldA : methodsA) {
			fieldA.setAccessible(true);
			for(Field fieldB : methodsB){
				fieldB.setAccessible(true);
				if(fieldA.getName().equals(fieldB.getName())){
					try {
						String fieldValueA = fieldA.get(a).toString();
						String fieldValueB = fieldB.get(b).toString();
						if(!fieldValueA.equals(fieldValueB)){
							return false;
						}
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Get all fields and their values into a string to analyze the object
	 * @param obj
	 * @return A string
	 */
	public static <T> String valuestoString(T obj){
		StringBuilder strB = new StringBuilder();
		Class<? extends Object> clsObj = obj.getClass();
		strB.append("\nStart: ");
		strB.append(clsObj.getName());
		strB.append("\n");
		Field[] methods = clsObj.getDeclaredFields();
		for (Field field : methods) {
			field.setAccessible(true);
			try {
				strB.append(field.getName());
				strB.append("=");
				strB.append(field.get(obj));
				strB.append("\n");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace(); 
			}
		}
		strB.append("End\n");
		return strB.toString();
	}

	
	/**
	 * Create a better, generic hashcode method then the default one. 
	 * This one always creates the same hash for a object with the same conent.
	 * @param obj
	 * @return
	 */
	public static <T> int HashCode(T obj){
		Class<? extends Object> clsObj = obj.getClass();
		Field[] methods = clsObj.getDeclaredFields();
		int hash=0;
	    int[] primes = new int[2];
	    primes[0]=17;
	    primes[1]=31;
	    int i=0;
		for (Field field : methods) {
			field.setAccessible(true);
			try {
				hash = hash * primes[(++i%2)] + (field.get(obj)==null?"":field.get(obj)).toString().hashCode();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace(); 
			}
		}
		return hash;
	}

}
