package com.iaox.farmer.node.mining;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.mining.MiningAreas;
import com.iaox.farmer.node.Node;

public class MiningBank extends Node {

	@Override
	public boolean active() {
		return !miningMethods.readyToMine() && miningMethods.playerInBankArea();
	}

	@Override
	public void execute() {
		switch (miningMethods.getAssignment()) {
		case IRON_ORE_RIMMINGTON:
		case IRON_ORE_RIMMINGTON_2:
		case TIN_ORE_RIMMINGTON:
		case GOLD_ORE_RIMMINGTON:
			if (miningMethods.playerInArea(MiningAreas.PORT_SARIM_DEPOSIT_AREA)) {
				depositBoxBanking();
			}else{
				defaultBanking();
			}
			break;
		default:
			defaultBanking();
			break;

		}
	}

	private void depositBoxBanking() {
		if (script.inventory.isFull()) {
			bankingMethods.depositBoxDepositAllExcept(miningMethods.getPickaxe());
		}
	}

	private void defaultBanking() {
		if (script.inventory.isFull()) {
			bankingMethods.depositAll();
		}else if(!miningMethods.playerHasPickaxe()){
			bankingMethods.withdraw(miningMethods.getPickaxe());
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
