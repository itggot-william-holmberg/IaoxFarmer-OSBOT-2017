package com.iaox.farmer.node.combat;

import com.iaox.farmer.node.Node;

public class WalkToFight extends Node {

	@Override
	public boolean active() {
		if (!combatMethods.playerInFightArea() && combatMethods.playerIsReadyForFight()) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		switch (combatMethods.getAssignment()) {
		default:
			walkingMethods.webWalk(combatMethods.getAssignment().getFightArea());
			break;
		}
	}


	public String toString() {
		return "WALK TO FIGHT";
	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}
}
