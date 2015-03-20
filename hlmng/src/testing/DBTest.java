package testing;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Test;

import db.DB;

public class DBTest {

	@Test
    public void testGetConnectionHLMNG() throws SQLException{
        DB dbConnection = new DB("hlmng");
        dbConnection.getConnection();
        assertNotNull(dbConnection.getConnection());
    }
}
