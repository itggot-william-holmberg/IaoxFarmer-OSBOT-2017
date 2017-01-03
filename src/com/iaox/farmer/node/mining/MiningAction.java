package com.iaox.farmer.node.mining;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.node.Node;

public class MiningAction extends Node{

	@Override
	public boolean active() {
		return readyToMine() && playerInArea(IaoxIntelligence.getMiningAssignment().getMiningArea());
	}

	@Override
	public void execute() {
		script.log("lets mine");
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "Mining...";
	}

}
