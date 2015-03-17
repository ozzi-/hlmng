package testing;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import db.DB;

public class DBTest {

	@Test
    public void testGetConnectionHLMNG(){
        DB dbConnection = new DB("hlmng");
        dbConnection.getConnection();
        assertNotNull(dbConnection.getConnection());
    }
}
