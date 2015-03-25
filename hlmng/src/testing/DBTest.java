package testing;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.NamingException;

import org.junit.Test;

public class DBTest {
	
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String dbRoot = "jdbc:mysql://127.0.0.1/";
	private static final Properties loginData= new Properties();
	private Connection connection=null;
	private static final String dbName="hlmng";
	

	@Test
    public void testGetConnectionHLMNG() throws SQLException, NamingException, ClassNotFoundException{
			Class.forName(dbClassName);
		    loginData.put("user","user");
		    loginData.put("password","pw12");
			connection = DriverManager.getConnection(dbRoot+dbName,loginData);
			assertNotNull(connection);
    }
}
