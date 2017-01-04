package com.iaox.farmer.node.mining;

import org.osbot.rs07.api.map.constants.Banks;

import com.iaox.farmer.node.Node;

public class WalkToMiningBank extends Node{

	@Override
	public boolean active() {
		return !miningMethods.readyToMine() && !miningMethods.playerInArea(Banks.DRAYNOR);
	}

	@Override
	public void execute() {
		walkingMethods.webWalk(Banks.DRAYNOR);		
	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}

	@Override
	public String toString() {
		return "Walking to bank";
	}

}
