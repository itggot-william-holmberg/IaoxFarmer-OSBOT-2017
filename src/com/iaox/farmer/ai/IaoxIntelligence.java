package com.iaox.farmer.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.skills.IntelligentAgility;
import com.iaox.farmer.ai.skills.IntelligentCombat;
import com.iaox.farmer.ai.skills.IntelligentFishing;
import com.iaox.farmer.ai.skills.IntelligentMining;
import com.iaox.farmer.ai.skills.IntelligentWoodcutting;
import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.fishing.FishingAssignment;
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
	private static FishingAssignment fishingAssignment;
	private static AgilityAssignment agilityAssignment;

	private static Entity lastClickedObject;

	private IntelligentMining intelligentMining;
	private IntelligentCombat intelligentCombat;
	private IntelligentWoodcutting intelligentWoodcutting;
	private IntelligentAgility intelligentAgility;
	private IntelligentFishing intelligentFishing;
	
	private IaoxCommunicator iaoxCommunicator;

	private int ticks;
	private int lastTickReset;

	private Node lastRollNode;
	private Players players;
	private int amountOfWorldChanges;

	public static boolean scriptShouldRun = true;

	public static List<RandomBreak> break_handler;
	private int current_session_start = 0;
	private long sleepTimeStartTime = 0;
	private int sleepTime = 0;
	private int lastMuleTrade;

	public void start(Script script) {
		this.script = script;
		this.players = new Players(script);

		frame = new AIFrame();
		frame.setVisible(true);

		intelligentMining = new IntelligentMining(this.script);
		intelligentCombat = new IntelligentCombat(this.script);
		intelligentWoodcutting = new IntelligentWoodcutting(this.script);
		intelligentAgility = new IntelligentAgility(this.script);
		intelligentFishing = new IntelligentFishing(this.script);

		iaoxCommunicator = new IaoxCommunicator(script.getBot().getMethods());
		
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
			if (Data.USE_BREAKS && nextBreak.getPlayTime() < (getCurrentPlayTime() / 60)) {
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
			woodcuttingAssignment = intelligentWoodcutting.getNewAssignment();
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
			miningAssignment = intelligentMining.getNewAssignment();
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
		WoodcuttingAssignment similarAssignment = intelligentWoodcutting
				.getSimilarAssignment(woodcuttingAssignment.getWCArea(), woodcuttingAssignment.getRequiredLevel());
		if (similarAssignment != null) {
			script.log("There is a better area for this assignment! lets switch.");
			woodcuttingAssignment = similarAssignment;
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
		setMiningAssignment(intelligentMining.getNewAssignment());
	}

	public void getNewFightingAssignment() {
		setFightingAssignment(intelligentCombat.getNewAssignment());
	}

	public static WoodcuttingAssignment getWCAssignment() {
		return woodcuttingAssignment;
	}

	public void getNewWCAssignment() {
		setWCAssignment(intelligentWoodcutting.getNewAssignment());
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

	public static FishingAssignment getFishingAssignment() {
		return fishingAssignment;
	}

	public void getNewFishingAssignment() {
		fishingAssignment = intelligentFishing.getNewAssignment();

	}

	private void generateNewBreaks() {
		script.log(Data.PLAYER_GENERATED_BREAKS);
		if (Data.PLAYER_GENERATED_BREAKS != null && !Data.PLAYER_GENERATED_BREAKS.isEmpty()) {
			Data.PLAYER_GENERATED_BREAKS.forEach(pBreak -> {
				break_handler.add(pBreak);
			});
		} else {
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
			break_handler.add(new RandomBreak(IaoxAIO.random(50,100), IaoxAIO.random(360, 540)));
		}
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
		for (int i = 0; i < 25; i++) {
			if (script.client.isLoggedIn()) {
				script.getLogoutTab().logOut();
				sleeps(2000);
			} else {
				break;
			}
		}

		sleeps(minutes * 60000);

		current_session_start = ticks;
		scriptShouldRun = true;
		return true;
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

	public static boolean lastClickedObject(Entity object) {
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
		int levelGoal = getExperienceGoal(assignment);
		return new Task(assignment, levelGoal, assignment.getSkill());
	}

	private int getExperienceGoal(Assignment assignment) {
		int currentLevel = script.getSkills().getStatic(assignment.getSkill());
		int currentExperience = script.getSkills().getExperience(assignment.getSkill());
		switch (assignment) {
		case ATTACK:
		case DEFENCE:
			if (currentLevel < 10) {
				return currentExperience + 500 +  IaoxAIO.random(1000);
			}
			if (currentLevel < 20) {
				return currentExperience + 1500 +  IaoxAIO.random(2000);
			}
			if (currentLevel < 30) {
				return currentExperience + 2000 +  IaoxAIO.random(3000);
			}
			if (currentLevel < 40) {
				return currentExperience + 3000 +  IaoxAIO.random(4000);
			}
			if (currentLevel < 50) {
				return currentExperience + 4000 +  IaoxAIO.random(5000);
			}
			if (currentLevel < 60) {
				return currentExperience + 4500 +  IaoxAIO.random(7500);
			}
			return currentExperience + 5000 +  IaoxAIO.random(10000);

		case AGILITY:
			if (currentLevel < 30) {
				return currentExperience + 2000 +  IaoxAIO.random(1500);
			}
			if (currentLevel < 40) {
				return currentExperience + 3000 +  IaoxAIO.random(4000);
			}
			if (currentLevel < 50) {
				return currentExperience + 4000 +  IaoxAIO.random(6000);
			}
			if (currentLevel < 60) {
				return currentExperience + 5000 +  IaoxAIO.random(8500);
			}
			if (currentLevel < 70) {
				return currentExperience + 7000 +  IaoxAIO.random(10000);
			}
			return currentExperience + 10000 +  IaoxAIO.random(12500);

		case STRENGTH:
		case WOODCUTTING:
			if (currentLevel < 10) {
				return currentExperience + 1000 +  IaoxAIO.random(500);
			}
			if (currentLevel < 20) {
				return currentExperience + 2000 +  IaoxAIO.random(1500);
			}
			if (currentLevel < 30) {
				return currentExperience + 3000 +  IaoxAIO.random(2000);
			}
			if (currentLevel < 40) {
				return currentExperience + 3500 +  IaoxAIO.random(3000);
			}
			if (currentLevel < 50) {
				return currentExperience + 4000 +  IaoxAIO.random(5000);
			}
			if (currentLevel < 60) {
				return currentExperience + 5000 +  IaoxAIO.random(5000);
			}
			if (currentLevel < 70) {
				return currentExperience + 7000 +  IaoxAIO.random(10000);
			}
			return currentExperience + 10000 +  IaoxAIO.random(15000);

		case MINING:
		case FISHING:
			if (currentLevel < 10) {
				return currentExperience + 750 +  IaoxAIO.random(500);
			}
			if (currentLevel < 20) {
				return currentExperience + 1000 +  IaoxAIO.random(750);
			}
			if (currentLevel < 30) {
				return currentExperience + 1500 +  IaoxAIO.random(1000);
			}
			if (currentLevel < 40) {
				return currentExperience + 2000 +  IaoxAIO.random(2000);
			}
			return currentExperience + 3000 +  IaoxAIO.random(3000);
		}
			return currentExperience + 3000 +  IaoxAIO.random(3000);
	}

	public Assignment getRandomAssignment() {
		int task = IaoxAIO.random(1, 7);
		Assignment ass = null;
		switch (task) {
		case 1:
		case 2:
		case 3:
		case 4:
			return getRandomCombatAssignment();
		case 5:
			return Assignment.MINING;
		case 6:
			return Assignment.WOODCUTTING;
		case 7:
			return Assignment.FISHING;
		/*
		 * case 6: if(script.worlds.isMembersWorld()){ return
		 * Assignment.AGILITY; } return getRandomAssignment();
		 */
		default:
			return Assignment.WOODCUTTING;
		}
	}

	public Assignment getRandomCombatAssignment() {
		int task = IaoxAIO.random(1, 3);
		if (script.getSkills().getStatic(Skill.ATTACK) >= script.getSkills().getStatic(Skill.STRENGTH)) {
			return Assignment.STRENGTH;
		}
		if (script.getSkills().getStatic(Skill.DEFENCE) >= script.getSkills().getStatic(Skill.ATTACK)
				|| script.getSkills().getStatic(Skill.STRENGTH) >= 30
						&& script.getSkills().getStatic(Skill.ATTACK) < 30 || 
							script.getSkills().getStatic(Skill.STRENGTH) >= 15
						&& script.getSkills().getStatic(Skill.ATTACK) < 15) {
			return Assignment.ATTACK;
		}
		switch (task) {
		case 1:
			return Assignment.ATTACK;
		case 2:
			return Assignment.STRENGTH;
		case 3:
			if (Data.trainDefence) {
				return Assignment.DEFENCE;
			} else {
				return getRandomCombatAssignment();
			}
		default:
			return Assignment.STRENGTH;

		}
	}
}
