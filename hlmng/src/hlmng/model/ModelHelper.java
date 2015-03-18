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
}
