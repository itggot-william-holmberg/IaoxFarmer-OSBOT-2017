package com.iaox.farmer.node.fishing;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.mining.MiningAreas;
import com.iaox.farmer.data.items.IaoxItem;
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
		} else if (!fishingMethods.playerHasFishingGear()) {
			for (IaoxItem item : fishingMethods.getAssignment().getInventoryItems()) {
				if (!script.inventory.contains(item.getID())) {
					switch (item) {
					case FEATHER:
						bankingMethods.withdraw(1000 + IaoxAIO.random(1000), item.getID());
						break;
					default:
						bankingMethods.withdraw(1, item.getID());
						break;

					}
				}
			}
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
