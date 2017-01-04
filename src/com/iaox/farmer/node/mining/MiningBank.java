package com.iaox.farmer.node.mining;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.node.Node;

public class MiningBank extends Node{

	@Override
	public boolean active() {
		return !miningMethods.readyToMine() && miningMethods.playerInArea(Banks.DRAYNOR);
	}

	@Override
	public void execute() {
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
