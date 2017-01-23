package com.iaox.farmer.node.woodcutting.methods;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.woodcutting.WoodcuttingAssignment;
import com.iaox.farmer.data.WebBank;
import com.iaox.farmer.data.items.IaoxItem;

public class WoodcuttingMethods {
	
	private Script script;
	
	public WoodcuttingMethods(Script script){
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
		if (getAssignment() != null) {
			return WebBank.getNearest(script).getArea();
		}
		return null;
	}

	public boolean readyToCut() {
		return IaoxIntelligence.getWCAssignment() != null && playerHasAxe() && !script.inventory.isFull();
	}

	public boolean playerHasAxe() {
		if (script.equipment.isWieldingWeapon(getAxe())) {
			return true;
		}

		if (script.inventory.contains(getAxe())) {
			return !canEquipAxe();
		}
		return false;
	}

	private boolean canEquipAxe() {
		switch (getAxe()) {

		case "Bronze axe":
			if (script.bank.isOpen()) {
				script.bank.close();
				return true;
			} else {
				script.inventory.interact("Wield", getAxe());
				sleep(2500);
				return true;
			}

		case "Mithril axe":
			if (script.getSkills().getDynamic(Skill.ATTACK) < 20) {
				return false;
			} else if (script.bank.isOpen()) {
				script.bank.close();
				sleep(2500);
				return true;
			} else {
				script.inventory.interact("Wield", getAxe());
				sleep(2500);
				return true;
			}

		case "Adamant axe":
			if (script.getSkills().getDynamic(Skill.ATTACK) < 30) {
				return false;
			} else if (script.bank.isOpen()) {
				script.bank.close();
				sleep(2500);
				return true;
			} else {
				script.inventory.interact("Wield", getAxe());
				sleep(2500);
				return true;
			}

		case "Rune axe":
			if (script.getSkills().getDynamic(Skill.ATTACK) < 40) {
				return false;
			} else if (script.bank.isOpen()) {
				script.bank.close();
				sleep(2500);
				return true;
			} else {
				script.inventory.interact("Wield", getAxe());
				sleep(2500);
				return true;
			}

		}
		return false;
	}

	private void sleep(int i) {
		try {
			script.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getAxe() {
		if (script.getSkills().getDynamic(Skill.WOODCUTTING) < 21) {
			return IaoxItem.BRONZE_AXE.getName();
		}

		if (script.getSkills().getDynamic(Skill.WOODCUTTING) < 31) {
			return IaoxItem.MITHRIL_AXE.getName();
		}

		if (script.getSkills().getDynamic(Skill.WOODCUTTING) < 41) {
			return IaoxItem.ADAMANT_AXE.getName();
		}

		return IaoxItem.RUNE_AXE.getName();
	}

	public WoodcuttingAssignment getAssignment() {
		return IaoxIntelligence.getWCAssignment();
	}

	@SuppressWarnings("unchecked")
	public void cut() {
		IaoxAIO.CURRENT_ACTION = "Lets Cut tree!";
		WoodcuttingAssignment assignment = getAssignment();

		RS2Object tree = script.objects.closest(object -> assignment.getObjectIDs().contains(object.getId())
				&& assignment.getWCArea().contains(object));
		if (tree != null && !IaoxIntelligence.lastClickedObject(tree) && tree.interact("Chop down")) {
			cutSleep(tree);
		}
	}

	/*
	 * If we are successfully mining, we should do a conditional sleep until we
	 * are ready to mine again.
	 */
	private void cutSleep(RS2Object tree) {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done cutting the tree";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return tree != null && !tree.exists() || !script.myPlayer().isAnimating();
			}
		}.sleep();
	}

	public void cutSleep() {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done cutting the tree";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return !script.myPlayer().isAnimating();
			}
		}.sleep();
	}

}
