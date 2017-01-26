package com.iaox.farmer.node.fishing;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.mining.MiningAreas;
import com.iaox.farmer.node.Node;

public class FishingBank extends Node {

	@Override
	public boolean active() {
		return !fishingMethods.readyToFish() && fishingMethods.playerInBankArea();
	}

	@Override
	public void execute() {
		switch (fishingMethods.getAssignment()) {
		default:
			defaultBanking();
			break;

		}
	}

	private void depositBoxBanking() {
		if (script.inventory.isFull()) {
			bankingMethods.depositBoxDepositAllExcept(fishingMethods.getAssignment().getInventoryItems());
		}
	}

	private void defaultBanking() {
		if (script.inventory.isFull()) {
			bankingMethods.depositAll();
		}else if(!fishingMethods.playerHasFishingGear()){
			bankingMethods.withdraw(fishingMethods.getAssignment().getInventoryItems());
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
