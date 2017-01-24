package com.iaox.farmer;

import java.awt.Color;
import java.awt.Graphics2D;
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

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.events.LoginEvent;
import com.iaox.farmer.frame.Gui;
import com.iaox.farmer.node.Node;
import com.iaox.farmer.node.combat.BankFight;
import com.iaox.farmer.node.combat.Fight;
import com.iaox.farmer.node.combat.WalkToBankFromFight;
import com.iaox.farmer.node.combat.WalkToFight;
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

@ScriptManifest(author = "Iaox", info = "Farms for you", logo = "", name = "IaoxSlave", version = 0.3)
public class IaoxAIO extends Script {

	public static MuleThread muleThread;

	private int startCash;
	public static int coinsNeeded = 0;
	public static boolean shouldTrade = false;
	public static boolean shouldWithdrawCash;

	private String username;
	private String password;

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

	public static boolean shouldThreadRun = true;

	@Override
	public void onStart() throws InterruptedException {

		TASK_HANDLER = new LinkedList<Task>();
		NODE_HANDLER = new ArrayList<Node>();
		GRAND_EXCHANGE_NODE_HANDLER = new ArrayList<Node>();
		MULE_NODE_HANDLER = new ArrayList<Node>();

		addGrandExchangeData();

		username = getBot().getUsername();
		switch (username) {
		case "rebeccaabbe@clayvolatile.tk":
			password = "bella";
			break;
		default:
			password = "boowoo123";
			break;
		}

		muleThread = new MuleThread(this);

		gui = new Gui();
		guiWait = true;
		
		login();

		ii = new IaoxIntelligence();
		ii.start(this);

		timeStarted = System.currentTimeMillis();

		GrandExchangeData.ITEMS_TO_BUY_LIST = new ArrayList<IaoxItem>();

	}

	@Override
	public int onLoop() {
		handleGui();
		if (shouldTrade) {
			handleTrade();
		} else if (!GrandExchangeData.ITEMS_TO_BUY_LIST.isEmpty()) {
			handleGrandExchange();
		} else {
			handleTask();
		}

		return 600;
	}

	private void addGrandExchangeData() {
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS = new ArrayList<IaoxItem>();
		GrandExchangeData.CURRENT_SELLABLE_ITEMS = new ArrayList<IaoxItem>();
		GrandExchangeData.DEFAULT_SELLABLE_ITEMS.add(IaoxItem.IRON_ORE);
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
		Boolean active = false;
		for (Node node : GRAND_EXCHANGE_NODE_HANDLER) {
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
		if (!TASK_HANDLER.isEmpty()) {
			CURRENT_TASK = TASK_HANDLER.getFirst();
			updateNodes();
		} else {
			Assignment ass = Assignment.values()[random(Assignment.values().length)];
			TASK_HANDLER.add(new Task(ass, getSkills().getDynamic(ass.getSkill())+1, ass.getSkill()));
			
		}
	}

	private void updateNodes() {

		GRAND_EXCHANGE_NODE_HANDLER.clear();
		MULE_NODE_HANDLER.clear();
		IaoxAIO.coinsNeeded = 0;
		if (!NODE_HANDLER.isEmpty()) {
			NODE_HANDLER.clear();
		}

		switch (CURRENT_TASK.getAssignment().getType()) {
		case SKILL:
			skillSwitch();
			break;
		case COMBAT:
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
			sleeps(IaoxAIO.random(50000,60000));
		} else if (muleThread.isRunning() && muleThread.getMule() == null) {
			logout("Mule is not available, lets logout and sleep until mule is available");
			logoutTab.logOut();
			sleeps(IaoxAIO.random(50000,60000));
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
		LoginEvent loginEvent = new LoginEvent(username, password);
		getBot().addLoginListener(loginEvent);
		execute(loginEvent);
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

		if (startXP > 0) {
			g.setColor(Color.BLACK);
			xpGained = getSkills().getExperience(CURRENT_TASK.getAssignment().getSkill()) - startXP;
			g.drawString("XP Gained: " + xpGained, 300, 410);
			g.drawString("Current XP/h: " + getPerHour(xpGained, timeRan), 300, 430);
		}

		if (ii.getBreakHandler() != null) {
			g.setColor(Color.RED);
			g.drawString("Current break_list " + ii.getBreakHandler(), 50, 50);
			if (ii.getLeftSleepTime() > 1000) {
				g.drawString("We are breaking! ", 50, 70);
				g.drawString("Time untill break is done: " + ft(ii.getLeftSleepTime()), 50, 90);
			} else {
				g.drawString("Current sesson_playtime(ticks) " + ii.getCurrentPlayTime(), 50, 70);
				g.drawString("We will break in "
						+ (ii.getBreakHandler().getFirst().getPlayTime() - (ii.getCurrentPlayTime() / 60) + " minutes"),
						50, 90);
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
