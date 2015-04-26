package testing;

import static org.junit.Assert.*;
import hlmng.model.Event;
import hlmng.model.ModelHelper;

import org.junit.Before;
import org.junit.Test;

import com.owlike.genson.Genson;

public class ModelHelperTest {
	private Genson genson;
	private Event e1;
	private Event e2;
	private Event e3;
	private Event e4;
	
	@Before
	public void init() {
		genson = new Genson();
		e1= genson.deserialize("{\"endDate\":\"2015-01-01\",\"eventID\":34,\"name\":\"eventname\",\"startDate\":\"2015-01-01\",\"description\":\"description\"}",Event.class);
		e2= genson.deserialize("{\"eventID\":34,\"name\":\"eventname\",\"startDate\":\"2015-01-01\",\"description\":\"description\",\"endDate\":\"2015-01-01\"}",Event.class);
		e3= genson.deserialize("{\"eventID\":35,\"name\":\"eventname\",\"startDate\":\"2015-01-01\",\"description\":\"description\",\"endDate\":\"2015-01-01\"}",Event.class);
		e4= genson.deserialize("{\"name\":\"eventname\",\"startDate\":\"2015-01-01\",\"description\":\"description\",\"endDate\":\"2015-01-01\"}",Event.class);
	}
	@Test
	public void CompareOrderingTest(){
		assertTrue(ModelHelper.compare(e1,e2));
		assertTrue(ModelHelper.compare(e2,e1));
	}
	@Test
	public void CompareSame(){
		assertTrue(ModelHelper.compare(e1,e1));
	}
	@Test
	public void CompareOtherFieldValue(){
		assertFalse(ModelHelper.compare(e1,e3));
		assertFalse(ModelHelper.compare(e3,e1));
	}
	@Test
	public void CompareMissingField(){
		assertFalse(ModelHelper.compare(e1,e4));
		assertFalse(ModelHelper.compare(e4,e1));
	}
	@Test
	public void HashSameObject(){
		int h1 = ModelHelper.hashCode(e1);
		int h1_2 = ModelHelper.hashCode(e1);
		assertTrue(h1==h1_2);
	}
	@Test
	public void HashSameContent(){
		int h1 = ModelHelper.hashCode(e1);
		int h2 = ModelHelper.hashCode(e2);
		assertTrue(h1==h2);
	}
	@Test
	public void HashDifferentContent(){
		int h1 = ModelHelper.hashCode(e1);
		int h3 = ModelHelper.hashCode(e3);
		assertFalse(h1==h3);
	}	
}
