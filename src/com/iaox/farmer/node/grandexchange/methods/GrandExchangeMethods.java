package com.iaox.farmer.node.grandexchange.methods;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.Areas;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxItem;

public class GrandExchangeMethods {

	private Script script;
	private GrandExchangePriceAPI gePrice;

	public GrandExchangeMethods(Script script) {
		this.script = script;
		this.gePrice = new GrandExchangePriceAPI();
	}

	public boolean playerInGeArea() {
		return Areas.GRAND_EXCHANGE_AREA.contains(script.myPosition());
	}

	public void openGE() {
		RS2Object geBooth = script.getObjects().closest("Grand Exchange booth");
		NPC exchangeWorker = script.getNpcs().closest("Grand Exchange Clerk");

		int random = new Random().nextInt(10);
		if (geBooth != null && random < 5) {
			geBooth.interact("Exchange");
			new ConditionalSleep(2500, 3000) {
				@Override
				public boolean condition() {
					return script.getGrandExchange().isOpen();
				}
			}.sleep();
		}

		if (exchangeWorker != null && random >= 5) {
			exchangeWorker.interact("Exchange");
			new ConditionalSleep(2500, 3000) {

				@Override
				public boolean condition() {
					return script.getGrandExchange().isOpen();
				}

			}.sleep();
		}

	}

	public void buyItems() {
		for (IaoxItem item : GrandExchangeData.ITEMS_TO_BUY_LIST) {
			if (collectButtonIsVisible()) {
				int emptySlots = script.inventory.getEmptySlotCount();
				script.grandExchange.collect();
				new ConditionalSleep(2500, 3000) {

					@Override
					public boolean condition() {
						return script.inventory.getEmptySlotCount() > emptySlots;
					}

				}.sleep();
			} else if (script.inventory.contains(item.getName())) {
				GrandExchangeData.ITEMS_TO_BUY_LIST.remove(item);
			} else if (script.inventory.getAmount(995) >= getItemPrice(item)) {
				script.grandExchange.buyItem(item.getID(), item.getName(), getItemPrice(item), 1);
				new ConditionalSleep(2500, 3000) {

					@Override
					public boolean condition() {
						return collectButtonIsVisible();
					}
				}.sleep();
			} else {
				script.log("lets call mule");
				// call mule
				IaoxAIO.shouldTrade = true;
				IaoxAIO.coinsNeeded = getItemPrice(item);
				break;
			}
		}
	}

	public boolean inventoryContains(List<IaoxItem> potentialItems) {
		for (IaoxItem item : potentialItems) {
			if (script.inventory.contains(item.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean collectButtonIsVisible() {
		return script.widgets.isVisible(465, 6, 1);
	}

	public int getItemPrice(IaoxItem item) {
		switch (item) {
		case MITHRIL_PICKAXE:
			return 2000 + IaoxAIO.random(500);
		default:
			int overallPrice = 1000;
			try {
				overallPrice = gePrice.getOverallPrice(item.getID());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (int) (overallPrice * 1.3);
		}
	}

	public void sellItems() {
		for (IaoxItem item : GrandExchangeData.DEFAULT_SELLABLE_ITEMS) {
			if (collectButtonIsVisible()) {
				int amountOfCash = (int) script.inventory.getAmount(995);
				script.grandExchange.collect();
				new ConditionalSleep(5000,10000) {

					@Override
					public boolean condition() {
						return script.inventory.getAmount(995) > amountOfCash;
					}
				}.sleep();
				GrandExchangeData.SHOULD_COLLECT = false;
			} else {
				script.grandExchange.sellItem(getItemID(item), IaoxAIO.random(10, 20),
						(int) script.inventory.getAmount(item.getName()));
				new ConditionalSleep(2500, 3000) {

					@Override
					public boolean condition() {
						return collectButtonIsVisible();
					}
				}.sleep();
			}
		}

	}

	private int getItemID(IaoxItem item) {
		if (script.inventory.getItem(item.getName()).isNote()) {
			return item.getID() + 1;
		}
		return item.getID();
	}
}
