package hlmng.dao;

import hlmng.Log;
import hlmng.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import db.DB;


public enum EventDao implements Dao{
	instance;
	
    private static final String addElement = QueryBuilder.BuildQuery("Event",QueryBuilder.opType.add);
    private static final String removeElement = QueryBuilder.BuildQuery("Event",QueryBuilder.opType.delete);
    private static final String listElements = QueryBuilder.BuildQuery("Event",QueryBuilder.opType.list);
    private static final String getElement = QueryBuilder.BuildQuery("Event",QueryBuilder.opType.get);
    private static final String updateElement = QueryBuilder.BuildQuery("Event",QueryBuilder.opType.update);
    
    
    
	private DB dbHandle;
	private Connection dbConnection;

	EventDao() {
		dbHandle = new DB("hlmng");
		dbConnection = dbHandle.getConnection();
	}
    
    
	@Override
	public boolean addElement(Object model) {
		Event event = (Event) model;
        PreparedStatement ps;
        int rs=0;
		try {			
			ps = dbConnection.prepareStatement(addElement);
	        ps.setString(1,event.getName());
	        ps.setString(2,event.getDescription());
	        ps.setString(3, event.getStart());
	        ps.setString(4, event.getEnd());
	        rs = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		Log.addEntry(Level.INFO,"Event Element add ("+event+")="+rs);
		return (rs==1);
	}

	@Override
	public boolean deleteElement(String idS) {
		int id = Integer.parseInt(idS);
        PreparedStatement ps;
        int rs=0;
		try {
			ps = dbConnection.prepareStatement(removeElement);
	        ps.setInt(1,id);
	        rs = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		Log.addEntry(Level.INFO,"Event Element delete ("+id+")="+rs);
		return (rs==1);
	}

	@Override
	public boolean updateElement(Object model, String idS) {
		int id = Integer.parseInt(idS);
		Event event = (Event) model;
		PreparedStatement ps;
		int rs=0;
		try {
			ps = dbConnection.prepareStatement(updateElement);
	        ps.setString(1,event.getName());
	        ps.setString(2,event.getDescription());
	        ps.setString(3,event.getStart());
	        ps.setString(4,event.getEnd());
			ps.setInt(4,id);
			rs = ps.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		Log.addEntry(Level.INFO,"User Element update ("+event+")="+rs);
		return (rs==1);
	}

	@Override
	public Object getElement(String idS) {
		int id = Integer.parseInt(idS);
		PreparedStatement ps;
        ResultSet rs;
        Event event=null;
		try {
			ps = dbConnection.prepareStatement(getElement);
			ps.setInt(1,id);
	        rs = ps.executeQuery();
	        if (rs.isBeforeFirst() ) {     
	        	rs.next();
				event=DB.getObjectFromRS(rs,Event.class);
	        } 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,"Event Element get ("+event+")");
		return event;
	}

	@Override
	public List<Object> listElements() {
        PreparedStatement ps;
        ResultSet rs;
		List<Object> eventList = new ArrayList<Object>();
		try {
			ps = dbConnection.prepareStatement(listElements);
	        rs = ps.executeQuery();
			while (rs.next()) {
				eventList.add(DB.getObjectFromRS(rs,Event.class));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,"Event Element list ("+eventList.hashCode()+"["+eventList.size()+"])");
		return eventList;
	}

	public Dao getInstance() {
		return instance;
	}


}
