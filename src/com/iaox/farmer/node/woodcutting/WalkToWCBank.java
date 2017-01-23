package com.iaox.farmer.node.woodcutting;

import org.osbot.rs07.api.map.constants.Banks;

import com.iaox.farmer.node.Node;

public class WalkToWCBank extends Node{

	@Override
	public boolean active() {
		return !wcMethods.readyToCut() && !wcMethods.playerInBankArea();
	}

	@Override
	public void execute() {
		walkingMethods.webWalk(wcMethods.getBankArea());		
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
