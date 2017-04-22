package com.iaox.farmer.node.woodcutting;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.mining.MiningAreas;
import com.iaox.farmer.node.Node;

public class WCBank extends Node {

	@Override
	public boolean active() {
		return !wcMethods.readyToCut() && wcMethods.playerInBankArea();
	}

	@Override
	public void execute() {
		switch (wcMethods.getAssignment()) {
		default:
			defaultBanking();
			break;

		}
	}

	private void depositBoxBanking() {
		if (methodProvider.inventory.isFull()) {
			bankProvider.depositBoxDepositAllExcept(wcMethods.getAxe());
		}
	}

	private void defaultBanking() {
		if (methodProvider.inventory.isFull()) {
			bankProvider.depositAll();
		}else if(!wcMethods.playerHasAxe()){
			bankProvider.withdraw(wcMethods.getAxe());
		}
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "Banking";
	}

}
