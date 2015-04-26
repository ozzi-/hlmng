package testing;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.NamingException;

import org.junit.Test;

import settings.HLMNGSettings;

public class DBTest {
	
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	
	private static final Properties loginData= new Properties();
	private Connection connection=null;
	

	@Test
    public void testGetConnectionHLMNG() throws SQLException, NamingException, ClassNotFoundException{
			Class.forName(dbClassName);
		    loginData.put("user",HLMNGSettings.jdbcUser);
		    loginData.put("password",HLMNGSettings.jdbcPassword);
			connection = DriverManager.getConnection(HLMNGSettings.jdbcPath,loginData);
			assertNotNull(connection);
    }
}
