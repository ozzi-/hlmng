package hlmng.dao;

import hlmng.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;


public enum UserDao implements Dao {
	instance;


	private static final String tablename="user";
	
    private static final String addElement = "insert into "+tablename+" (name, deviceID, regID) values (?, ?, ?);";
    private static final String removeElement = "delete from "+tablename+" where userID = ?;";
    private static final String listElements = "select * from "+tablename+";";
    private static final String getElement = "select * from "+tablename+" where userID = ?;";
    private static final String updateElement = "upadte "+tablename+" set name = ? , deviceID = ? , regID = ? where userID = ?;";
   
	
    
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
	        ps.setString(1,user.getName());
	        ps.setString(2,user.getDeviceID());
	        ps.setString(3,user.getRegID());
	        rs = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
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
		return (rs==1);
		
	}

	@Override
	public boolean updateElement(Object element) {
		User user = (User) element;
		 PreparedStatement ps;
	        int rs=0;
			try {
				ps = dbConnection.prepareStatement(updateElement);
		        ps.setString(1,user.getName());
		        ps.setString(2,user.getDeviceID());
		        ps.setString(3,user.getRegID());
		        ps.setInt(4,user.getUserID());
		        rs = ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
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
			ps.setInt(1,id);
	        rs = ps.executeQuery();
	        if (rs.isBeforeFirst() ) {     
	        	rs.next();
	        	user=getUserFromRS(rs);
	        } 
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
				userList.add(getUserFromRS(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public Dao getInstance() {
		return instance;
	}

	
	private User getUserFromRS(ResultSet rs){
		int userID = -1;
		String name = "<error>";
		String deviceID = "<error>";
		String regID = "<error>";
		try {
			userID = rs.getInt("userID");
			name = rs.getString("name");
			deviceID = rs.getString("deviceID");
			regID = rs.getString("regID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new User(userID, name, deviceID,regID);
	}
	
}
