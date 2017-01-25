package com.iaox.farmer.node.methods;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxItem;

public class BankingMethods {

	private Script script;

	public BankingMethods(Script script) {
		this.script = script;
	}

	public void depositAll() {
		if (script.bank.isOpen()) {
			script.bank.depositAll();
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return script.inventory.isEmpty();
				}
			}.sleep();
		} else {
			try {
				script.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void withdraw(String itemName) {
		if (!script.bank.isOpen()) {
			try {
				script.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (script.inventory.isFull()) {
			script.bank.depositAll();
		} else if (script.bank.contains(itemName)) {
			script.bank.withdraw(itemName, 1);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return script.inventory.contains(itemName);
				}
			}.sleep();
		} else {
			script.log("We do not have the required item: " + itemName);
			IaoxItem item = IaoxItem.getItem(itemName);
			if (item != null) {
				GrandExchangeData.ITEMS_TO_BUY_LIST.add(IaoxItem.getItem(itemName));
			} else {
				script.log("The required item: " + itemName + " does not exist in the Item database");
				script.stop();
				script.stop();
			}
		}
	}
	
	public void withdraw(int amount, String itemName) {
		if (!script.bank.isOpen()) {
			try {
				script.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (script.inventory.isFull()) {
			script.bank.depositAll();
		} else if (script.bank.contains(itemName)) {
			script.bank.withdraw(itemName, amount);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return script.inventory.contains(itemName);
				}
			}.sleep();
		} else {
			script.log("We do not have the required item: " + itemName);
			IaoxItem item = IaoxItem.getItem(itemName);
			if (item != null) {
				GrandExchangeData.ITEMS_TO_BUY_LIST.add(IaoxItem.getItem(itemName));
			} else {
				script.log("The required item: " + itemName + " does not exist in the Item database");
				script.stop();
				script.stop();
			}
		}
	}

	public void depositBoxDepositAllExcept(String item) {
		if (script.depositBox.isOpen()) {
			script.depositBox.depositAllExcept(item);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return script.inventory.isEmpty();
				}
			}.sleep();
		} else {
			script.depositBox.open();
		}

	}

	public void depositBoxDepositAll() {
		if (script.depositBox.isOpen()) {
			script.depositBox.depositAll();
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return script.inventory.isEmpty();
				}
			}.sleep();
		} else {
			script.depositBox.open();
		}

	}

	public void depositAll(String string) {
		if (script.bank.isOpen()) {
			script.bank.depositAll(string);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return script.inventory.isEmpty();
				}
			}.sleep();
		} else {
			try {
				script.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
