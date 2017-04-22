package com.iaox.farmer.node.fishing;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.ai.skills.IntelligentWoodcutting;
import com.iaox.farmer.node.Node;

public class FishingAction extends Node{

	@Override
	public boolean active() {
		return fishingMethods.readyToFish() && fishingMethods.playerInArea(IaoxIntelligence.getFishingAssignment().getFishingArea());
	}

	@Override
	public void execute() {
		checkContinue();
		if (!methodProvider.myPlayer().isAnimating()) {
			fishingMethods.fish();
		} else {
			fishingMethods.fishSleep();
		}
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "Fish";
	}


}
