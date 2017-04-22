package com.iaox.farmer.node.mule;

import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.node.Node;

public class SellItems extends Node {

	@Override
	public boolean active() {
		return geMethods.playerInGeArea() && GrandExchangeData.CURRENT_SELLABLE_ITEMS.isEmpty()
				&& (geMethods.inventoryContains(GrandExchangeData.DEFAULT_SELLABLE_ITEMS)
						|| GrandExchangeData.SHOULD_COLLECT);
	}

	@Override
	public void execute() {
		if (!methodProvider.grandExchange.isOpen()) {
			geMethods.openGE();
		} else if (geMethods.collectButtonIsVisible()) {
			int amountOfCash = (int) methodProvider.inventory.getAmount(995);
			methodProvider.grandExchange.collect();
			new ConditionalSleep(5000, 10000) {

				@Override
				public boolean condition() {
					return methodProvider.inventory.getAmount(995) > amountOfCash;
				}
			}.sleep();
			GrandExchangeData.SHOULD_COLLECT = false;
		} else if (geMethods.inventoryContains(GrandExchangeData.DEFAULT_SELLABLE_ITEMS)) {
			geMethods.sellItems();
		}
	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}

	@Override
	public String toString() {
		return "Buying items in ge";
	}

}
