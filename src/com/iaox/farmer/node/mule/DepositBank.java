package com.iaox.farmer.node.mule;

import org.osbot.rs07.api.Bank.BankMode;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.node.Node;

public class DepositBank extends Node {

	@Override
	public boolean active() {
		return IaoxAIO.shouldTrade && !GrandExchangeData.CURRENT_SELLABLE_ITEMS.isEmpty() && muleMethods.playerInGe();
	}

	@Override
	public void execute() {
		if (!methodProvider.bank.isOpen()) {
			try {
				methodProvider.bank.open();
				new ConditionalSleep(5000) {
					@Override
					public boolean condition() throws InterruptedException {
						sleeps(300);
						return methodProvider.bank.isOpen();
					}
				}.sleep();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (methodProvider.bank.contains(995)) {
			methodProvider.bank.withdrawAll(995);
			new ConditionalSleep(5000) {
				@Override
				public boolean condition() throws InterruptedException {
					sleeps(300);
					return !methodProvider.bank.contains(995);
				}
			}.sleep();
		} else if (!GrandExchangeData.CURRENT_SELLABLE_ITEMS.isEmpty()) {
			for (IaoxItem item : GrandExchangeData.CURRENT_SELLABLE_ITEMS) {
				if (!methodProvider.bank.contains(item.getName())) {
					GrandExchangeData.CURRENT_SELLABLE_ITEMS.remove(item);
				} else if (methodProvider.bank.getWithdrawMode() != BankMode.WITHDRAW_NOTE) {
					methodProvider.bank.enableMode(BankMode.WITHDRAW_NOTE);
					new ConditionalSleep(5000) {
						@Override
						public boolean condition() throws InterruptedException {
							sleeps(300);
							return methodProvider.bank.getInsertMode() == BankMode.WITHDRAW_NOTE;
						}
					}.sleep();
				} else {
					methodProvider.bank.withdrawAll(item.getName());
					GrandExchangeData.SHOULD_COLLECT = true;
					new ConditionalSleep(5000) {
						@Override
						public boolean condition() throws InterruptedException {
							sleeps(300);
							return !methodProvider.bank.contains(item.getName());
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
		return "Deposit items to mule";
	}

}
