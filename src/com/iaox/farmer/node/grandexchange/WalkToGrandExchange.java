package com.iaox.farmer.node.grandexchange;

import com.iaox.farmer.data.Areas;
import com.iaox.farmer.node.Node;

public class WalkToGrandExchange extends Node{

	@Override
	public boolean active() {
		return !geMethods.playerInGeArea();
	}

	@Override
	public void execute() {
		walkingMethods.webWalkGE(Areas.GRAND_EXCHANGE_AREA);
	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}

	@Override
	public String toString() {
		return "Walking to the Grand Exchange";
	}

}
