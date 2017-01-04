package com.iaox.farmer.node.mining;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.node.Node;

public class WalkToMiningLocation extends Node{

	@Override
	public boolean active() {
		return miningMethods.readyToMine() && !miningMethods.playerInArea(IaoxIntelligence.getMiningAssignment().getObjectArea());
	}

	@Override
	public void execute() {
		walkingMethods.webWalk(IaoxIntelligence.getMiningAssignment().getObjectArea());
	}
	
	@Override
	public boolean safeToInterrupt() {
		//we do not want IaoxIntelligence to interrupt the script when we are walking
		return false;
	}
	
	@Override
	public String toString() {
		return "Walking to mining location";
	}


}
