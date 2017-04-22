package com.iaox.farmer.node.combat;

import com.iaox.farmer.IaoxAIO;
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
		case CHAOS_DRUIDS_TAVERLEY:
			if(methodProvider.myPosition().getY() > 9000 && methodProvider.inventory.contains("Falador teleport")){
				methodProvider.inventory.interact("Break",  "Falador teleport");
				sleeps(5000 + IaoxAIO.random(1000));
			}else{
				walkingMethods.webWalk(combatMethods.getBankArea());
			}
			break;
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
