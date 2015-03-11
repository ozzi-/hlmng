package hlmng.dao;

import java.util.List;


public interface Dao {
	public void addElement(Object model);
	public boolean deleteElement(String id);
	public void updateElement(Object model);
	public Object getElement(String idS);
	public List<Object> listElements();
	public Dao getInstance();
}
