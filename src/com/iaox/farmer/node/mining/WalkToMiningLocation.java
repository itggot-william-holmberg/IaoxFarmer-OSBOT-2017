package com.iaox.farmer.node.mining;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.node.Node;

public class WalkToMiningLocation extends Node{

	@Override
	public boolean active() {
		return readyToMine() && !playerInArea(IaoxIntelligence.getMiningAssignment().getMiningArea());
	}

	@Override
	public void execute() {
		script.walking.webWalk(IaoxIntelligence.getMiningAssignment().getMiningArea());
	}

	@Override
	public String toString() {
		return "Walking to mining location...";
	}

	@Override
	public boolean safeToInterrupt() {
		//we do not want IaoxIntelligence to interrupt the script when we are walking
		return false;
	}

}
