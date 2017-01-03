package com.iaox.farmer.ai;

import org.osbot.rs07.script.Script;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.mining.MiningAssignment;

public class IaoxIntelligence implements Runnable {

	private AIFrame frame;
	private Thread t;
	private Script script;

	private static MiningAssignment miningAssignment;
	private IntelligentMining im;

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
			sleep(1000);
		}

	}

	private void intelligentMining() {
		if (miningAssignment == null) {
			miningAssignment = im.getNewAssignment();
			frame.newMessage("Updated mining assignment");
		}
	}

	public static MiningAssignment getMiningAssignment(){
		return miningAssignment;
	}
	public void start(Script script) {
		this.script = script;
		frame = new AIFrame();
		frame.setVisible(true);
		im = new IntelligentMining(script);
		if (t == null) {
			t = new Thread(this, "Iaox Intelligence Thread");
			t.start();
		}
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
