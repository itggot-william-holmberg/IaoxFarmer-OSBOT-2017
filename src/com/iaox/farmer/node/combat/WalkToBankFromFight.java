package com.iaox.farmer.node.combat;

import com.iaox.farmer.node.Node;

public class WalkToBankFromFight extends Node {

	@Override
	public boolean active() {
		if (!combatMethods.playerInBankArea() && !combatMethods.playerIsReadyForFight()) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		switch (combatMethods.getAssignment()) {
		default:
			walkingMethods.webWalk(combatMethods.getBankArea());
			break;
		}
	}
	
	public String toString() {
		return "WALK TO BANK FROM FIGHT";
	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}

}
