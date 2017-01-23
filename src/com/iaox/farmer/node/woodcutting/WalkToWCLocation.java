package com.iaox.farmer.node.woodcutting;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.node.Node;

public class WalkToWCLocation extends Node{

	@Override
	public boolean active() {
		return wcMethods.readyToCut() && !wcMethods.playerInArea(IaoxIntelligence.getWCAssignment().getWCArea());
	}

	@Override
	public void execute() {
		walkingMethods.webWalk(IaoxIntelligence.getWCAssignment().getWCArea());
	}
	
	@Override
	public boolean safeToInterrupt() {
		//we do not want IaoxIntelligence to interrupt the script when we are walking
		return false;
	}
	
	@Override
	public String toString() {
		return "Walking to wc location";
	}


}
