package hlmng.dao;

import hlmng.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;

public enum UserDao implements Dao {
	instance;

	private DB dbHandle;
	private String tablename;

	UserDao() {
		dbHandle = new DB("hlmng");
		tablename = "user";
	}

	@Override
	public void addElement(Object model) {
		User user = (User) model;
		dbHandle.doUpdateGetResult("insert into " + tablename
				+ " (name, deviceID) values ('" + user.getName() + "' , '"
				+ user.getDeviceID() + "')");
	}

	@Override
	public boolean deleteElement(String idS) {
		int id = Integer.parseInt(idS);
		int delRet = dbHandle.doUpdateGetResult("delete from " + tablename
				+ " where userID=" + id);
		return ((delRet == 1 ? true : false));
	}

	@Override
	public void updateElement(Object model) {
		User user = (User) model;
		dbHandle.doUpdateGetResult("update " + tablename + " set name='"
				+ user.getName() + "' , deviceID='" + user.getDeviceID()
				+ " where userID=" + user.getUserID());
	}

	@Override
	public Object getElement(String idS) {
		int id = Integer.parseInt(idS);
		ResultSet rs = dbHandle.doQueryGetResult("select * from " + tablename
				+ " where userID=" + id);
		int userID = -1;
		String name = "<error>";
		String deviceID = "<error>";
		try {
			if (!rs.isBeforeFirst()) {
				return null;
			}
			rs.next();
			userID = rs.getInt("userID");
			name = rs.getString("name");
			deviceID = rs.getString("deviceID");
		} catch (SQLException e) {
			System.err
					.println("Error when trying to build album from SQL Resultset");
			e.printStackTrace();
		}
		return new User(userID, name, deviceID);
	}

	@Override
	public List<Object> listElements() {
		ResultSet rs = dbHandle.doQueryGetResult("select * from " + tablename);
		int userID = 0;
		String name = "";
		String deviceID = "";
		List<Object> userList = new ArrayList<Object>();
		try {
			while (rs.next()) {
				userID = rs.getInt("userID");
				name = rs.getString("name");
				deviceID = rs.getString("deviceID");
				userList.add(new User(userID, name, deviceID));
			}
		} catch (SQLException e) {
			System.err
					.println("Error when trying to build User list from SQL Resultset");
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public Dao getInstance() {
		return instance;
	}

}
