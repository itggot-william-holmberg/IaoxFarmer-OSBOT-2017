package com.iaox.farmer.node.mule;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.node.Node;

public class Deposit extends Node {

	@Override
	public boolean active() {
		return IaoxAIO.shouldTrade && GrandExchangeData.CURRENT_SELLABLE_ITEMS.isEmpty() && 
				!geMethods.inventoryContains(GrandExchangeData.DEFAULT_SELLABLE_ITEMS) && 
				muleMethods.playerInGe() && !GrandExchangeData.SHOULD_COLLECT;
	}

	@Override
	public void execute() {
		if (!script.trade.isCurrentlyTrading() && !script.inventory.contains(995)) {
			script.log("we are done!");
			IaoxAIO.shouldTrade = false;
			return;
		}

		script.log("lets trade:" + IaoxAIO.muleThread.getMule());

		if (!script.trade.isCurrentlyTrading()) {
			muleMethods.tradePlayer();
		} else if (script.trade.isFirstInterfaceOpen()) {
			if (script.inventory.contains(995)) {
				script.trade.offer(995, (int) script.inventory.getAmount(995));
			} else if (script.trade.didOtherAcceptTrade()) {
				script.trade.acceptTrade();
			}
		} else if (script.trade.isSecondInterfaceOpen()) {
			muleMethods.acceptSecondTradeInterface();
		}

	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}

	@Override
	public String toString() {
		return "Deposit items to mule";
	}

}
