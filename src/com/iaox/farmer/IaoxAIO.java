package com.iaox.farmer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.account.Mule;
import com.iaox.farmer.account.RSAccount;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.events.LoginEvent;
import com.iaox.farmer.frame.Gui;
import com.iaox.farmer.node.Node;
import com.iaox.farmer.node.agility.gnome.GnomeCourse;
import com.iaox.farmer.node.agility.gnome.WalkToTreeGnome;
import com.iaox.farmer.node.combat.BankFight;
import com.iaox.farmer.node.combat.Fight;
import com.iaox.farmer.node.combat.WalkToBankFromFight;
import com.iaox.farmer.node.combat.WalkToFight;
import com.iaox.farmer.node.crafting.CraftingAction;
import com.iaox.farmer.node.crafting.CraftingBank;
import com.iaox.farmer.node.crafting.CraftingWalkToAction;
import com.iaox.farmer.node.crafting.CraftingWalkToBank;
import com.iaox.farmer.node.fishing.FishingAction;
import com.iaox.farmer.node.fishing.FishingBank;
import com.iaox.farmer.node.fishing.WalkToFishingBank;
import com.iaox.farmer.node.fishing.WalkToFishingLocation;
import com.iaox.farmer.node.grandexchange.BuyItem;
import com.iaox.farmer.node.grandexchange.GrandExchangeBank;
import com.iaox.farmer.node.grandexchange.WalkToGrandExchange;
import com.iaox.farmer.node.mining.MiningAction;
import com.iaox.farmer.node.mining.MiningBank;
import com.iaox.farmer.node.mining.WalkToMiningBank;
import com.iaox.farmer.node.mining.WalkToMiningLocation;
import com.iaox.farmer.node.mule.Deposit;
import com.iaox.farmer.node.mule.DepositBank;
import com.iaox.farmer.node.mule.SellItems;
import com.iaox.farmer.node.woodcutting.WCAction;
import com.iaox.farmer.node.woodcutting.WCBank;
import com.iaox.farmer.node.woodcutting.WalkToWCBank;
import com.iaox.farmer.node.woodcutting.WalkToWCLocation;
import com.iaox.farmer.task.Task;
import com.iaox.farmer.tcp.MuleThread;

@ScriptManifest(author = "Iaox", info = "Farms for you", logo = "", name = "IaoxSlave4", version = 0.3)
public class IaoxAIO extends Script {

	public static MuleThread muleThread;

	private int startCash;
	public static int coinsNeeded = 0;
	public static boolean shouldTrade = false;
	public static boolean shouldWithdrawCash;

	public static boolean guiWait;
	private Gui gui;

	private IaoxIntelligence ii;

	public static Deque<Task> TASK_HANDLER;
	public static List<Node> NODE_HANDLER;
	public static List<Node> GRAND_EXCHANGE_NODE_HANDLER;
	public static List<Node> MULE_NODE_HANDLER;

	public static Task CURRENT_TASK = null;
	public static Node CURRENT_NODE = null;
	public static String CURRENT_ACTION;

	public long timeStarted;
	public long timeRan;

	private int startXP = 0;

	private int xpGained;

	private long taskTimeRan;

	private long taskTimeStarted;

	public static Mule mule;

	public static boolean shouldStop = false;

	public static boolean shouldThreadRun = true;

	public static RSAccount currentAccount = null;

	public static boolean shouldUpdateTutorialIslandStatus = false;

	public static boolean updatedTutorialIslandStatus = false;

	@Override
	public void onStart() throws InterruptedException {
		currentAccount = new RSAccount("tryminex@gmail.com", "password123", true);
		// check so the ip is what it should be!
		BufferedReader in;
		String ip = "nothingyet";
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			ip = in.readLine(); // you get the IP as a String
			log(ip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * String par = getParameters();
		 * if(getBot().getUsername().equals("comparate664@hotmail.com")){
		 * log("this user shoudl not be using a proxy"); }else if (par != null
		 * && par.equals(ip)) { log("The user is succesfully uising a proxy" +
		 * ip); }else{
		 * log("params does not exist or the ip does not match. lets exit");
		 * shouldStop = true; }
		 */

		// let the bot fully start
		sleep(5000);

		TASK_HANDLER = new LinkedList<Task>();
		NODE_HANDLER = new ArrayList<Node>();
		GRAND_EXCHANGE_NODE_HANDLER = new ArrayList<Node>();
		MULE_NODE_HANDLER = new ArrayList<Node>();

		addGrandExchangeData();

		muleThread = new MuleThread(this);

		gui = new Gui();
		guiWait = true;

		login();

		timeStarted = System.currentTimeMillis();

		GrandExchangeData.ITEMS_TO_BUY_LIST = new ArrayList<IaoxItem>();

	}

	@Override
	public int onLoop() {
		if (shouldStop) {
			stop();
		} else if (guiWait) {
			handleGui();
		} else if (ii == null) {
			initIaoxIntelligent();
		} else if (shouldTrade) {
			handleTrade();
		} else if (!GrandExchangeData.ITEMS_TO_BUY_LIST.isEmpty()) {
			handleGrandExchange();
		} else {
			handleTask();
		}

		return 600;
	}

	private void initIaoxIntelligent() {
		if (ii == null) {
			ii = new IaoxIntelligence();
			ii.start(this);
		}

	}

	private void handleBreakLogout() {
		if (client.isLoggedIn()) {
			logout("We should break");
		}
	}

	private void addGrandExchangeData() {
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS = new ArrayList<IaoxItem>();
		GrandExchangeData.CURRENT_SELLABLE_ITEMS = new ArrayList<IaoxItem>();
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.IRON_ORE);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.GRIMY_DWARF_WEED);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.GRIMY_CADANTINE);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.GRIMY_AVANTOE);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.GRIMY_HARRALANDER);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.GRIMY_IRIT);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.GRIMY_KWUARM);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.GRIMY_LANTADYME);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.GRIMY_RANARR_WEED);
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.MOLTEN_GLASS);
		addGrandExchangeNodes();
	}

	private void handleGrandExchange() {
		if (!GRAND_EXCHANGE_NODE_HANDLER.isEmpty()) {
			executeGrandExchangeNode();
		} else {
			addGrandExchangeNodes();
		}
	}

	private void executeGrandExchangeNode() {
		log("handle ge task");
		if (client.isLoggedIn()) {
			Boolean active = false;
			for (Node node : GRAND_EXCHANGE_NODE_HANDLER) {
				if (node.active()) {
					active = true;
					CURRENT_NODE = node;
					node.execute();
					break;
				}
			}

			// Should probably keep track of this to map where and when no node
			// can
			// be found.
			if (!active) {
				CURRENT_NODE = null;
			}
		} else {
			login();
		}

	}

	private void handleTask() {
		if (CURRENT_TASK == null) {
			newTask();
		} else if (CURRENT_TASK.isCompleted(this)) {
			TASK_HANDLER.remove(CURRENT_TASK);
			CURRENT_TASK = null;
		} else {
			handleNode();
		}
	}

	private void newTask() {
		Task breakHandlerTask = ii.getBreakHandler().get(0).getTask();
		if (!TASK_HANDLER.isEmpty()) {
			CURRENT_TASK = TASK_HANDLER.getFirst();
			CURRENT_TASK.setBreakTime(CURRENT_TASK.getPlayTime());
			log(CURRENT_TASK.getPlayTime());
			log(CURRENT_TASK.getBreakTime());
			log(System.currentTimeMillis());
			updateNodes();
		} else if (breakHandlerTask != null && !breakHandlerTask.isCompleted(this) && Data.ONE_TASK_PER_PLAYTIME) {
			TASK_HANDLER.add(breakHandlerTask);
		} else if (getConfigs().get(281) != 1000) {
			TASK_HANDLER.add(new Task(Assignment.TUTORIAL_ISLAND));
		} else {
			TASK_HANDLER.add(ii.generateNewTask());
		}
	}

	private void updateNodes() {

		GRAND_EXCHANGE_NODE_HANDLER.clear();
		MULE_NODE_HANDLER.clear();
		IaoxAIO.coinsNeeded = 0;
		if (!NODE_HANDLER.isEmpty()) {
			NODE_HANDLER.clear();
		}
		taskTimeStarted = System.currentTimeMillis();
		switch (CURRENT_TASK.getAssignment().getType()) {
		case SKILL:
			skillSwitch();
			break;
		case MELEE:
			combatSwitch();
			break;
		case QUEST:
			questSwitch();
			break;
		default:
			break;
		}

	}

	private void combatSwitch() {
		ii.getNewFightingAssignment();
		startXP = getSkills().getExperience(CURRENT_TASK.getAssignment().getSkill());
		NODE_HANDLER.add(new BankFight().init(this));
		NODE_HANDLER.add(new Fight().init(this));
		NODE_HANDLER.add(new WalkToBankFromFight().init(this));
		NODE_HANDLER.add(new WalkToFight().init(this));
	}

	private void handleGui() {
		while (guiWait) {
			if (!gui.isShowing()) {
				gui.frame.setVisible(true);
			}
			sleeps(200, 500);
		}
	}

	private void questSwitch() {
		// NODE_HANDLER.add.....
		// NODE_HANDLER.add.....
	}

	private void skillSwitch() {

		switch (CURRENT_TASK.getAssignment()) {
		case MINING:
			startXP = getSkills().getExperience(Skill.MINING);
			ii.getNewMiningAssignment();

			NODE_HANDLER.add(new MiningBank().init(this));
			NODE_HANDLER.add(new WalkToMiningBank().init(this));
			NODE_HANDLER.add(new WalkToMiningLocation().init(this));
			NODE_HANDLER.add(new MiningAction().init(this));
			break;
		case WOODCUTTING:
			startXP = getSkills().getExperience(Skill.WOODCUTTING);
			ii.getNewWCAssignment();

			NODE_HANDLER.add(new WCAction().init(this));
			NODE_HANDLER.add(new WalkToWCBank().init(this));
			NODE_HANDLER.add(new WalkToWCLocation().init(this));
			NODE_HANDLER.add(new WCBank().init(this));
			break;
		case AGILITY:
			startXP = getSkills().getExperience(Skill.AGILITY);
			ii.getNewAgilityAssignment();

			NODE_HANDLER.add(new GnomeCourse().init(this));
			NODE_HANDLER.add(new WalkToTreeGnome().init(this));
		case FISHING:
			startXP = getSkills().getExperience(Skill.FISHING);
			ii.getNewFishingAssignment();

			NODE_HANDLER.add(new FishingAction().init(this));
			NODE_HANDLER.add(new FishingBank().init(this));
			NODE_HANDLER.add(new WalkToFishingBank().init(this));
			NODE_HANDLER.add(new WalkToFishingLocation().init(this));
		case CRAFTING:
			startXP = getSkills().getExperience(Skill.CRAFTING);
			ii.getNewCraftingAssignment();

			NODE_HANDLER.add(new CraftingAction().init(this));
			NODE_HANDLER.add(new CraftingBank().init(this));
			NODE_HANDLER.add(new CraftingWalkToAction().init(this));
			NODE_HANDLER.add(new CraftingWalkToBank().init(this));
		}
	}

	private void addGrandExchangeNodes() {

		NODE_HANDLER.clear();
		MULE_NODE_HANDLER.clear();
		// reset the current sellable items list.
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.forEach(item -> {
			GrandExchangeData.CURRENT_SELLABLE_ITEMS.add(item);
		});

		GRAND_EXCHANGE_NODE_HANDLER.add(new BuyItem().init(this));
		GRAND_EXCHANGE_NODE_HANDLER.add(new WalkToGrandExchange().init(this));
		GRAND_EXCHANGE_NODE_HANDLER.add(new GrandExchangeBank().init(this));
	}

	private void addMuleDepositNodes() {

		NODE_HANDLER.clear();
		GRAND_EXCHANGE_NODE_HANDLER.clear();
		// reset the current sellable items list.
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.forEach(item -> {
			GrandExchangeData.CURRENT_SELLABLE_ITEMS.add(item);
		});

		MULE_NODE_HANDLER.add(new Deposit().init(this));
		MULE_NODE_HANDLER.add(new DepositBank().init(this));
		MULE_NODE_HANDLER.add(new SellItems().init(this));
		MULE_NODE_HANDLER.add(new WalkToGrandExchange().init(this));
	}

	private void handleNode() {
		if (NODE_HANDLER.isEmpty()) {
			updateNodes();
		} else if (ii.scriptShouldRun) {
			executeNode();
		}
	}

	private void executeNode() {
		if (client.isLoggedIn()) {
			Boolean active = false;
			for (Node node : NODE_HANDLER) {
				if (node.active()) {
					active = true;
					CURRENT_NODE = node;
					node.execute();
					break;
				}
			}

			// Should probably keep track of this to map where and when no node
			// can
			// be found.
			if (!active) {
				CURRENT_NODE = null;
			}
		} else {
			login();
		}
	}

	private void handleTrade() {

		if (muleThread.isRunning() && muleThread.getMule() != null) {
			trade();
		} else if (!muleThread.isRunning() && muleThread.getConnection()) {
			startNewThread();
		} else if (!muleThread.isRunning() && !muleThread.getConnection()) {
			logout("host is not available, lets logout and sleep until host is available");
			sleeps(IaoxAIO.random(50000, 60000));
		} else if (muleThread.isRunning() && muleThread.getMule() == null) {
			logout("Mule is not available, lets logout and sleep until mule is available");
			logoutTab.logOut();
			sleeps(IaoxAIO.random(50000, 60000));
		}
	}

	private void startNewThread() {
		// Since the thread is using ingame data we have to be logged in before
		// we start a new thread
		if (client.isLoggedIn()) {
			muleThread = new MuleThread(this);
			muleThread.getConnection();
			muleThread.start();

			startCash = (int) inventory.getAmount(995);
		} else {
			login();
		}
	}

	private void trade() {
		if (!client.isLoggedIn()) {
			login();
		} else if (coinsNeeded > 0) {
			slaveTradeWithdraw();
		} else {
			handleMuleDepositNodes();
		}
	}

	private void handleMuleDepositNodes() {
		if (!MULE_NODE_HANDLER.isEmpty()) {
			executeMuleDepositNode();
		} else {
			addMuleDepositNodes();
		}
	}

	private void executeMuleDepositNode() {
		log("handle mule task");
		Boolean active = false;
		for (Node node : MULE_NODE_HANDLER) {
			if (node.active()) {
				active = true;
				CURRENT_NODE = node;
				node.execute();
				break;
			}
		}

		// Should probably keep track of this to map where and when no node can
		// be found.
		if (!active) {
			CURRENT_NODE = null;
		}
	}

	private void slaveTradeWithdraw() {
		log("lets trade:" + muleThread.getMule());
		if (inventory.getAmount(995) >= (startCash + coinsNeeded)) {
			log("we are done!");
			shouldTrade = false;
			return;
		}

		if (!trade.isCurrentlyTrading()) {
			tradePlayer();
		} else if (trade.isFirstInterfaceOpen()) {
			if (trade.didOtherAcceptTrade()) {
				trade.acceptTrade();
			}
		} else if (trade.isSecondInterfaceOpen()) {
			acceptSecondTradeInterface();
		}

	}

	@SuppressWarnings("unchecked")
	private void tradePlayer() {
		Player player = players.closest(p -> p.getName().equals(muleThread.getMule()));
		if (player != null) {
			log("player exists, lets trade");
			player.interact("Trade with");
			new ConditionalSleep(10000) {
				@Override
				public boolean condition() throws InterruptedException {
					return trade.isCurrentlyTrading();
				}

			}.sleep();
		}
	}

	private void acceptSecondTradeInterface() {

		if (trade.didOtherAcceptTrade()) {
			trade.acceptTrade();
			new ConditionalSleep(10000) {
				@Override
				public boolean condition() throws InterruptedException {
					return trade.isCurrentlyTrading();
				}
			}.sleep();
		}

		if (!trade.isCurrentlyTrading() && !inventory.contains(995)) {
			shouldTrade = false;
		}
	}

	public void login() {
		log("lets login");
		if (!client.isLoggedIn() && currentAccount != null) {
			LoginEvent loginEvent = new LoginEvent(currentAccount.getLogin(), currentAccount.getPassword());
			getBot().addLoginListener(loginEvent);
			execute(loginEvent);
		} else {
			log("we need an account");
		}
	}

	private void logout(String logoutMessage) {
		log(logoutMessage);
		logoutTab.logOut();
	}

	@Override
	public void onPaint(Graphics2D g) {
		timeRan = System.currentTimeMillis() - timeStarted;

		g.setColor(new Color(56, 58, 60));
		// g.fillRect(5, 340, 505, 135);

		g.setColor(Color.BLACK);
		g.drawString("Time ran: " + ft(timeRan), 250, 360);
		if (ii != null) {
			g.drawString("Amount of ticks: " + ii.getTicks(), 380, 360);
		}

		g.setColor(Color.WHITE);
		g.drawString("Current task_list: " + TASK_HANDLER, 50, 370);
		g.drawString("Current node_list: " + NODE_HANDLER, 50, 390);
		g.drawString("Current task: " + CURRENT_TASK, 50, 410);
		g.drawString("Current node: " + CURRENT_NODE, 50, 430);
		g.drawString("Current action: " + CURRENT_ACTION, 50, 450);
		g.drawString("Current Account: " + currentAccount.getLogin(), 50, 110);

		if (startXP > 0) {
			g.setColor(Color.BLACK);
			xpGained = getSkills().getExperience(CURRENT_TASK.getAssignment().getSkill()) - startXP;
			g.drawString("XP Gained: " + xpGained, 300, 410);
			taskTimeRan = System.currentTimeMillis() - taskTimeStarted;
			g.drawString("Current XP/h: " + getPerHour(xpGained, taskTimeRan), 300, 430);
		}

		if (ii.getBreakHandler() != null && Data.USE_BREAKS) {
			g.setColor(Color.RED);
			g.drawString("Current break_list " + ii.getBreakHandler(), 50, 50);
			if (ii.getLeftSleepTime() > 1000) {
				g.drawString("We are breaking! ", 50, 70);
				g.drawString("Time untill break is done: " + ft(ii.getLeftSleepTime()), 50, 90);
			} else {
				g.drawString("Current sesson_playtime(ticks) " + ii.getCurrentPlayTime(), 50, 70);
				g.drawString("We will break in "
						+ (ii.getBreakHandler().get(0).getPlayTime() - (ii.getCurrentPlayTime() / 60) + " minutes"), 50,
						90);
			}
		}
	}

	public int getPerHour(int totalGainedInRunTime, long runTime) {
		return (int) (totalGainedInRunTime * 3600000.0D / runTime);
	}

	public void sleeps(int milli) {
		try {
			sleep(milli);
		} catch (Exception e) {

		}
	}

	public void sleeps(int milli, int milli2) {
		try {
			sleep(random(milli, milli2));
		} catch (Exception e) {

		}
	}

	public static void realSleep(int milli) {
		try {
			Script.sleep(milli);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String ft(long duration) {

		String res = "";
		long days = TimeUnit.MILLISECONDS.toDays(duration);
		long hours = TimeUnit.MILLISECONDS.toHours(duration)
				- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
		if (days == 0) {
			res = (hours + ":" + minutes + ":" + seconds);
		} else {
			res = (days + ":" + hours + ":" + minutes + ":" + seconds);
		}
		return res;
	}

	@Override
	public void onExit() throws InterruptedException {
		if (muleThread.getThread() != null) {
			muleThread.getThread().interrupt();
		}

		ii.destroy();
		shouldThreadRun = false;
		gui.frame.dispose();
	}
}
