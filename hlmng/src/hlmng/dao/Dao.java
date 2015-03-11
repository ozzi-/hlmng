package hlmng.dao;

import java.util.List;


public interface Dao {
	public boolean addElement(Object model);
	public boolean deleteElement(String id);
	public boolean updateElement(Object model);
	public Object getElement(String idS);
	public List<Object> listElements();
	public Dao getInstance();
}
