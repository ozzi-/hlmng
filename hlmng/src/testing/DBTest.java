package testing;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.junit.Test;

import db.DB;

public class DBTest {

	@Test
    public void testGetConnectionHLMNG() throws SQLException, NamingException{
		Connection dbConnection = DB.getConnection();
        assertNotNull(dbConnection);
    }
}
