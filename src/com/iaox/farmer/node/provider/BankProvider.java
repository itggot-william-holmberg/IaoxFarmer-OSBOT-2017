package com.iaox.farmer.node.provider;

import java.util.List;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxInventoryItem;
import com.iaox.farmer.data.items.IaoxItem;

public class BankProvider extends IaoxProvider {

	public BankProvider(MethodProvider methodProvider) {
		super(methodProvider);
	}

	public void depositAll() {
		if (methodProvider.bank.isOpen()) {
			methodProvider.bank.depositAll();
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return methodProvider.inventory.isEmpty();
				}
			}.sleep();
		} else {
			try {
				methodProvider.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void withdrawExact(int amount, int itemID) {
		if (methodProvider.bank.isOpen()) {
			int currentAmount = (int) methodProvider.inventory.getAmount(itemID);
			if (currentAmount > amount) {
				methodProvider.bank.deposit(itemID, currentAmount - amount);
			} else if (currentAmount > 0) {
				withdraw(amount - currentAmount, itemID);
			} else {
				withdraw(amount, itemID);
			}
			new ConditionalSleep(3000, 600) {

				@Override
				public boolean condition() throws InterruptedException {
					return methodProvider.inventory.getAmount(itemID) >= amount;
				}

			}.sleep();
		} else {
			openBank();
		}
	}

	public Item itemToDeposit(List<IaoxInventoryItem> list) {
		Item[] items = methodProvider.inventory.getItems();
		if (items != null) {
			for (Item item : items) {
				if (item != null && getItemIDs(list) != null && !getItemIDs(list).contains(item.getId())) {
					return item;
				}
			}
		}
		return null;
	}

	public IaoxInventoryItem itemToWithdraw(List<IaoxInventoryItem> list, boolean exactAmount) {
		if (exactAmount) {
			for (IaoxInventoryItem item : list) {
				if (item != null && methodProvider.inventory.getAmount(item.getItem().getID()) != item.getAmount()) {
					return item;
				}
			}
		} else {
			return itemToWithdraw(list);
		}
		return null;
	}

	public IaoxInventoryItem itemToWithdraw(List<IaoxInventoryItem> list) {
		Item[] items = methodProvider.inventory.getItems();
		for (IaoxInventoryItem item : list) {
			if (item != null && methodProvider.inventory.getAmount(item.getItem().getID()) != item.getAmount()) {
				return item;
			}
		}
		return null;
	}

	private void openBank() {
		try {
			methodProvider.bank.open();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void withdrawExact(IaoxInventoryItem item) {
		if (item == null) {
			methodProvider.log("We ran out of item: ");
			try {
				methodProvider.getBot().getScriptExecutor().stop();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (methodProvider.bank.isOpen()) {
			int amount = item.getAmount();
			int itemID = item.getItem().getID();
			int currentAmount = (int) methodProvider.inventory.getAmount(itemID);
			if (currentAmount > amount) {
				methodProvider.bank.deposit(itemID, currentAmount - amount);
			} else if (currentAmount > 0) {
				methodProvider.log("lets not withdraw all");
				withdraw(amount - currentAmount, itemID);
			} else {
				withdraw(amount, itemID);
			}
			new ConditionalSleep(3000, 600) {

				@Override
				public boolean condition() throws InterruptedException {
					return methodProvider.inventory.getAmount(itemID) >= amount;
				}

			}.sleep();
		} else {
			openBank();
		}
	}

	public void withdraw(String itemName) {
		if (!methodProvider.bank.isOpen()) {
			try {
				methodProvider.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (methodProvider.inventory.isFull()) {
			methodProvider.bank.depositAll();
		} else if (methodProvider.bank.contains(itemName)) {
			methodProvider.bank.withdraw(itemName, 1);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return methodProvider.inventory.contains(itemName);
				}
			}.sleep();
		} else {
			methodProvider.log("We do not have the required item: " + itemName);
			IaoxItem item = IaoxItem.getItem(itemName);
			if (item != null) {
				GrandExchangeData.ITEMS_TO_BUY_LIST.add(IaoxItem.getItem(itemName));
			} else {
				methodProvider.log("The required item: " + itemName + " does not exist in the Item database");
				try {
					methodProvider.getBot().getScriptExecutor().stop();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void withdraw(List<IaoxItem> items) {
		for (IaoxItem item : items) {
			if (!methodProvider.inventory.contains(item.getID())) {
				withdraw(1, item.getID());
			}
		}
	}

	public void withdraw(int amount, String itemName) {
		if (!methodProvider.bank.isOpen()) {
			try {
				methodProvider.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (methodProvider.inventory.isFull()) {
			methodProvider.bank.depositAll();
		} else if (methodProvider.bank.contains(itemName)) {
			methodProvider.bank.withdraw(itemName, amount);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return methodProvider.inventory.contains(itemName);
				}
			}.sleep();
		} else {
			methodProvider.log("We do not have the required item: " + itemName);
			IaoxItem item = IaoxItem.getItem(itemName);
			if (item != null) {
				GrandExchangeData.ITEMS_TO_BUY_LIST.add(IaoxItem.getItem(itemName));
			} else {
				methodProvider.log("The required item: " + itemName + " does not exist in the Item database");
				try {
					methodProvider.getBot().getScriptExecutor().stop();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void withdraw(int amount, int itemID) {
		if (!methodProvider.bank.isOpen()) {
			try {
				methodProvider.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (methodProvider.inventory.isFull()) {
			methodProvider.bank.depositAll();
		} else if (methodProvider.bank.contains(itemID)) {
			methodProvider.bank.withdraw(itemID, amount);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return methodProvider.inventory.contains(itemID);
				}
			}.sleep();
		} else {
			methodProvider.log("We do not have the required item: " + itemID);
			IaoxItem item = IaoxItem.getItem(itemID);
			if (item != null) {
				GrandExchangeData.ITEMS_TO_BUY_LIST.add(IaoxItem.getItem(itemID));
			} else {
				methodProvider.log("The required item: " + itemID + " does not exist in the Item database");
				try {
					methodProvider.getBot().getScriptExecutor().stop();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void depositBoxDepositAllExcept(String item) {
		if (methodProvider.depositBox.isOpen()) {
			methodProvider.depositBox.depositAllExcept(item);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return methodProvider.inventory.isEmpty();
				}
			}.sleep();
		} else {
			methodProvider.depositBox.open();
		}

	}

	public void depositBoxDepositAllExcept(List<IaoxItem> items) {
		for (IaoxItem item : items) {
			if (methodProvider.depositBox.isOpen()) {
				methodProvider.depositBox.depositAllExcept(item.getID());
				new ConditionalSleep(4000) {
					@Override
					public boolean condition() throws InterruptedException {
						IaoxAIO.sleep(300);
						return methodProvider.inventory.isEmpty();
					}
				}.sleep();
			} else {
				methodProvider.depositBox.open();
			}
		}

	}

	public void depositBoxDepositAll() {
		if (methodProvider.depositBox.isOpen()) {
			methodProvider.depositBox.depositAll();
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return methodProvider.inventory.isEmpty();
				}
			}.sleep();
		} else {
			methodProvider.depositBox.open();
		}

	}

	public void depositAll(String string) {
		if (methodProvider.bank.isOpen()) {
			methodProvider.bank.depositAll(string);
			new ConditionalSleep(4000) {
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return methodProvider.inventory.isEmpty();
				}
			}.sleep();
		} else {
			try {
				methodProvider.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
