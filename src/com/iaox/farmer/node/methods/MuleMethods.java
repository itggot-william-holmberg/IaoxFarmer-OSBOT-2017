package com.iaox.farmer.node.methods;

import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.Areas;

public class MuleMethods {

	private Script script;
	
	public MuleMethods(Script script){
		 this.script = script;
	}
	
	public boolean playerInGe(){
		return Areas.GRAND_EXCHANGE_AREA.contains(script.myPlayer());
	}
	
	public void tradePlayer() {
		Player player = script.players.closest(p -> p.getName().equals(IaoxAIO.muleThread.getMule()));
		if (player != null) {
			script.log("player exists, lets trade");
			player.interact("Trade with");
			new ConditionalSleep(10000) {
				@Override
				public boolean condition() throws InterruptedException {
					return script.trade.isCurrentlyTrading();
				}

			}.sleep();
		}
	}
	
	public void acceptSecondTradeInterface() {

		if (script.trade.didOtherAcceptTrade()) {
			script.trade.acceptTrade();
			new ConditionalSleep(10000) {
				@Override
				public boolean condition() throws InterruptedException {
					return script.trade.isCurrentlyTrading();
				}
			}.sleep();
		}

		if (!script.trade.isCurrentlyTrading() && !script.inventory.contains(995)) {
			IaoxAIO.shouldTrade = false;
		}
	}
	
	
}
