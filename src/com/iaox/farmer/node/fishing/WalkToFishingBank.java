package com.iaox.farmer.node.fishing;

import org.osbot.rs07.api.map.constants.Banks;

import com.iaox.farmer.node.Node;

public class WalkToFishingBank extends Node{

	@Override
	public boolean active() {
		return !fishingMethods.readyToFish() && !fishingMethods.playerInBankArea();
	}

	@Override
	public void execute() {
		walkingMethods.webWalk(fishingMethods.getBankArea());		
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
