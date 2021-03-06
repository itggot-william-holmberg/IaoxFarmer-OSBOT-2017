package com.iaox.farmer.node.methods.mining;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.mining.MiningAreas;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.data.WebBank;
import com.iaox.farmer.data.items.IaoxItem;

public class MiningMethods {

	private MethodProvider methodProvider;

	public MiningMethods(MethodProvider methodProvider) {
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
			if (playerInArea(MiningAreas.PORT_SARIM_DEPOSIT_AREA)) {
				return getAssignment().getBankArea();
			}

			if (getAssignment().getMiningArea().equals(MiningAreas.RIMMINGTON_MINING_AREA) && playerHasPickaxe()) {
				return getAssignment().getBankArea();
			}

			return WebBank.getNearest(methodProvider).getArea();
		}
		return null;
	}

	public boolean readyToMine() {
		return IaoxIntelligence.getMiningAssignment() != null && playerHasPickaxe() && !methodProvider.inventory.isFull();
	}

	public boolean playerHasPickaxe() {
		if (playerInArea(MiningAreas.PORT_SARIM_DEPOSIT_AREA)) {
			return true;
		}

		if (methodProvider.equipment.isWieldingWeapon(getPickaxe())) {
			return true;
		}

		if (methodProvider.inventory.contains(getPickaxe())) {
			return !canEquipPickaxe();
		}
		return false;
	}

	private boolean canEquipPickaxe() {
		switch (getPickaxe()) {

		case "Bronze pickaxe":
			if (methodProvider.bank.isOpen()) {
				methodProvider.bank.close();
				return true;
			} else {
				methodProvider.inventory.interact("Wield", getPickaxe());
				sleep(2500);
				return true;
			}

		case "Mithril pickaxe":
			if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 20) {
				return false;
			} else if (methodProvider.bank.isOpen()) {
				methodProvider.bank.close();
				sleep(2500);
				return true;
			} else {
				methodProvider.inventory.interact("Wield", getPickaxe());
				sleep(2500);
				return true;
			}

		case "Adamant pickaxe":
			if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 30) {
				return false;
			} else if (methodProvider.bank.isOpen()) {
				methodProvider.bank.close();
				sleep(2500);
				return true;
			} else {
				methodProvider.inventory.interact("Wield", getPickaxe());
				sleep(2500);
				return true;
			}

		case "Rune pickaxe":
			if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 40) {
				return false;
			} else if (methodProvider.bank.isOpen()) {
				methodProvider.bank.close();
				sleep(2500);
				return true;
			} else {
				methodProvider.inventory.interact("Wield", getPickaxe());
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

	public String getPickaxe() {
		if (methodProvider.getSkills().getStatic(Skill.MINING) < 21) {
			return IaoxItem.BRONZE_PICKAXE.getName();
		}

		if (methodProvider.getSkills().getStatic(Skill.MINING) < 31) {
			return IaoxItem.MITHRIL_PICKAXE.getName();
		}

		if (methodProvider.getSkills().getStatic(Skill.MINING) < 41) {
			return IaoxItem.ADAMANT_PICKAXE.getName();
		}

		return IaoxItem.RUNE_PICKAXE.getName();
	}

	public MiningAssignment getAssignment() {
		return IaoxIntelligence.getMiningAssignment();
	}

	@SuppressWarnings("unchecked")
	public void mine() {
		IaoxAIO.CURRENT_ACTION = "Lets Mine!";
		MiningAssignment assignment = getAssignment();

		RS2Object ore = methodProvider.objects.closest(object -> assignment.getObjectIDs().contains(object.getId())
				&& assignment.getObjectArea().contains(object));
		if (ore != null && ore.interact("Mine")) {
			mineSleep(ore);
		}
	}

	/*
	 * If we are successfully mining, we should do a conditional sleep until we
	 * are ready to mine again.
	 */
	private void mineSleep(RS2Object ore) {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done mining";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return ore != null && !ore.exists() || !methodProvider.myPlayer().isAnimating();
			}
		}.sleep();
	}

	public void mineSleep() {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done mining";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return !methodProvider.myPlayer().isAnimating();
			}
		}.sleep();
	}

}
