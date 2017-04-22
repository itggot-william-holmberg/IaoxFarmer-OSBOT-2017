package com.iaox.farmer.node.crafting;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.node.Node;

public class CraftingAction extends Node {

	@Override
	public boolean active() {
		return craftingProvider.hasRequiredInventoryItems() && craftingProvider.inActionArea();
	}

	@Override
	public void execute() {
		switch (craftingProvider.getCraftingAssignment()) {
		case MOLTEN_GLASS:
			craftMoltenGlass();
			break;
		case SNAKESKIN_CHAPS:
			craftSnakeskin();
		default:
			break;

		}

	}

	private void craftSnakeskin() {
		if (methodProvider.myPlayer().isAnimating()) {
			new ConditionalSleep(25000, 1500) {

				@Override
				public boolean condition() throws InterruptedException {
					return !methodProvider.myPlayer().isAnimating();
				}

			}.sleep();
		} else if(methodProvider.bank.isOpen()){
			methodProvider.bank.close();
		}
		else if (widgetProvider.isVisible(306, 3)) {
			make10();
		} else if (inventoryProvider.isSelected(IaoxItem.NEEDLE)) {
			inventoryProvider.interact(IaoxItem.SNAKESKIN);
		} else {
			inventoryProvider.select(IaoxItem.NEEDLE);
		}
	}

	private void craftMoltenGlass() {
		if (methodProvider.myPlayer().isAnimating()) {
			new ConditionalSleep(25000, 1500) {

				@Override
				public boolean condition() throws InterruptedException {
					return !methodProvider.myPlayer().isAnimating();
				}

			}.sleep();
		} else if (widgetProvider.isVisible(309, 2)) {
			makeAll();
		} else if (inventoryProvider.isSelected(IaoxItem.BUCKET_OF_SAND)) {
			interactFurnace();
		} else {
			inventoryProvider.select(IaoxItem.BUCKET_OF_SAND);
		}
	}

	private void interactFurnace() {
		RS2Object furnace = objectProvider.get(16469);
		furnace.interact("Use");
		iaoxProvider.sleep(1000, 2000);
		new ConditionalSleep(3000, 600) {

			@Override
			public boolean condition() throws InterruptedException {
				return widgetProvider.isVisible(309, 2);
			}

		}.sleep();
	}

	private void makeAll() {
		methodProvider.log("lets make all");
		widgetProvider.makeAll(309, 2);
		iaoxProvider.sleep(2000, 3000);
		new ConditionalSleep(25000, 1500) {

			@Override
			public boolean condition() throws InterruptedException {
				return !methodProvider.myPlayer().isAnimating();
			}

		}.sleep();
	}

	private void make10() {
		methodProvider.log("lets make 10");
		widgetProvider.interact(306, 3, "Make 10");
		iaoxProvider.sleep(2000, 3000);
		new ConditionalSleep(25000, 1500) {

			@Override
			public boolean condition() throws InterruptedException {
				return !methodProvider.myPlayer().isAnimating();
			}

		}.sleep();
	}


	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "MELT";
	}

}
