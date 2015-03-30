package hlmng.dao;

import hlmng.resource.Log;

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
	private Properties loginDataForTest;
	private String jdbcUrlForTest;

	public <T> GenDao(Class<T> classTypeP){

		className=classTypeP.getSimpleName();
		System.out.println("GenDao creating for Class:"+className);
		try {
			classType = Class.forName("hlmng.model."+className);
		} catch (ClassNotFoundException e) {
			Log.addEntry(Level.SEVERE, "Missing class for dao creation. Class name: "+className);
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
	 * @param model
	 * @return returns -1 if not inserted, else the id of the freshly inserted row
	 */
	@SuppressWarnings("unchecked")
	public <T> int addElement(Object model) {
		T tModel=null;
		ResultSet rs=null;
        PreparedStatement ps = null;
        Connection dbConnection=null;
        int insertedID=-1;
		try {
			tModel = (T) Class.forName("hlmng.model."+classType.getSimpleName()).cast(model);
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement(addElement,Statement.RETURN_GENERATED_KEYS);
			ps = DB.setAllFieldsOfPS(ps, classType, tModel ,null);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			insertedID = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			tryToClose(rs, ps, dbConnection);
		}
		if(insertedID!=-1){
			Log.addEntry(Level.INFO,className+" Element add ("+tModel+") ID="+insertedID,model);			
		}else{
			Log.addEntry(Level.WARNING,className+" Element wasn't added due to malformed input ("+tModel+") ID="+insertedID,model);			
		}
		return insertedID;
	}
	
	/**
	 * @param id
	 * @return True if element was deleted, false if not
	 */
	public boolean deleteElement(int id) {
        PreparedStatement ps = null;
        int rs=0;
        Connection dbConnection=null;
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement(removeElement);
			ps=DB.setIdFieldOfPS(ps,id);
	        rs = ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Element couldn't be deleted. "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			tryToClose( ps, dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Element delete ("+id+")="+rs);
		return (rs==1);		
	}
	
	
	/**
	 * @param model
	 * @param id
	 * @return True if element was updated, false if not
	 */
	@SuppressWarnings("unchecked")
	public <T> boolean updateElement(Object model, int id) {
		T tModel=null;
		PreparedStatement ps = null;
		int rs=0;
        Connection dbConnection=null;
		try {
			dbConnection = getDBConnection();
			tModel = (T) Class.forName("hlmng.model."+classType.getSimpleName()).cast(model);
			ps = dbConnection.prepareStatement(updateElement);
			ps = DB.setAllFieldsOfPS(ps, classType, tModel,id);
			rs = ps.executeUpdate();
		}catch (Exception e) {	
			Log.addEntry(Level.WARNING,"Element couldn't be updated. "+e.getMessage(),model);
			e.printStackTrace();
		}
		finally{
			tryToClose( ps, dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Element update ("+tModel+")="+rs,model);
		return (rs==1);
	}
	
	/**
	 * Gets (the first) element from said class where ID = @param
	 * @param idS
	 * @return
	 */
	public <T> Object getElement(int id) {
		PreparedStatement ps = null;
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
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Element couldn't be returned (GET). "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			tryToClose( ps, dbConnection);
		}
		Log.addEntry(Level.INFO,className+" Element get ("+element+")");
		return element;
	}
	
	
	
	public <T> List<Object> listByFK(String fkName, int idFK){
        PreparedStatement ps = null;
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
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Elements couldn't be listed by FK. "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			tryToClose( ps, dbConnection);
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
			Log.addEntry(Level.WARNING,"Elements couldn't be listed. "+e.getMessage());
			e.printStackTrace();
		}
		finally{
		}
		Log.addEntry(Level.INFO,className+" Element list ("+elemList.hashCode()+"["+elemList.size()+"])");
		return elemList;
	}


	protected Connection getDBConnection() throws SQLException, NamingException, ClassNotFoundException {
		if(isTest()){
			// This is used to connect in a JUnit Test as we don't have access to the context.xml etc.
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(jdbcUrlForTest,loginDataForTest);
		}else{
			return DB.getConnection();			
		}
	}


	/**
	 * If this is is called with isTest = True then then all Dao's will connect directly via provided login information
	 * to the DB instead of using connection pooling and the context.xml in web/inf.
	 * This due to the fact, that JUnit cannot use context.xml and other mechanisms.
	 * @param isTest
	 * @param loginData
	 * @param jdbcUrl
	 */
	public void setTest(boolean isTest, Properties loginData, String jdbcUrl) {
		this.isTest = isTest;
		this.loginDataForTest=loginData;
		this.jdbcUrlForTest=jdbcUrl;
	}
	
	public boolean isTest() {
		return isTest;
	}
	
	protected void tryToClose(ResultSet rs, PreparedStatement ps, Connection dbConnection) {
		if(rs!=null){
			try{rs.close();  }catch(Exception exception){Log.addEntry(Level.WARNING, "Resultset couldn't be closed ("+exception.getMessage());exception.printStackTrace();}			
		}
		tryToClose(ps, dbConnection);
		
	}
	protected void tryToClose(PreparedStatement ps, Connection dbConnection) {
		if(ps!=null){
			try{ps.close();}catch(Exception exception){Log.addEntry(Level.WARNING, "Preparedstatement couldn't be closed ("+exception.getMessage());exception.printStackTrace();}			
		}
		if(dbConnection!=null){
			try{dbConnection.close();}catch(Exception exception){Log.addEntry(Level.SEVERE, "Connection couldn't be closed ("+exception.getMessage());exception.printStackTrace();}					
		}
	}
}
