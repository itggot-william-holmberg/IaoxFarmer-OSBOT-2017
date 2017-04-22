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
		if (!methodProvider.trade.isCurrentlyTrading() && !methodProvider.inventory.contains(995)) {
			methodProvider.log("we are done!");
			IaoxAIO.shouldTrade = false;
			return;
		}

		methodProvider.log("lets trade:" + IaoxAIO.muleThread.getMule());

		if (!methodProvider.trade.isCurrentlyTrading()) {
			muleMethods.tradePlayer();
		} else if (methodProvider.trade.isFirstInterfaceOpen()) {
			if (methodProvider.inventory.contains(995)) {
				methodProvider.trade.offer(995, (int) methodProvider.inventory.getAmount(995));
			} else if (methodProvider.trade.didOtherAcceptTrade()) {
				methodProvider.trade.acceptTrade();
			}
		} else if (methodProvider.trade.isSecondInterfaceOpen()) {
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
