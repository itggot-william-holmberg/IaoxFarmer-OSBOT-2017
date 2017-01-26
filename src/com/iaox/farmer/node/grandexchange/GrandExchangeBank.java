package com.iaox.farmer.node.grandexchange;

import org.osbot.rs07.api.Bank.BankMode;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.node.Node;

public class GrandExchangeBank extends Node {

	@Override
	public boolean active() {
		return geMethods.playerInGeArea() && !GrandExchangeData.CURRENT_SELLABLE_ITEMS.isEmpty();
	}

	@Override
	public void execute() {
		if (!script.bank.isOpen()) {
			try {
				script.bank.open();
				new ConditionalSleep(5000) {
					@Override
					public boolean condition() throws InterruptedException {
						sleeps(300);
						return script.bank.isOpen();
					}
				}.sleep();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (script.bank.contains(995)) {
			script.bank.withdrawAll(995);
			new ConditionalSleep(5000) {
				@Override
				public boolean condition() throws InterruptedException {
					sleeps(300);
					return !script.bank.contains(995);
				}
			}.sleep();
		} else if (!GrandExchangeData.CURRENT_SELLABLE_ITEMS.isEmpty()) {
			for (IaoxItem item : GrandExchangeData.CURRENT_SELLABLE_ITEMS) {
				if(script.inventory.isFull()){
					script.bank.depositAll();
					sleeps(4000);
				}
				if (!script.bank.contains(item.getName())) {
					GrandExchangeData.CURRENT_SELLABLE_ITEMS.remove(item);
				} else if (script.bank.getWithdrawMode() != BankMode.WITHDRAW_NOTE) {
					script.bank.enableMode(BankMode.WITHDRAW_NOTE);
					new ConditionalSleep(5000) {
						@Override
						public boolean condition() throws InterruptedException {
							sleeps(300);
							return script.bank.getInsertMode() == BankMode.WITHDRAW_NOTE;
						}
					}.sleep();
				} else {
					script.bank.withdrawAll(item.getName());
					new ConditionalSleep(5000) {
						@Override
						public boolean condition() throws InterruptedException {
							sleeps(300);
							return !script.bank.contains(item.getName());
						}
					}.sleep();
				}
			}
		}
	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}

	@Override
	public String toString() {
		return "Banking in GE";
	}

}
