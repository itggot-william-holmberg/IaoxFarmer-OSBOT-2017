package com.iaox.farmer.node.mining;



import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.node.Node;

public class MiningAction extends Node {
	
	@Override
	public boolean active() {
		return miningMethods.readyToMine() && miningMethods.playerInArea(IaoxIntelligence.getMiningAssignment().getObjectArea());
	}

	@Override
	public void execute() {
		if (!methodProvider.myPlayer().isAnimating()) {
			miningMethods.mine();
		} else {
			miningMethods.mineSleep();
		}
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "Mining";
	}

}
