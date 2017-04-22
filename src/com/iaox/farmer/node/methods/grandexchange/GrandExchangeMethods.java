package com.iaox.farmer.node.methods.grandexchange;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.data.Areas;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.data.GrandExchangeData;
import com.iaox.farmer.data.items.IaoxItem;

public class GrandExchangeMethods {

	private MethodProvider methodProvider;
	private GrandExchangePriceAPI gePrice;

	public GrandExchangeMethods(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		this.gePrice = new GrandExchangePriceAPI();
	}

	public boolean playerInGeArea() {
		return Areas.GRAND_EXCHANGE_AREA.contains(methodProvider.myPosition());
	}

	public void openGE() {
		RS2Object geBooth = methodProvider.getObjects().closest("Grand Exchange booth");
		NPC exchangeWorker = methodProvider.getNpcs().closest("Grand Exchange Clerk");

		int random = new Random().nextInt(10);
		if (geBooth != null && random < 5) {
			geBooth.interact("Exchange");
			new ConditionalSleep(2500, 3000) {
				@Override
				public boolean condition() {
					return methodProvider.getGrandExchange().isOpen();
				}
			}.sleep();
		}

		if (exchangeWorker != null && random >= 5) {
			exchangeWorker.interact("Exchange");
			new ConditionalSleep(2500, 3000) {

				@Override
				public boolean condition() {
					return methodProvider.getGrandExchange().isOpen();
				}

			}.sleep();
		}

	}

	public void buyItems() {
		for (IaoxItem item : GrandExchangeData.ITEMS_TO_BUY_LIST) {
			if (collectButtonIsVisible()) {
				int emptySlots = methodProvider.inventory.getEmptySlotCount();
				methodProvider.grandExchange.collect();
				new ConditionalSleep(2500, 3000) {

					@Override
					public boolean condition() {
						return methodProvider.inventory.getEmptySlotCount() > emptySlots;
					}

				}.sleep();
			} else if (methodProvider.inventory.getAmount(item.getName()) > GrandExchangeData.CURRENT_AMOUNT_OF_ITEM) {
				GrandExchangeData.ITEMS_TO_BUY_LIST.remove(item);
				GrandExchangeData.CURRENT_AMOUNT_OF_ITEM = 999999999;
			} else if (methodProvider.inventory.getAmount(995) >= getItemPrice(item)) {
				GrandExchangeData.CURRENT_AMOUNT_OF_ITEM = (int) methodProvider.inventory.getAmount(item.getName());
				methodProvider.grandExchange.buyItem(item.getID(), item.getName(), getItemPrice(item), getItemAmount(item));
				new ConditionalSleep(2500, 3000) {

					@Override
					public boolean condition() {
						return collectButtonIsVisible();
					}
				}.sleep();
			} else {
				methodProvider.log("lets call mule");
				// call mule
				IaoxAIO.shouldTrade = true;
				int coinsNeeded = 0;
				for (IaoxItem coinItem : GrandExchangeData.ITEMS_TO_BUY_LIST) {
					coinsNeeded += getItemPrice(coinItem);
				}
				for (IaoxItem coinItem : Data.WITHDRAW_LIST) {
					coinsNeeded += getItemPrice(coinItem);
				}
				IaoxAIO.coinsNeeded = coinsNeeded;
				break;
			}
		}
	}

	private int getItemAmount(IaoxItem item) {
		switch (item) {
		case FALADOR_TELEPORT:
			return 35;
		case COOKED_TROUT:
			return 500;
		case FEATHER:
			return 2500 + IaoxAIO.random(1000);
		case SNAKESKIN:
			return 3000 + IaoxAIO.random(1000);
		case BUCKET_OF_SAND:
			return 500;
		case SODA_ASH:
			return 500;
		default:
			return 1;
		}
	}

	public boolean inventoryContains(List<IaoxItem> potentialItems) {
		for (IaoxItem item : potentialItems) {
			if (methodProvider.inventory.contains(item.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean collectButtonIsVisible() {
		return methodProvider.widgets.isVisible(465, 6, 1);
	}

	public int getItemPrice(IaoxItem item) {
		if(item.getName().contains("robe") || item.getName().contains("cape") || 
				item.getName().contains("hat") || item.getName().contains("boots") ){
			try {
				return gePrice.getOverallPrice(item.getID()) + 4000 + IaoxAIO.random(3000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		switch (item) {
		case MITHRIL_PICKAXE:
		case DESERT_BOOTS:
		case RED_HAT:
		case MONKS_BOTTOM_ROBE:
		case MONKS_TOP_ROBE:
		case FLY_FISHING_ROD:
			return 4000 + IaoxAIO.random(500);
		case FEATHER:
			return 30 + IaoxAIO.random(10);
		case SNAKESKIN:
			return 25;
		case BUCKET_OF_SAND:
			return 60 + IaoxAIO.random(10);
		case SODA_ASH:
			return 130 + IaoxAIO.random(10);
		default:
			int overallPrice = 1000;
			try {
				overallPrice = gePrice.getOverallPrice(item.getID());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (int) (overallPrice * 2);
		}
	}

	public void sellItems() {
		for (IaoxItem item : GrandExchangeData.DEFAULT_SELLABLE_ITEMS) {
			if (item.getName() == null || !methodProvider.inventory.contains(item.getName())) {
				methodProvider.log("item is null");
			} else if (collectButtonIsVisible()) {
				int amountOfCash = (int) methodProvider.inventory.getAmount(995);
				methodProvider.grandExchange.collect();
				new ConditionalSleep(5000, 10000) {

					@Override
					public boolean condition() {
						return methodProvider.inventory.getAmount(995) > amountOfCash;
					}
				}.sleep();
				GrandExchangeData.SHOULD_COLLECT = false;
			} else {
				methodProvider.grandExchange.sellItem(getItemID(item), IaoxAIO.random(10, 20),
						(int) methodProvider.inventory.getAmount(item.getName()));
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
		if (methodProvider.inventory.getItem(item.getName()).isNote()) {
			return item.getID() + 1;
		}
		return item.getID();
	}
}
