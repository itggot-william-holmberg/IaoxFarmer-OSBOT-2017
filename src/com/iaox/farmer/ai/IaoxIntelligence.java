package com.iaox.farmer.ai;

import java.util.LinkedList;

import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.node.Node;
import com.iaox.farmer.node.methods.Players;

public class IaoxIntelligence implements Runnable {

	private AIFrame frame;
	private Thread t;
	private Script script;

	private static MiningAssignment miningAssignment;
	private IntelligentMining im;
	private int ticks;
	private int lastTickReset;

	private Node lastRollNode;
	private Players players;
	private int amountOfWorldChanges;

	public boolean scriptShouldRun = true;

	private LinkedList<RandomBreak> break_handler;
	private int current_session_start = 0;
	private long sleepTimeStartTime = 0;
	private int sleepTime = 0;
	private int lastMuleTrade;

	
	public void start(Script script) {
		this.script = script;
		this.players = new Players(script);

		frame = new AIFrame();
		frame.setVisible(true);

		im = new IntelligentMining(this.script);

		break_handler = new LinkedList<RandomBreak>();
		generateNewBreaks();

		if (t == null) {
			t = new Thread(this, "Iaox Intelligence Thread");
			t.start();
		}
	}
	
	@Override
	public void run() {
		while (IaoxAIO.shouldThreadRun && !t.isInterrupted() && t.isAlive()) {
			script.log("thread running");

			if (ticks - lastTickReset > IaoxAIO.random(180, 350)) {
				resetVariables();
			}

			if (script.client.isLoggedIn()) {

				if (IaoxAIO.CURRENT_TASK != null) {
					intelligentSkilling();
				}

				if (IaoxAIO.CURRENT_NODE != null && IaoxAIO.CURRENT_NODE.safeToInterrupt() && !IaoxAIO.shouldTrade) {
					randomBehaviour();
					intelligentBreaking();
					intelligentMuleDeposit();
				}
			} else {
				intelligentBreaking();
			}
			// ~~ 3600 per hour
			sleeps(1000);
			ticks++;
		}
	}
	

	/*
	 * Lets reset the variables, such as "amount of world changes", every third
	 * minute
	 */

	private void resetVariables() {
		frame.newMessage("Reset variables!");
		amountOfWorldChanges = amountOfWorldChanges - IaoxAIO.random(1, 2);
		lastTickReset = ticks;
	}
	
	
	private void intelligentSkilling() {
		switch (IaoxAIO.CURRENT_TASK.getAssignment()) {
		case MINING:
			intelligentMining();
			break;
		default:
			break;

		}
	}


	private void intelligentBreaking() {
		if (break_handler.isEmpty()) {
			generateNewBreaks();
		} else {
			RandomBreak nextBreak = break_handler.getFirst();
			if (nextBreak.getPlayTime() < (getCurrentPlayTime() / 60)) {
				/*
				 * if the break is successfully executed we have to remove it
				 * and then restart the session
				 */
				if (breakSleep(nextBreak.getBreakTime())) {
					break_handler.remove(nextBreak);
				}
			}
		}

	}
	
	private void intelligentMuleDeposit() {
		if(ticks-lastMuleTrade > 10000 && IaoxAIO.random(100) == 50){
			frame.newMessage("WE SHOULD MULE DEPOSIT");
			IaoxAIO.shouldTrade = true;
			lastMuleTrade = ticks;
		}
		
	}

	/*
	 * It is safe to interrupt our node when mining.. banking.. etc.. It is not
	 * safe to interrupt our node when we are in combat or webwalking etc.. If
	 * it is safe to interrupt, "roll the dice" and if we hit a specific number
	 * we have to do something answer a phone call, open the dorr, yell at mom,
	 * go to the toilet, etc.... various "afk" things that will make sure that
	 * the script is not doing the exact same thing during the whole task
	 * 
	 */
	private void randomBehaviour() {

		/*
		 * If we are currently banking && we havent done a random bank behaviour
		 * yet We not supposed to do the random behaviour more than once per
		 * bank node LastRollNode makes sure that we do not.
		 */

		if (IaoxAIO.CURRENT_NODE.toString().contains("Bank") && lastRollNode != IaoxAIO.CURRENT_NODE) {
			randomBankBehaviour();
			lastRollNode = IaoxAIO.CURRENT_NODE;
		} else {
			randomSkillingActionBehaviour();
			lastRollNode = IaoxAIO.CURRENT_NODE;
		}

	}

	/*
	 * This method will be executed, for instance, when we are mining. Some
	 * appropriate afk behaviors would be to :
	 * 
	 * Go to the toilet (1 time per hour?) Answer the phone (1 time per hour?)
	 * Go grab some food (1 time per hour?) Move your chair (1 time per hour?)
	 * Scratch your nose (3 times per hour?) Change youtube video (3 times per
	 * hour)
	 * 
	 * Etc...
	 */

	private void randomSkillingActionBehaviour() {
		int diceNumber = IaoxAIO.random(3600);

		if (diceNumber == 1336) {
			// Move your chair (1 time per hour?)
			scriptSleep(IaoxAIO.random(6500, 14500));
			return;
		}

		if (diceNumber == 1337) {
			// Go to the toilet (1 time per hour?)
			scriptSleep(IaoxAIO.random(45000, 150000));
			return;
		}

		if (diceNumber == 1338) {
			// Answer the phone (1 time per hour?)
			scriptSleep(IaoxAIO.random(10000, 25000));
			return;
		}

		if (diceNumber == 1339) {
			// Go grab some food (1 time per hour?)
			scriptSleep(IaoxAIO.random(25000, 90000));
			return;
		}

		if (diceNumber >= 1340 && diceNumber <= 1342) {
			// Scratch your nose (3 times per hour?)
			scriptSleep(IaoxAIO.random(3500, 8000));
			return;
		}

		if (diceNumber >= 1343 && diceNumber <= 1345) {
			// Change youtube video (3 times per hour)
			scriptSleep(IaoxAIO.random(8000, 21000));
			return;
		}

	}

	/*
	 * This method will be executed when we are standing in the bank. Some
	 * appropriate afk behaviour would be to go afk for a longer time etc...
	 * 
	 * .........IMPORTANT........... We should make sure that the player is
	 * selling his items and then transfering to a mule every once in a while,
	 * preferably every 3-6th hour. Everytime we are in a bank we should check
	 * if it has been more than 3 hours ( ~~ 10000 ticks) since last transfer.
	 * If it has been more than 10000 ticks we should pause the current task and
	 * go to the grand exchange to sell the items. When we have sold the items
	 * it is time to check if any mule is available. If no mule is available we
	 * should logout and wait until there is one available. Preferably would be
	 * to notice the bot owner that there is no mule available.
	 */

	private void randomBankBehaviour() {
		// frame.newMessage("New random bank behaviour");
	}

	private void intelligentMining() {

		/*
		 * If we already have an assignment, do nothing. In the future, maybe
		 * change assignment in the middle of the task For instance, if the
		 * current spot is "overcrowded" - change world or spot
		 */
		if (miningAssignment == null) {
			miningAssignment = im.getNewAssignment();
			frame.newMessage("Updated mining assignment");
		} else if (players.playersInArea(miningAssignment.getObjectArea()) > 1
				&& IaoxAIO.CURRENT_NODE.toString().equals("Mining")) {
			findBetterAssignment();
		}
	}

	private void findBetterAssignment() {
		MiningAssignment similarAssignment = getSimilarMiningAssignment();
		if (similarAssignment != null && players.playersInArea(similarAssignment.getObjectArea()) == 0) {
			script.log("There is a better area for this assignment! lets switch.");
			miningAssignment = similarAssignment;
		} else if (amountOfWorldChanges < 2) {
			changeWorld();
		} else {
			script.log("nothing we can do, we already changed world");
		}

	}

	private void changeWorld() {
		scriptShouldRun = false;
		if (script.getWorlds().isMembersWorld()) {
			int currentWorld = script.getWorlds().getCurrentWorld();
			script.getWorlds().hopToP2PWorld();
			new ConditionalSleep(6000, 12000) {
				@Override
				public boolean condition() throws InterruptedException {
					sleeps(600);
					return script.worlds.getCurrentWorld() != currentWorld;
				}
			}.sleep();
		} else {
			int currentWorld2 = script.getWorlds().getCurrentWorld();
			script.getWorlds().hop(getRandomF2PWorld());
			new ConditionalSleep(6000, 12000) {
				@Override
				public boolean condition() throws InterruptedException {
					sleeps(600);
					return script.worlds.getCurrentWorld() != currentWorld2;
				}
			}.sleep();
		}
		amountOfWorldChanges++;
		frame.newMessage("Changed world");
		scriptShouldRun = true;
	}

	private int getRandomF2PWorld() {
		return Data.AVAILABLE_F2P_WORLDS[IaoxAIO.random(Data.AVAILABLE_F2P_WORLDS.length)];
	}

	private MiningAssignment getSimilarMiningAssignment() {
		for (MiningAssignment ass : MiningAssignment.values()) {
			if (ass.getMiningArea().equals(miningAssignment.getMiningArea())
					&& ass.getObjectIDs().equals(miningAssignment.getObjectIDs()) && !ass.equals(miningAssignment)) {
				return ass;
			}
		}
		return null;
	}

	public static MiningAssignment getMiningAssignment() {
		return miningAssignment;
	}

	public void setMiningAssignment(MiningAssignment ass) {
		miningAssignment = ass;
	}


	private void generateNewBreaks() {
		int breaks = IaoxAIO.random(4, 7);
		for (int i = 0; i < breaks; i++) {
			/*
			 * Random breaks such as eating dinner etc
			 */
			break_handler.add(new RandomBreak(IaoxAIO.random(45, 75), IaoxAIO.random(5,25)));
		}

		/*
		 * "Sleepbreak"
		 */
		break_handler.add(new RandomBreak(IaoxAIO.random(30, 65), IaoxAIO.random(360, 540)));
	}

	private void sleeps(int i) {
		try {
			sleepTimeStartTime = System.currentTimeMillis();
			sleepTime = i;
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean breakSleep(int minutes) {
		scriptShouldRun = false;
		frame.newMessage("Lets sleep for: " + minutes + " minutes.");
		if (script.client.isLoggedIn()) {
			script.getLogoutTab().logOut();
			return false;
		} else {
			sleeps(minutes * 60000);

			current_session_start = ticks;
			scriptShouldRun = true;
			return true;
		}
	}
	
	private void scriptSleep(int random) {
		frame.newMessage("Lets sleep for: " + random / 1000 + " seconds.");
		scriptShouldRun = false;
		sleeps(random);
		scriptShouldRun = true;
	}
	
	public LinkedList<RandomBreak> getBreakHandler() {
		return break_handler;
	}

	public int getLeftSleepTime() {
		return (int) ((sleepTimeStartTime + sleepTime) - System.currentTimeMillis());
	}
	
	public int getCurrentPlayTime() {
		return ticks - current_session_start;
	}
	
	public int getTicks() {
		return ticks;
	}
	
	public void destroy() {
		t.interrupt();
		t.interrupt();
	}
	

}
