package hlmng.dao;

import hlmng.model.ModelHelper;

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

import log.Log;
import db.DB;
import db.QueryBuilder;


public class GenDao {
	
    private String addElement;
    private String addIdElement; 
    private String removeElement;
    private String listElements;
    private String listElementsLimit;
    private String getElement; 
    private String updateElement;
    private String className;
    private Map<?, ?> fkElement;
	private Class<?> classType;
	private boolean isTest=false;
	private Properties loginDataForTest;
	private String jdbcUrlForTest;
	private long lastUpdateTime=System.currentTimeMillis();


	public <T> GenDao(Class<T> classTypeP){

		className=classTypeP.getSimpleName();
		
		try {
			classType = Class.forName("hlmng.model."+className);
		} catch (ClassNotFoundException e) {
			Log.addEntry(Level.SEVERE, "Missing class for dao creation. Class name: "+className);
			e.printStackTrace();
		}
		
		String classPath = "hlmng.model";
	    addElement = QueryBuilder.buildQuery(className,classPath,QueryBuilder.opType.add);
	    addIdElement = QueryBuilder.buildQuery(className,classPath,QueryBuilder.opType.addid);
	    removeElement = QueryBuilder.buildQuery(className,classPath,QueryBuilder.opType.delete);
	    listElements = QueryBuilder.buildQuery(className,classPath,QueryBuilder.opType.list);
	    listElementsLimit = QueryBuilder.buildQuery(className,classPath,QueryBuilder.opType.listLimit);
	    getElement = QueryBuilder.buildQuery(className,classPath,QueryBuilder.opType.get);
	    updateElement = QueryBuilder.buildQuery(className,classPath,QueryBuilder.opType.update);
	    fkElement = QueryBuilder.buildFKQuery(className,classPath);
	}
	
	public <T> int addElement(Object model) {
		return addElementInternal(model,addElement,false);
	}
	
	public <T> int addIDElement(Object model) {
		return addElementInternal(model,addIdElement,true);
	}

	/**
	 * @param model
	 * @param addElement2 
	 * @return returns -1 if not inserted, else the id of the freshly inserted row
	 */
	@SuppressWarnings("unchecked")
	public <T> int addElementInternal(Object model, String statement,boolean idset) {
        int insertedID=-1;
        T tModel;
		try(Connection dbConnection = getDBConnection()){
			tModel = (T) Class.forName("hlmng.model."+classType.getSimpleName()).cast(model);
			try(PreparedStatement ps = dbConnection.prepareStatement(statement,Statement.RETURN_GENERATED_KEYS)){
				try(PreparedStatement psF = DB.setAllFieldsOfPS(ps, classType, tModel ,null,idset)){
					if(psF!=null){
						psF.executeUpdate();
						if(!idset){
							try(ResultSet rs = ps.getGeneratedKeys()){						
								rs.next();
								insertedID = rs.getInt(1);				
							}
						}
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insertedID!=-1){
			Log.addEntry(Level.INFO,className+" Element add "+ModelHelper.valuestoString(model)+" inserted ID = "+insertedID);			
		}else{
			Log.addEntry(Level.WARNING,className+" Element wasn't added due to malformed input "+ModelHelper.valuestoString(model)+" inserted ID = "+insertedID);			
		}

		setLastUpdatedTime(insertedID!=-1);

		return insertedID;
	}
	
	/**
	 * @param id
	 * @return True if element was deleted, false if not
	 */
	public boolean deleteElement(int id) {
        int rs=0;
		try (Connection dbConnection = getDBConnection()){
			try(PreparedStatement ps = dbConnection.prepareStatement(removeElement)){
				try(PreparedStatement psF = DB.setIdFieldOfPS(ps,id)){
					rs = psF.executeUpdate();									
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Element couldn't be deleted. "+e.getMessage());
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,className+" Element delete ("+id+")="+rs);
		
		setLastUpdatedTime(rs==1);
		
		return (rs==1);		
	}
	
	
	/**
	 * @param model
	 * @param id
	 * @return True if element was updated, false if not
	 */
	@SuppressWarnings("unchecked")
	public <T> boolean updateElement(Object model, int id) {
		int rs=0;
		T tModel = null;
		try (Connection dbConnection = getDBConnection()){
			tModel = (T) Class.forName("hlmng.model."+classType.getSimpleName()).cast(model);
			try(PreparedStatement ps = dbConnection.prepareStatement(updateElement)){
				try(PreparedStatement psF = DB.setAllFieldsOfPS(ps, classType, tModel,id,false)){
					rs = psF.executeUpdate();							
				}
			}
		}catch (Exception e) {	
			Log.addEntry(Level.WARNING,"Element couldn't be updated. "+e.getMessage()+". "+ModelHelper.valuestoString(model));
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,className+" Element update ("+tModel+")="+rs+". "+ModelHelper.valuestoString(model));
	
		setLastUpdatedTime(rs==1);
	
		return (rs==1);
	}
	
	/**
	 * Gets (the first) element from said class where ID = @param
	 * @param idS
	 * @return
	 */
	public <T> Object getElement(int id) {
		Object element = null;
		try (Connection	dbConnection = getDBConnection();){
			try(PreparedStatement ps = dbConnection.prepareStatement(getElement)){
				try(PreparedStatement psF=DB.setIdFieldOfPS(ps,id)){
					try(ResultSet rs = psF.executeQuery()){
						if (rs.isBeforeFirst() ) {     
							rs.next();
							element=DB.getObjectFromRS(rs,classType);
						} 															
					}
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Element couldn't be returned (GET). "+e.getMessage());
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,className+" Element get ("+element+")");
		return element;
	}
	
	
	/**
	 * 
	 * @return A list of all elements from the specified class/model
	 */
	public <T> List<Object> listElements(boolean limit) {
		return doListElements((limit)?listElementsLimit:listElements);
	}
	
	public <T> List<Object> listByFKLimited(String fkName, int idFK){
		return listByFK(fkName+"_limited",idFK);
	}

	public <T> List<Object> listByFK(String fkName, int idFK){
		List<Object> elemList = new ArrayList<Object>();
		try (Connection dbConnection = getDBConnection()){
			try(PreparedStatement ps = dbConnection.prepareStatement(fkElement.get(fkName).toString())){
				PreparedStatement psF= DB.setIdFieldOfPS(ps,idFK);				
				try(ResultSet rs = psF.executeQuery()){
					while (rs.next()) {
						elemList.add(DB.getObjectFromRS(rs,classType));
					}					
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Elements couldn't be listed by FK. "+e.getMessage());
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,className+" Element list ("+elemList.hashCode()+"["+elemList.size()+"])");
		return elemList;
	}
	
	private <T> List<Object> doListElements(String listElementsQuery){
		List<Object> elemList = new ArrayList<Object>();
		try(Connection dbConnection = getDBConnection()){
			try (PreparedStatement ps = dbConnection.prepareStatement(listElementsQuery);){
				try(ResultSet rs = ps.executeQuery();){
					while (rs.next()) {
						elemList.add(DB.getObjectFromRS(rs,classType));
					}
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Elements couldn't be listed. "+e.getMessage());
			e.printStackTrace();
		} 
		Log.addEntry(Level.INFO,className+" Element list ("+elemList.hashCode()+". limited="+(listElementsQuery.equals(listElementsLimit))+" ["+elemList.size()+"]) ");
		return elemList;
	}


	protected Connection getDBConnection() throws SQLException, NamingException, ClassNotFoundException {
		if(isTest()){
			// This is used to connect in a JUnit Test as we don't have access to the context.xml etc. from there
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(jdbcUrlForTest,loginDataForTest);
		}else{
			return DB.getConnection();			
		}
	}

	/**
	 * This is used to enable the client the ability to determine if he has to reload the json data or not.
	 * I wish i could have asked the DB directly, but 
	 * InnoDB has a bug getting update_time ( https://bugs.mysql.com/bug.php?id=14374 ) and calculating a checksum is way to costly..
	 */
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	
	private void setLastUpdatedTime(boolean changedSomething){
		if(changedSomething){
			this.lastUpdateTime=System.currentTimeMillis();
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
}
