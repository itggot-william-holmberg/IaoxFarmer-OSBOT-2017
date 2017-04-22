package com.iaox.farmer.node.combat;

import java.util.Arrays;

import com.iaox.farmer.data.Data;
import com.iaox.farmer.node.Node;

public class BankFight extends Node {

	public boolean active() {
		if (combatMethods.playerInBankArea() && !combatMethods.playerIsReadyForFight()) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		if (methodProvider.inventory.getEmptySlots() < 28-combatMethods.getFoodAmount()-1) {
			bankProvider.depositAll();
		} else if (methodProvider.inventory.contains("coins")) {
			bankProvider.depositAll("Coins");
		} else if (!Data.WITHDRAW_LIST.isEmpty()) {
			combatMethods.withdrawNeededItems();
		}
	}

	public String toString() {
		return "Banking --fight";
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

}
