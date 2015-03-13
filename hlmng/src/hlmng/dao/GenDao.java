package hlmng.dao;

import hlmng.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import db.DB;
import db.QueryBuilder;


public class GenDao {
	
    private String addElement; 
    private String removeElement;
    private String listElements;
    private String getElement; 
    private String updateElement;
    private String className;
	private Class<?> classType;
	private static DB dbHandle = new DB("hlmng");
	private static Connection dbConnection = dbHandle.getConnection();
	

	public <T> GenDao(Class<T> classTypeP){
		System.out.println("GenDao creating for Class:"+classTypeP.getSimpleName());
		className=classTypeP.getSimpleName();
		try {
			classType = Class.forName("hlmng.model."+className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	    addElement = QueryBuilder.BuildQuery(className,QueryBuilder.opType.add);
	    removeElement = QueryBuilder.BuildQuery(className,QueryBuilder.opType.delete);
	    listElements = QueryBuilder.BuildQuery(className,QueryBuilder.opType.list);
	    getElement = QueryBuilder.BuildQuery(className,QueryBuilder.opType.get);
	    updateElement = QueryBuilder.BuildQuery(className,QueryBuilder.opType.update);
	}
	
	@SuppressWarnings("unchecked")
	public <T> boolean addElement(Object model) {
		T tModel=null;
        PreparedStatement ps;
        int rs=0;
		try {
			tModel = (T) Class.forName("hlmng.model."+classType.getSimpleName()).cast(model);
			ps = dbConnection.prepareStatement(addElement);
			ps = DB.setAllFieldsOfPS(ps, classType, tModel ,null);
			rs = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		Log.addEntry(Level.INFO,className+" Element add ("+tModel+")="+rs);
		return (rs==1);
	}
	
	
	public boolean deleteElement(String idS) {
		int id = Integer.parseInt(idS);
        PreparedStatement ps;
        int rs=0;
		try {
			ps = dbConnection.prepareStatement(removeElement);
			ps=DB.setIdFieldOfPS(ps,id);
	        rs = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		Log.addEntry(Level.INFO,className+" Element delete ("+id+")="+rs);
		return (rs==1);
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> boolean updateElement(Object model, String idS) {
		T tModel=null;
		int id = Integer.parseInt(idS);
		PreparedStatement ps;
		int rs=0;
		try {
			tModel = (T) Class.forName("hlmng.model."+classType.getSimpleName()).cast(model);
			ps = dbConnection.prepareStatement(updateElement);
			ps = DB.setAllFieldsOfPS(ps, classType, tModel,id);
			rs = ps.executeUpdate();
		}catch (Exception e) {	
			e.printStackTrace();
			return false;
		}
		Log.addEntry(Level.INFO,className+" Element update ("+tModel+")="+rs);
		return (rs==1);
	}
	
	public <T> Object getElement(String idS) {
		int id = Integer.parseInt(idS);
		PreparedStatement ps;
        ResultSet rs;
        Object element=null;
		try {
			ps = dbConnection.prepareStatement(getElement);
			ps=DB.setIdFieldOfPS(ps,id);
	        rs = ps.executeQuery();
	        if (rs.isBeforeFirst() ) {     
	        	rs.next();
				element=DB.getObjectFromRS(rs,classType);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,className+" Element get ("+element+")");
		return element;
	}
	
	public <T> List<Object> listElements() {
        PreparedStatement ps;
        ResultSet rs;
		List<Object> userList = new ArrayList<Object>();
		try {
			ps = dbConnection.prepareStatement(listElements);
	        rs = ps.executeQuery();
			while (rs.next()) {
				userList.add(DB.getObjectFromRS(rs,classType));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,className+" Element list ("+userList.hashCode()+"["+userList.size()+"])");
		return userList;
	}

}
