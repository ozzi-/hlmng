package testing;

import static org.junit.Assert.*;
import hlmng.model.UserActionLimiter;

import org.junit.Test;

import settings.HLMNGSettings;

public class UserActionLimiterTest {
	
	@Test
	public void testMultipleActions(){
		boolean exceededTest1=false;
		boolean exceededTest2=false;
		for (int i = 0; i < HLMNGSettings.maxActionsAllowed; i++) {
			exceededTest1 = UserActionLimiter.actionsExceeded("test1");
			exceededTest2 = UserActionLimiter.actionsExceeded("test2");
		}
		assertFalse(exceededTest1);
		assertFalse(exceededTest2);
	}
	

	@Test
	public void testActionsExceeded(){
		boolean exceeded=false;
		for (int i = 0; i <= HLMNGSettings.maxActionsAllowed; i++) {
			exceeded = UserActionLimiter.actionsExceeded("test");		
		}
		assertTrue(exceeded);
	}
	
	@Test
	public void testActionsReset() throws InterruptedException{
		boolean exceeded=false;
		for (int i = 0; i <= HLMNGSettings.maxActionsAllowed; i++) {
			exceeded = UserActionLimiter.actionsExceeded("test");
            Thread.sleep((HLMNGSettings.actionGraceTime/HLMNGSettings.maxActionsAllowed)+10);
		}
		assertFalse(exceeded);
	}
}
