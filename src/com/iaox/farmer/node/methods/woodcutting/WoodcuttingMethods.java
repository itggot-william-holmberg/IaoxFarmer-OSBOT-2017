package com.iaox.farmer.node.methods.woodcutting;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.woodcutting.WoodcuttingAssignment;
import com.iaox.farmer.data.WebBank;
import com.iaox.farmer.data.items.IaoxItem;

public class WoodcuttingMethods {
	
	private MethodProvider methodProvider;
	
	public WoodcuttingMethods(MethodProvider methodProvider){
		this.methodProvider = methodProvider;
	}
	
	public boolean playerInArea(Area area) {
		return area.contains(methodProvider.myPlayer());
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
			return WebBank.getNearest(methodProvider).getArea();
		}
		return null;
	}

	public boolean readyToCut() {
		return IaoxIntelligence.getWCAssignment() != null && playerHasAxe() && !methodProvider.inventory.isFull();
	}

	public boolean playerHasAxe() {
		if (methodProvider.equipment.isWieldingWeapon(getAxe())) {
			return true;
		}

		if (methodProvider.inventory.contains(getAxe())) {
			return !canEquipAxe();
		}
		return false;
	}

	private boolean canEquipAxe() {
		switch (getAxe()) {

		case "Bronze axe":
			if (methodProvider.bank.isOpen()) {
				methodProvider.bank.close();
				return true;
			} else {
				methodProvider.inventory.interact("Wield", getAxe());
				sleep(2500);
				return true;
			}

		case "Mithril axe":
			if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 20) {
				return false;
			} else if (methodProvider.bank.isOpen()) {
				methodProvider.bank.close();
				sleep(2500);
				return true;
			} else {
				methodProvider.inventory.interact("Wield", getAxe());
				sleep(2500);
				return true;
			}

		case "Adamant axe":
			if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 30) {
				return false;
			} else if (methodProvider.bank.isOpen()) {
				methodProvider.bank.close();
				sleep(2500);
				return true;
			} else {
				methodProvider.inventory.interact("Wield", getAxe());
				sleep(2500);
				return true;
			}

		case "Rune axe":
			if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 40) {
				return false;
			} else if (methodProvider.bank.isOpen()) {
				methodProvider.bank.close();
				sleep(2500);
				return true;
			} else {
				methodProvider.inventory.interact("Wield", getAxe());
				sleep(2500);
				return true;
			}

		}
		return false;
	}

	private void sleep(int i) {
		try {
			methodProvider.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getAxe() {
		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 21) {
			return IaoxItem.BRONZE_AXE.getName();
		}

		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 31) {
			return IaoxItem.MITHRIL_AXE.getName();
		}

		if (methodProvider.getSkills().getStatic(Skill.WOODCUTTING) < 41) {
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

		RS2Object tree = methodProvider.objects.closest(object -> assignment.getObjectIDs().contains(object.getId())
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
				return tree != null && !tree.exists() || !methodProvider.myPlayer().isAnimating();
			}
		}.sleep();
	}

	public void cutSleep() {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done cutting the tree";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return !methodProvider.myPlayer().isAnimating();
			}
		}.sleep();
	}

}
