package hlmng.dao;

import java.util.List;


public interface Dao {
	public abstract boolean addElement(Object model);
	public abstract boolean deleteElement(String idS);
	public abstract boolean updateElement(Object model, String idS);
	public abstract Object getElement(String idS);
	public abstract List<Object> listElements();
	public abstract Dao getInstance();
	
	
}
	


