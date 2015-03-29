package testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
		assertTrue(ModelHelper.Compare(e1,e2));
		assertTrue(ModelHelper.Compare(e2,e1));
	}
	@Test
	public void CompareSame(){
		assertTrue(ModelHelper.Compare(e1,e1));
	}
	@Test
	public void CompareOtherFieldValue(){
		assertFalse(ModelHelper.Compare(e1,e3));
		assertFalse(ModelHelper.Compare(e3,e1));
	}
	@Test
	public void CompareMissingField(){
		assertFalse(ModelHelper.Compare(e1,e4));
		assertFalse(ModelHelper.Compare(e4,e1));
	}
}
