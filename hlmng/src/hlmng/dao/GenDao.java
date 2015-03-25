package hlmng.dao;

import hlmng.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.naming.NamingException;

import db.DB;
import db.QueryBuilder;


public class GenDao {
	
    private String addElement; 
    private String removeElement;
    private String listElements;
    protected String getElement; 
    private String updateElement;
    private String className;
    private Map<?, ?> fkElement;
	private Class<?> classType;
	private boolean isTest=false;

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
	    fkElement = QueryBuilder.buildFKQuery(className);
	}
	
	/**
	 * 
	 * @param model
	 * @return returns -1 if not inserted, else id
	 */
	@SuppressWarnings("unchecked")
	public <T> int addElement(Object model) {
		T tModel=null;
        PreparedStatement ps;
        int insertedID=-1;
        Connection dbConnection=null;
		try {
			tModel = (T) Class.forName("hlmng.model."+classType.getSimpleName()).cast(model);
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement(addElement,Statement.RETURN_GENERATED_KEYS);
			ps = DB.setAllFieldsOfPS(ps, classType, tModel ,null);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			insertedID = rs.getInt(1);
			ps.close();
			DB.closeConnection(dbConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			closeDbConnection(dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Element add ("+tModel+") ID="+insertedID);
		return insertedID;
	}
	
	public boolean deleteElement(String idS) {
		int id = Integer.parseInt(idS);
        PreparedStatement ps;
        int rs=0;
        Connection dbConnection=null;
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement(removeElement);
			ps=DB.setIdFieldOfPS(ps,id);
	        rs = ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			closeDbConnection(dbConnection);
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
        Connection dbConnection=null;
		try {
			dbConnection = getDBConnection();
			tModel = (T) Class.forName("hlmng.model."+classType.getSimpleName()).cast(model);
			ps = dbConnection.prepareStatement(updateElement);
			ps = DB.setAllFieldsOfPS(ps, classType, tModel,id);
			rs = ps.executeUpdate();
			ps.close();
			DB.closeConnection(dbConnection);
		}catch (Exception e) {	
			e.printStackTrace();
		}
		finally{
			closeDbConnection(dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Element update ("+tModel+")="+rs);
		return (rs==1);
	}
	
	/**
	 * Gets (the first) element from said class where ID = @param
	 * @param idS
	 * @return
	 */
	public <T> Object getElement(String idS) {
		int id = Integer.parseInt(idS);
		PreparedStatement ps;
        ResultSet rs;
        Object element=null;
        Connection dbConnection=null;
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement(getElement);
			ps=DB.setIdFieldOfPS(ps,id);
	        rs = ps.executeQuery();
	        if (rs.isBeforeFirst() ) {     
	        	rs.next();
				element=DB.getObjectFromRS(rs,classType);
			} 
			ps.close();
			DB.closeConnection(dbConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			closeDbConnection(dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Element get ("+element+")");
		return element;
	}
	
	
	
	public <T> List<Object> listByFK(String fkName, String idFkS){
		int idFK = Integer.parseInt(idFkS);
        PreparedStatement ps;
        ResultSet rs;
        Connection dbConnection=null;
		List<Object> elemList = new ArrayList<Object>();
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement(fkElement.get(fkName).toString());
			ps=DB.setIdFieldOfPS(ps,idFK);
	        rs = ps.executeQuery();
			while (rs.next()) {
				elemList.add(DB.getObjectFromRS(rs,classType));
			}
			ps.close();
			DB.closeConnection(dbConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			closeDbConnection(dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Element list ("+elemList.hashCode()+"["+elemList.size()+"])");
		return elemList;
	}
	
	
	/**
	 * 
	 * @return A list of all elements from the specified class/model
	 */
	public <T> List<Object> listElements() {
		PreparedStatement ps;
		ResultSet rs;
        Connection dbConnection=null;
		List<Object> elemList = new ArrayList<Object>();
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement(listElements);
			rs = ps.executeQuery();
			while (rs.next()) {
				elemList.add(DB.getObjectFromRS(rs,classType));
			}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			closeDbConnection(dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Element list ("+elemList.hashCode()+"["+elemList.size()+"])");
		return elemList;
	}

	private void closeDbConnection(Connection dbConnection) {
		if(dbConnection!=null){
			DB.closeConnection(dbConnection);				
		}
	}

	/**
	 * @param id
	 * @return  @return A list of all elements from the specified class/model where the ID is equal to @param
	 */
	public <T> List<Object> getElements(String idS) {
		int id = Integer.parseInt(idS);
		PreparedStatement ps;
        ResultSet rs;
        Connection dbConnection=null;
		List<Object> elemList = new ArrayList<Object>();
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement(getElement);
			ps=DB.setIdFieldOfPS(ps,id);
	        rs = ps.executeQuery();
	        while(rs.next()){
				elemList.add(DB.getObjectFromRS(rs,classType));
			} 
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			closeDbConnection(dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Elements get list ("+elemList+")");
		return elemList;
	}

	private Connection getDBConnection() throws SQLException, NamingException, ClassNotFoundException {
		if(isTest()){
			// This is used to connect in a JUnit Test as we don't have access to the context.xml etc.
			Properties loginData= new Properties();
			Class.forName("com.mysql.jdbc.Driver");
		    loginData.put("user","user");
		    loginData.put("password","pw12");
			return DriverManager.getConnection("jdbc:mysql://127.0.0.1/"+"hlmng",loginData);

		}else{
			return DB.getConnection();			
		}
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}
}
