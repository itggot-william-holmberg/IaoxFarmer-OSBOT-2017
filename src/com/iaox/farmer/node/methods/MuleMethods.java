package com.iaox.farmer.node.methods;

import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.Areas;

public class MuleMethods {

	private MethodProvider methodProvider;
	
	public MuleMethods(MethodProvider methodProvider){
		 this.methodProvider = methodProvider;
	}
	
	public boolean playerInGe(){
		return Areas.GRAND_EXCHANGE_AREA.contains(methodProvider.myPlayer());
	}
	
	public void tradePlayer() {
		Player player = methodProvider.players.closest(p -> p.getName().equals(IaoxAIO.muleThread.getMule()));
		if (player != null) {
			methodProvider.log("player exists, lets trade");
			player.interact("Trade with");
			new ConditionalSleep(10000) {
				@Override
				public boolean condition() throws InterruptedException {
					return methodProvider.trade.isCurrentlyTrading();
				}

			}.sleep();
		}
	}
	
	public void acceptSecondTradeInterface() {

		if (methodProvider.trade.didOtherAcceptTrade()) {
			methodProvider.trade.acceptTrade();
			new ConditionalSleep(10000) {
				@Override
				public boolean condition() throws InterruptedException {
					return methodProvider.trade.isCurrentlyTrading();
				}
			}.sleep();
		}

		if (!methodProvider.trade.isCurrentlyTrading() && !methodProvider.inventory.contains(995)) {
			IaoxAIO.shouldTrade = false;
		}
	}
	
	
}
