package com.iaox.farmer.ai;

import org.osbot.rs07.script.Script;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.node.Node;

public class IaoxIntelligence implements Runnable {

	private AIFrame frame;
	private Thread t;
	private Script script;

	private static MiningAssignment miningAssignment;
	private IntelligentMining im;
	private int ticks;
	private Node lastRollNode;

	@Override
	public void run() {
		while (!t.isInterrupted() && t.isAlive()) {

			if (IaoxAIO.CURRENT_TASK != null) {
				switch (IaoxAIO.CURRENT_TASK.getAssignment()) {
				case MINING:
					intelligentMining();
					break;
				default:
					break;

				}
			}

			/*
			 * It is safe to interrupt our node when mining.. banking.. etc.. It
			 * is not safe to interrupt our node when we are in combat or
			 * webwalking etc.. If it is safe to interrupt, "roll the dice" and
			 * if we hit a specific number we have to do something answer a
			 * phone call, open the dorr, yell at mom, go to the toilet, etc....
			 * various "afk" things that will make sure that the script is not
			 * doing the exact same thing during the whole task
			 * 
			 */

			if (IaoxAIO.CURRENT_NODE != null && IaoxAIO.CURRENT_NODE.safeToInterrupt()) {
				randomBehaviour();
			}
			// ~~ 3600 per hour
			sleep(1000);
			ticks++;
		}
	}

	private void randomBehaviour() {
		
		/*
		 * If we are currently banking && we havent done a random bank behaviour yet
		 * We not supposed to do the random behaviour more than once per bank node
		 * LastRollNode makes sure that we do not.
		 */

		if (IaoxAIO.CURRENT_NODE.toString().contains("Bank") && lastRollNode != IaoxAIO.CURRENT_NODE) {
			randomBankBehaviour();
			lastRollNode = IaoxAIO.CURRENT_NODE;
		} else {
			randomSkillingActionBehaviour();
		}

	}
	
	/*
	 * This method will be executed, for instance, when we are mining.
	 * Some appropriate afk behaviour would be to :
	 * Go to the toilet
	 * Answer the phone
	 * Go grab some food
	 * Etc... 
	 */

	private void randomSkillingActionBehaviour() {
		int diceNumber = IaoxAIO.random(3600);
		if (diceNumber == 3600) {

		}		
	}
	
	/*
	 * This method will be executed when we are standing in the bank.
	 * Some appropriate afk behaviour would be to go afk for a longer time etc...
	 */

	private void randomBankBehaviour() {
		// TODO Auto-generated method stub

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
		}
	}

	public static MiningAssignment getMiningAssignment() {
		return miningAssignment;
	}

	public void start(Script script) {
		this.script = script;
		frame = new AIFrame();
		frame.setVisible(true);
		im = new IntelligentMining(this.script);
		if (t == null) {
			t = new Thread(this, "Iaox Intelligence Thread");
			t.start();
		}
	}

	public int getTicks() {
		return ticks;
	}

	private void sleep(int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Thread getThread() {
		return t;
	}

}
