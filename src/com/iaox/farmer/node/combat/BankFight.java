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
		if (!Data.WITHDRAW_LIST.isEmpty()) {
			combatMethods.withdrawNeededItems();
		} else if(script.inventory.getEmptySlots() < 5){
			bankingMethods.depositAll();
		} else if(script.inventory.contains("coins")){
			bankingMethods.depositAll("Coins");
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