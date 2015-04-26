package testing;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import settings.HLMNGSettings;
import db.QueryBuilder;
import db.QueryBuilder.opType;

public class QueryBuilderTest {

	@Test
	public void testQueryBuilder(){
		String deleteTestClassB = QueryBuilder.buildQuery("TestClass","testing",opType.delete);
		String deleteTestClass = "DELETE FROM testclass WHERE testclassID = ?;";
		assertTrue(deleteTestClass.equals(deleteTestClassB));
		
		String listTestClassB = QueryBuilder.buildQuery("TestClass","testing",opType.list);
		String listTestClass = "SELECT * FROM testclass;";
		assertTrue(listTestClass.equals(listTestClassB));
		
		String getTestClassB = QueryBuilder.buildQuery("TestClass","testing",opType.get);
		String getTestClass = "SELECT * FROM testclass WHERE testclassID = ?;";
		assertTrue(getTestClass.equals(getTestClassB));
		
		String insertTestClassB = QueryBuilder.buildQuery("TestClass","testing",opType.add);
		String insertTestClass = "INSERT INTO testclass ( fieldInt , fieldString , fieldOtherIDFK , fieldOtherOtherIDFK ) values ( ?  , ? , ? , ?);";
		assertTrue(insertTestClass.equals(insertTestClassB));
	
		String updateTestClassB = QueryBuilder.buildQuery("TestClass","testing",opType.update);
		String updateTestClass = "UPDATE testclass SET fieldInt=? , fieldString=? , fieldOtherIDFK=? , fieldOtherOtherIDFK=? WHERE testclassID = ? ;";
		assertTrue(updateTestClass.equals(updateTestClassB));
	
		String listLimitTestClassB = QueryBuilder.buildQuery("TestClass","testing",opType.listLimit);
		String listLimitTestClass = "SELECT * FROM testclass ORDER BY testclassID DESC LIMIT "+HLMNGSettings.selectLimit+";";
		assertTrue(listLimitTestClass.equals(listLimitTestClassB));
	}
	
	@Test
	public void testFKQueryBuilder(){
		Map<String, String> listByFKTestClassB = QueryBuilder.buildFKQuery("TestClass", "testing");
		String fieldOtherIDFKList = listByFKTestClassB.get("fieldOtherIDFK");
		String fieldOtherOtherIDFKList= listByFKTestClassB.get("fieldOtherOtherIDFK");

		assertTrue(fieldOtherIDFKList.equals("SELECT * FROM testclass WHERE fieldOtherIDFK = ?;"));
		assertTrue(fieldOtherOtherIDFKList.equals("SELECT * FROM testclass WHERE fieldOtherOtherIDFK = ?;"));
		
	}
	
	
}



