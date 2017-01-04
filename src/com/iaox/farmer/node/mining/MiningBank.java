package com.iaox.farmer.node.mining;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.node.Node;

public class MiningBank extends Node{

	@Override
	public boolean active() {
		return !miningMethods.readyToMine() && miningMethods.playerInBankArea();
	}

	@Override
	public void execute() {
		switch(miningMethods.getAssignment()){
		case IRON_ORE_RIMMINGTON:
			depositBoxBanking();
			break;
		default:
			defaultBanking();
			break;
		
		}
	}

	private void depositBoxBanking() {
		if(script.inventory.isFull()){
			bankingMethods.depositBoxDepositAll();
		}	
	}

	private void defaultBanking() {
		if(script.inventory.isFull()){
			bankingMethods.depositAll();
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
