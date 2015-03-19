package hlmng.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * User actions are used to limit actions on the API such as POST of images or other content.
 * This allows us to hinder excessive spamming.
 */
public class UserActionLimiter {
	private static HashMap<String,ArrayList<Long>> userActionList=new HashMap<String,ArrayList<Long>>();
	/**
	 * Lifetime of a action in millisecs
	 */
	private static int actionGraceTime=5000;
	/**
	 * How many actions are allowed in the grace time
	 */
	private static int maxActionsAllowed=2;

	
	private static ArrayList<Long> addAction(String username){
		long curMil=System.currentTimeMillis();
		ArrayList<Long> actionList = userActionList.get(username);
		if(actionList==null){
			actionList=new ArrayList<Long>();
			actionList.add(curMil);
			userActionList.put(username,actionList);
		}else{
			actionList.add(curMil);
		}
		return actionList;
	}
	/**
	 * Checks if the user has made too many actions.
	 * @param username
	 * @return true if the user made too many actions at said moment, false if he is allowed to do more actions
	 */
	public static boolean actionsExceeded(String username){
		ArrayList<Long> actionList = addAction(username);
		return (getActionsOnRecord(actionList)>maxActionsAllowed);
	}
	/**
	 * Returns the number of actions found in the list. Does a cleanup first so only the active ones are counted.
	 * @param actionList
	 * @return
	 */
	private static int getActionsOnRecord(ArrayList<Long> actionList){
		cleanUpActions(actionList);
		return actionList.size();
	}
	
	/**
	 * Removes all actions of the provided action list if their grace period has elapsed
	 * @param actionList
	 */
	private static void cleanUpActions(ArrayList<Long> actionList) {
		long curMil=System.currentTimeMillis();
		for (Iterator<Long> actionIterator = actionList.iterator(); actionIterator.hasNext();) {
		    Long action = actionIterator.next();
		    if(action+actionGraceTime<curMil){
		    	actionIterator.remove();		    	
		    }
		}
	}
	


}
