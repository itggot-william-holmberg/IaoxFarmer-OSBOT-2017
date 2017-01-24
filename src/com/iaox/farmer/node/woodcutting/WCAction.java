package com.iaox.farmer.node.woodcutting;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.ai.IntelligentWoodcutting;
import com.iaox.farmer.node.Node;

public class WCAction extends Node{

	@Override
	public boolean active() {
		return wcMethods.readyToCut() && wcMethods.playerInArea(IaoxIntelligence.getWCAssignment().getWCArea());
	}

	@Override
	public void execute() {
		checkContinue();
		if (!script.myPlayer().isAnimating()) {
			wcMethods.cut();
		} else {
			wcMethods.cutSleep();
		}
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "Woodcut";
	}


}
