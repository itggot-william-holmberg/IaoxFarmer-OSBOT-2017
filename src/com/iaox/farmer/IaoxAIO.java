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
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.events.LoginEvent;
import com.iaox.farmer.frame.Gui;
import com.iaox.farmer.node.Node;
import com.iaox.farmer.node.mining.MiningAction;
import com.iaox.farmer.node.mining.MiningBank;
import com.iaox.farmer.node.mining.WalkToMiningBank;
import com.iaox.farmer.node.mining.WalkToMiningLocation;
import com.iaox.farmer.task.Task;
import com.iaox.farmer.tcp.MuleThread;

@ScriptManifest(author = "Iaox", info = "Farms for you", logo = "", name = "IaoxSlaves", version = 0.3)
public class IaoxAIO extends Script {

	private MuleThread muleThread;

	public static int coinsNeeded = 1337;
	private int startCash;

	public static boolean shouldTrade = false;

	private String username;
	private String password;

	public static boolean guiWait;
	private Gui gui;

	private IaoxIntelligence ii;

	public static Deque<Task> TASK_HANDLER;
	public static List<Node> NODE_HANDLER;

	public static Task CURRENT_TASK = null;
	public static Node CURRENT_NODE = null;
	public static String CURRENT_ACTION;

	public long timeStarted;
	public long timeRan;

	public ExperienceTracker xpTracker;

	@Override
	public void onStart() throws InterruptedException {

		TASK_HANDLER = new LinkedList<Task>();
		NODE_HANDLER = new ArrayList<Node>();

		username = getBot().getUsername();
		password = "ugot00wned2";

		muleThread = new MuleThread(this);

		gui = new Gui();
		guiWait = true;
		login();

		ii = new IaoxIntelligence();
		ii.start(this);

		timeStarted = System.currentTimeMillis();
		this.xpTracker = getExperienceTracker();
	}

	@Override
	public int onLoop() {
		handleGui();

		if (!shouldTrade) {
			handleTask();
		} else {
			handleTrade();
		}

		return 600;
	}

	private void handleTask() {
		if (CURRENT_TASK != null && !CURRENT_TASK.isCompleted()) {
			handleNode();
		} else {
			newTask();
		}
	}

	private void newTask() {
		if (!TASK_HANDLER.isEmpty()) {
			CURRENT_TASK = TASK_HANDLER.poll();
			updateNodes();
		} else {
			// generate new task in the future
			stop();
		}
	}

	private void updateNodes() {

		if (!NODE_HANDLER.isEmpty()) {
			NODE_HANDLER.clear();
		}

		switch (CURRENT_TASK.getAssignment().getType()) {
		case SKILL:
			skillSwitch();
			break;
		case COMBAT:
			break;
		case QUEST:
			questSwitch();
			break;
		default:
			break;
		}

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
			xpTracker.start(Skill.MINING);
			NODE_HANDLER.add(new MiningBank().init(this));
			NODE_HANDLER.add(new WalkToMiningBank().init(this));
			NODE_HANDLER.add(new WalkToMiningLocation().init(this));
			NODE_HANDLER.add(new MiningAction().init(this));
			break;
		}
	}

	private void handleNode() {
		if (!NODE_HANDLER.isEmpty()) {
			executeNode();
		} else if (!TASK_HANDLER.isEmpty()) {
			CURRENT_TASK = TASK_HANDLER.poll();
		} else {
			// In the future, generate a new task
			stop();
		}
	}

	private void executeNode() {
		Boolean active = false;
		for (Node node : NODE_HANDLER) {
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

	private void handleTrade() {

		if (muleThread.isRunning() && muleThread.getMule() != null) {
			trade();
		} else if (!muleThread.isRunning() && muleThread.getConnection()) {
			startNewThread();
		} else if (!muleThread.isRunning() && !muleThread.getConnection()) {
			logout("host is not available, lets logout and sleep until host is available");
		} else if (muleThread.isRunning() && muleThread.getMule() == null) {
			logout("Mule is not available, lets logout and sleep until mule is available");
			logoutTab.logOut();
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
			slaveTradeDeposit();
		}
	}

	private void slaveTradeDeposit() {

		if (!trade.isCurrentlyTrading() && !inventory.contains(995)) {
			log("we are done!");
			shouldTrade = false;
			return;
		}

		log("lets trade:" + muleThread.getMule());

		if (!trade.isCurrentlyTrading()) {
			tradePlayer();
		} else if (trade.isFirstInterfaceOpen()) {
			if (inventory.contains(995)) {
				trade.offer(995, (int) inventory.getAmount(995));
			} else if (trade.didOtherAcceptTrade()) {
				trade.acceptTrade();
			}
		} else if (trade.isSecondInterfaceOpen()) {
			acceptSecondTradeInterface();
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
		g.fillRect(5, 340, 505, 135);

		g.setColor(Color.BLACK);
		g.drawString("Time ran: " + ft(timeRan), 250, 360);

		g.setColor(Color.WHITE);
		g.drawString("Current task_list: " + TASK_HANDLER, 50, 370);
		g.drawString("Current node_list: " + NODE_HANDLER, 50, 390);
		g.drawString("Current task: " + CURRENT_TASK, 50, 410);
		g.drawString("Current node: " + CURRENT_NODE, 50, 430);
		g.drawString("Current action: " + CURRENT_ACTION, 50, 450);

		if (xpTracker != null && xpTracker.getGainedXP(Skill.MINING) > 0) {
			g.setColor(Color.RED);
			g.drawString("XP Gained: " + xpTracker.getGainedXP(Skill.MINING), 300, 410);
			g.drawString("Current XP/h: " + xpTracker.getGainedXPPerHour(Skill.MINING), 300, 430);
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

		if (ii.getThread() != null) {
			ii.getThread().interrupt();
			ii.getThread().interrupt();
		}
		ii.getThread().interrupt();

		gui.frame.dispose();
	}

}
