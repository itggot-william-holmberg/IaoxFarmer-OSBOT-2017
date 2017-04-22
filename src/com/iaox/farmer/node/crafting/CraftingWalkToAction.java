package com.iaox.farmer.node.crafting;

import org.osbot.rs07.script.MethodProvider;

import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.node.Node;


public class CraftingWalkToAction extends Node {


	@Override
	public boolean active() {
		return craftingProvider.hasExactAmountOfRequiredInventoryItems() || (craftingProvider.hasRequiredInventoryItems() && !craftingProvider.inBankArea())  && !craftingProvider.inActionArea();
	}

	@Override
	public void execute() {
		methodProvider.walking.webWalk(craftingProvider.getActionArea());
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "WALK_TO_ACTION";
	}

}
