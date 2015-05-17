package testing;

import static org.junit.Assert.assertTrue;
import hlmng.resource.TimeHelper;
import hlmng.resource.TimeHelper.TimePart;

import org.junit.Test;

public class TimeTest {

	@Test
	public void testAddSub () throws java.text.ParseException {
		String t1 = "14:30:50";
		String t2 = "15:03:30";
		
		TimePart tp = new TimeHelper.TimePart();
		TimePart tp_t1 = TimeHelper.TimePart.parse(t1);
		TimePart tp_t2 = TimeHelper.TimePart.parse(t2);
		
		tp.add(tp_t2);
		tp.sub(tp_t1);
		assertTrue(tp.toString().equals("00:32:40"));
	} 
	
	@Test
	public void testAddAdd() throws java.text.ParseException {
		String t1 = "00:30:50";
		String t2 = "02:03:30";
		
		TimePart tp = new TimeHelper.TimePart();
		TimePart tp_t1 = TimeHelper.TimePart.parse(t1);
		TimePart tp_t2 = TimeHelper.TimePart.parse(t2);
		
		tp.add(tp_t2);
		tp.add(tp_t1);
		assertTrue(tp.toString().equals("02:34:20"));
	} 
	
	@Test
	public void testAddHigh () throws java.text.ParseException {
		String t1 = "12:30:50";
		String t2 = "12:32:30";
		
		TimePart tp = new TimeHelper.TimePart();
		TimePart tp_t1 = TimeHelper.TimePart.parse(t1);
		TimePart tp_t2 = TimeHelper.TimePart.parse(t2);
		
		tp.add(tp_t2);
		tp.add(tp_t1);
		assertTrue(tp.toString().equals("25:03:20"));
	} 
	
	@Test(expected=IllegalStateException.class)
	public void testSubHigh () throws java.text.ParseException {
		String t1 = "14:30:50";
		String t2 = "15:03:30";
		
		TimePart tp = new TimeHelper.TimePart();
		TimePart tp_t1 = TimeHelper.TimePart.parse(t1);
		TimePart tp_t2 = TimeHelper.TimePart.parse(t2);
		
		tp.add(tp_t1);
		tp.sub(tp_t2);
	} 
}
