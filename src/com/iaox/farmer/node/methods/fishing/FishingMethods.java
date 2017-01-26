package com.iaox.farmer.node.methods.fishing;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.fishing.FishingAssignment;
import com.iaox.farmer.assignment.woodcutting.WoodcuttingAssignment;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.data.WebBank;
import com.iaox.farmer.data.items.IaoxItem;

public class FishingMethods {
	
	private Script script;
	
	public FishingMethods(Script script){
		this.script = script;
	}
	
	public boolean playerInArea(Area area) {
		return area.contains(script.myPlayer());
	}

	public boolean playerInBankArea() {
		return getBankArea() != null && playerInArea(getBankArea());
	}

	/*
	 * If player has pickaxe we can bank at the usual place. But if player does
	 * not have his pickaxe we have to go to a "real bank" and withdraw it.
	 */
	public Area getBankArea() {
		if (getAssignment() != null && getAssignment().getBankArea() != null) {
			return getAssignment().getBankArea();
		}
		return WebBank.getNearest(script).getArea();
	}

	public boolean readyToFish() {
		return IaoxIntelligence.getFishingAssignment() != null && playerHasFishingGear() && !script.inventory.isFull();
	}

	public boolean playerHasFishingGear() {
		boolean bool = true;
		if (getAssignment().getInventoryItems() != null) {
			for (IaoxItem item : getAssignment().getInventoryItems()) {
				if (script.inventory.contains(item.getName())) {
					// inv contains the item. good
				} else if (!Data.WITHDRAW_LIST.contains(item)) {
					Data.WITHDRAW_LIST.add(item);
					bool = false;
				} else {
					bool = false;
				}
			}
		}
		return bool;
	}

	

	private void sleep(int i) {
		try {
			script.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	public FishingAssignment getAssignment() {
		return IaoxIntelligence.getFishingAssignment();
	}

	@SuppressWarnings("unchecked")
	public void fish() {
		IaoxAIO.CURRENT_ACTION = "Lets fish!";
		FishingAssignment assignment = getAssignment();

		NPC fishingSpot = script.npcs.closest(object -> assignment.getObjectIDs().contains(object.getId())
				&& assignment.getFishingArea().contains(object) && !object.getPosition().equals(new Position(3246,3157,0)));
		if (fishingSpot != null && fishingSpot.interact(assignment.getActionName())) {
			fishSleep(fishingSpot);
		}
	}

	/*
	 * If we are successfully mining, we should do a conditional sleep until we
	 * are ready to mine again.
	 */
	private void fishSleep(NPC tree) {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done fishing";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return tree != null && !tree.exists() || !script.myPlayer().isAnimating();
			}
		}.sleep();
	}

	public void fishSleep() {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done fishing";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return !script.myPlayer().isAnimating();
			}
		}.sleep();
	}

}
