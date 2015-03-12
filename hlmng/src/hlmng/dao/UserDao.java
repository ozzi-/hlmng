package hlmng.dao;

import hlmng.Log;
import hlmng.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import db.DB;


public enum UserDao implements Dao {
	instance;
	
	private static final String addElement = QueryBuilder.BuildQuery("User",QueryBuilder.opType.add);
    private static final String removeElement = QueryBuilder.BuildQuery("User",QueryBuilder.opType.delete);
    private static final String listElements = QueryBuilder.BuildQuery("User",QueryBuilder.opType.list);
    private static final String getElement = QueryBuilder.BuildQuery("User",QueryBuilder.opType.get);
    private static final String updateElement = QueryBuilder.BuildQuery("User",QueryBuilder.opType.update);
    

    
	private DB dbHandle;
	private Connection dbConnection;

	UserDao() {
		dbHandle = new DB("hlmng");
		dbConnection = dbHandle.getConnection();
	}

	@Override
	public boolean addElement(Object model) {
		User user = (User) model;
        PreparedStatement ps;
        int rs=0;
		try {
			ps = dbConnection.prepareStatement(addElement);
			ps = DB.setAllFieldsOfPS(ps, User.class, user,null);
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		Log.addEntry(Level.INFO,"User Element add ("+user+")="+rs);
		return (rs==1);
	}

	@Override
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
		Log.addEntry(Level.INFO,"User Element delete ("+id+")="+rs);
		return (rs==1);
		
	}

	@Override
	public boolean updateElement(Object element, String idS) {
		int id = Integer.parseInt(idS);
		User user = (User) element;
		PreparedStatement ps;
		int rs=0;
		try {
			ps = dbConnection.prepareStatement(updateElement);
			ps = DB.setAllFieldsOfPS(ps, User.class, user,id);
			rs = ps.executeUpdate();
		}catch (SQLException e) {	
			e.printStackTrace();
			return false;
		}
		Log.addEntry(Level.INFO,"User Element update ("+user+")="+rs);
		return (rs==1);
	}


	
	@Override
	public Object getElement(String idS) {
		int id = Integer.parseInt(idS);
		PreparedStatement ps;
        ResultSet rs;
        User user=null;
		try {
			ps = dbConnection.prepareStatement(getElement);
			ps=DB.setIdFieldOfPS(ps,id);
	        rs = ps.executeQuery();
	        if (rs.isBeforeFirst() ) {     
	        	rs.next();
				user=DB.getObjectFromRS(rs,User.class);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,"User Element get ("+user+")");
		return user;
	}

	@Override
	public List<Object> listElements() {
        PreparedStatement ps;
        ResultSet rs;
		List<Object> userList = new ArrayList<Object>();
		try {
			ps = dbConnection.prepareStatement(listElements);
	        rs = ps.executeQuery();
			while (rs.next()) {
				userList.add(DB.getObjectFromRS(rs,User.class));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,"User Element list ("+userList.hashCode()+"["+userList.size()+"])");
		return userList;
	}

	@Override
	public Dao getInstance() {
		return instance;
	}


}
