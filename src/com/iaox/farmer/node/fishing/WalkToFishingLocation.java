package com.iaox.farmer.node.fishing;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.node.Node;

public class WalkToFishingLocation extends Node{

	@Override
	public boolean active() {
		return fishingMethods.readyToFish() && !fishingMethods.playerInArea(IaoxIntelligence.getFishingAssignment().getFishingArea());
	}

	@Override
	public void execute() {
		walkingMethods.webWalk(IaoxIntelligence.getFishingAssignment().getFishingArea());
	}
	
	@Override
	public boolean safeToInterrupt() {
		//we do not want IaoxIntelligence to interrupt the script when we are walking
		return false;
	}
	
	@Override
	public String toString() {
		return "Walking to fishing location";
	}


}
