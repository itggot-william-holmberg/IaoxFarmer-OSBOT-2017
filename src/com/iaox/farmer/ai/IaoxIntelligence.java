package com.iaox.farmer.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.skills.IntelligentAgility;
import com.iaox.farmer.ai.skills.IntelligentCombat;
import com.iaox.farmer.ai.skills.IntelligentMining;
import com.iaox.farmer.ai.skills.IntelligentWoodcutting;
import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.assignment.woodcutting.WoodcuttingAssignment;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.node.Node;
import com.iaox.farmer.node.methods.Players;
import com.iaox.farmer.task.Task;

public class IaoxIntelligence implements Runnable {

	private AIFrame frame;
	private Thread t;
	private Script script;

	private static MiningAssignment miningAssignment;
	private static FightingAssignment fightingAssignment;
	private static WoodcuttingAssignment woodcuttingAssignment;
	private static Entity lastClickedObject;
	private static AgilityAssignment agilityAssignment;
	private IntelligentMining im;
	private IntelligentCombat ic;
	private IntelligentWoodcutting iw;
	private IntelligentAgility intelligentAgility;
	
	private int ticks;
	private int lastTickReset;

	private Node lastRollNode;
	private Players players;
	private int amountOfWorldChanges;

	public boolean scriptShouldRun = true;

	private List<RandomBreak> break_handler;
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
		ic = new IntelligentCombat(this.script);
		iw = new IntelligentWoodcutting(this.script);
		intelligentAgility = new IntelligentAgility(this.script);

		break_handler = new ArrayList<RandomBreak>();
		generateNewBreaks();

		if (t == null) {
			t = new Thread(this, "Iaox Intelligence Thread");
			t.start();
		}
	}

	@Override
	public void run() {
		while (IaoxAIO.shouldThreadRun && !t.isInterrupted() && t.isAlive()) {

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
					// intelligentMuleDeposit();
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
		lastClickedObject = null;
		lastTickReset = ticks;
	}

	private void intelligentSkilling() {
		switch (IaoxAIO.CURRENT_TASK.getAssignment()) {
		case MINING:
			intelligentMining();
			break;
		case WOODCUTTING:
			intelligentWoodcutting();
		default:
			break;

		}
	}

	private void intelligentBreaking() {
		if (break_handler.isEmpty()) {
			generateNewBreaks();
		} else {
			RandomBreak nextBreak = break_handler.get(0);
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
		if (ticks - lastMuleTrade > 10000 && IaoxAIO.random(100) == 50
				&& script.getSkills().getStatic(Skill.MINING) > 55) {
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

	private void intelligentWoodcutting() {

		/*
		 * If we already have an assignment, do nothing. In the future, maybe
		 * change assignment in the middle of the task For instance, if the
		 * current spot is "overcrowded" - change world or spot
		 */
		if (woodcuttingAssignment == null) {
			woodcuttingAssignment = iw.getNewAssignment();
			frame.newMessage("Updated wc assignment");
		} else if (script.myPlayer().isUnderAttack() && IaoxAIO.CURRENT_NODE != null
				&& IaoxAIO.CURRENT_NODE.toString().equals("Woodcut")) {
			script.log("we are under attack");
			findBetterWoodcuttingAssignment();
		}
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
			findBetterMiningAssignment();
		}
	}

	private void findBetterMiningAssignment() {
		MiningAssignment similarAssignment = getSimilarMiningAssignment();
		if (similarAssignment != null && players.playersInArea(similarAssignment.getObjectArea()) == 0) {
			script.log("There is a better area for this assignment! lets switch.");
			miningAssignment = similarAssignment;
		} else if (amountOfWorldChanges < 2) {
			changeWorld();
		}

	}

	private void findBetterWoodcuttingAssignment() {
		WoodcuttingAssignment similarAssignment = iw.getSimilarAssignment(woodcuttingAssignment.getWCArea(), woodcuttingAssignment.getRequiredLevel());
		if (similarAssignment != null) {
			script.log("There is a better area for this assignment! lets switch.");
			woodcuttingAssignment = similarAssignment;
			sleeps(10000);
		}
	}

	private void changeWorld() {
		scriptShouldRun = false;
		if (script.getWorlds().isMembersWorld()) {
			int currentWorld = script.getWorlds().getCurrentWorld();
			script.getWorlds().hop(getRandomP2PWorld());
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
					return script.worlds != null && script.worlds.getCurrentWorld() != currentWorld2;
				}
			}.sleep();
		}
		amountOfWorldChanges++;
		frame.newMessage("Changed world");
		scriptShouldRun = true;
	}

	private int getRandomP2PWorld() {
		return Data.AVAILABLE_P2P_WORLDS[IaoxAIO.random(Data.AVAILABLE_P2P_WORLDS.length)];
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

	public static FightingAssignment getFightingAssignment() {
		return fightingAssignment;
	}

	public static MiningAssignment getMiningAssignment() {
		return miningAssignment;
	}

	public void setMiningAssignment(MiningAssignment ass) {
		miningAssignment = ass;
	}

	public void setFightingAssignment(FightingAssignment ass) {
		fightingAssignment = ass;
	}

	public void getNewMiningAssignment() {
		setMiningAssignment(im.getNewAssignment());
	}

	public void getNewFightingAssignment() {
		setFightingAssignment(ic.getNewAssignment());
	}

	public static WoodcuttingAssignment getWCAssignment() {
		return woodcuttingAssignment;
	}

	public void getNewWCAssignment() {
		setWCAssignment(iw.getNewAssignment());
	}

	private void setWCAssignment(WoodcuttingAssignment woodcuttingAssignment) {
		this.woodcuttingAssignment = woodcuttingAssignment;

	}
	
	public static AgilityAssignment getAgilityAssignment() {
		return agilityAssignment;
	}
	
	public void getNewAgilityAssignment() {
		this.agilityAssignment = intelligentAgility.getNewAssignment();
		
	}

	private void generateNewBreaks() {
		int breaks = IaoxAIO.random(4, 7);
		for (int i = 0; i < breaks; i++) {
			/*
			 * Random breaks such as eating dinner etc
			 */
			break_handler.add(new RandomBreak(IaoxAIO.random(35, 56), IaoxAIO.random(5, 25), generateNewTask()));
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

	public List<RandomBreak> getBreakHandler() {
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

	public static boolean lastClickedObject(RS2Object object) {
		if (lastClickedObject == null) {
			lastClickedObject = object;
			return false;
		}
		Boolean bool = lastClickedObject.equals(object);
		lastClickedObject = object;
		return bool;
	}

	public Task generateNewTask() {
		Assignment assignment = getRandomAssignment();
		int levelGoal = getLevelGoal(assignment);
		return new Task(assignment, levelGoal, assignment.getSkill());
	}

	private int getLevelGoal(Assignment assignment) {
		int level = script.getSkills().getStatic(assignment.getSkill());
		switch (assignment) {
		case ATTACK:
		case DEFENCE:
			if (level < 10) {
				return level + 7 + IaoxAIO.random(0, 7);
			}
			if (level < 20) {
				return level + 5 + IaoxAIO.random(0, 5);
			}
			if (level < 30) {
				return level + 3 + IaoxAIO.random(0, 3);
			}
			if (level < 40) {
				return level + 2 + IaoxAIO.random(0, 2);
			}
			if (level < 50) {
				return level + 1 + IaoxAIO.random(0, 2);
			}
			return level + 1;
			
		case AGILITY:
			if (level < 30) {
				return 30;
			}
			if (level < 40) {
				return level + 5 + IaoxAIO.random(0, 5);
			}
			if (level < 50) {
				return level + 3 + IaoxAIO.random(0, 3);
			}
			if (level < 60) {
				return level + 2 + IaoxAIO.random(0, 2);
			}
			if (level < 70) {
				return level + 1 + IaoxAIO.random(0, 2);
			}
			return level + 1;

		case STRENGTH:
		case WOODCUTTING:
			if (level < 10) {
				return level + 10 + IaoxAIO.random(0, 10);
			}
			if (level < 20) {
				return level + 7 + IaoxAIO.random(0, 7);
			}
			if (level < 30) {
				return level + 6 + IaoxAIO.random(0, 3);
			}
			if (level < 40) {
				return level + 5 + IaoxAIO.random(0, 3);
			}
			if (level < 50) {
				return level + 4 + IaoxAIO.random(0, 2);
			}
			if (level < 60) {
				return level + 2 + IaoxAIO.random(0, 2);
			}
			if (level < 70) {
				return level + 1 + IaoxAIO.random(0, 1);
			}
			return level + 1;

		case MINING:
			if (level < 10) {
				return level + 5 + IaoxAIO.random(0, 5);
			}
			if (level < 20) {
				return level + 3 + IaoxAIO.random(0, 3);
			}
			if (level < 30) {
				return level + 2 + IaoxAIO.random(0, 3);
			}
			if (level < 40) {
				return level + 1 + IaoxAIO.random(0, 2);
			}
			return level + 1 + IaoxAIO.random(0, 1);
		}
		
		
		return level + 1;
	}

	public Assignment getRandomAssignment() {
		int task = IaoxAIO.random(1, 6);
		Assignment ass = null;
		switch (task) {
		case 1:
		case 2:
		case 3:
			return getRandomCombatAssignment();
		case 4:
			return Assignment.MINING;
		case 5:
			return Assignment.WOODCUTTING;
		case 6:
			if(script.worlds.isMembersWorld()){
				return Assignment.AGILITY;
			}
			return getRandomAssignment();
		default:
			return Assignment.WOODCUTTING;
		}
	}

	public Assignment getRandomCombatAssignment() {
		int task = IaoxAIO.random(1, 3);
		if (script.getSkills().getStatic(Skill.ATTACK) >= script.getSkills().getStatic(Skill.STRENGTH)) {
			return Assignment.STRENGTH;
		}
		if (script.getSkills().getStatic(Skill.DEFENCE) >= script.getSkills().getStatic(Skill.ATTACK)) {
			return Assignment.ATTACK;
		}
		switch (task) {
		case 1:
			return Assignment.ATTACK;
		case 2:
			return Assignment.STRENGTH;
		case 3:
			return Assignment.DEFENCE;
		default:
			return Assignment.STRENGTH;

		}
	}
}
