package com.iaox.farmer.node.crafting;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.script.MethodProvider;

import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.node.Node;


public class CraftingWalkToBank extends Node {

	@Override
	public boolean active() {
		return !craftingProvider.hasRequiredInventoryItems() && !craftingProvider.inBankArea();
	}

	@Override
	public void execute() {
		methodProvider.walking.webWalk(craftingProvider.getBankArea());
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "WALK_TO_CRAFTING_BANK";
	}

}
