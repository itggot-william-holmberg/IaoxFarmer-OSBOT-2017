package com.iaox.farmer.node.grandexchange;

import org.osbot.rs07.api.Bank.BankMode;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.node.Node;

public class BuyItem extends Node {

	@Override
	public boolean active() {
		return geMethods.playerInGeArea() && GrandExchangeData.CURRENT_SELLABLE_ITEMS.isEmpty();
	}

	@Override
	public void execute() {
		if (!methodProvider.grandExchange.isOpen()) {
			geMethods.openGE();
		} else if(geMethods.inventoryContains(GrandExchangeData.DEFAULT_SELLABLE_ITEMS)){
			geMethods.sellItems();
		} 
		else if (!GrandExchangeData.ITEMS_TO_BUY_LIST.isEmpty()) {
			geMethods.buyItems();
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
