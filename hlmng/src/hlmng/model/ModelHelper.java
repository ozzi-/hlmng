package hlmng.model;

import java.lang.reflect.Field;

public class ModelHelper {
	/**
	 * Checks if two objects are from the same class and have the same field values.
	 * If anything is different false is returned.
	 */
	public static <T> boolean Compare(T a, T b){
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
