package com.iaox.farmer.node.combat.methods;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.combat.gear.GearSetups;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.data.WebBank;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.node.methods.BankingMethods;

public class CombatMethods {

	private Script script;
	private BankingMethods bankingMethods;

	public CombatMethods(Script script) {
		this.script = script;
		this.bankingMethods = new BankingMethods(script);
	}

	public FightingAssignment getAssignment() {
		return IaoxIntelligence.getFightingAssignment();
	}

	public boolean playerCanAttack() {
		return !script.myPlayer().isAnimating() && script.myPlayer().isAttackable()
				&& !script.myPlayer().isUnderAttack() && !script.myPlayer().isMoving();
	}

	public void attack() {
		NPC npc = getClosestFreeNPC(getAssignment().getName());
	}

	public NPC getClosestFreeNPC(String name) {
		return script.getNpcs()
				.closest(npc -> !npc.isUnderAttack() && npc.getHealthPercent() > 0 && npc.getInteracting() == null
						&& npc.getName().equals(name) && npc.exists() && getAssignment().getFightArea().contains(npc)
						&& npc.hasAction("Attack"));
	}

	public boolean playerInBankArea() {
		return getBankArea().contains(script.myPlayer());
	}

	public Area getBankArea() {
		if (getAssignment().getBankArea() != null) {
			return getAssignment().getBankArea();
		} else {
			return WebBank.getNearest(script).getArea();
		}
	}
	

	public boolean playerIsReadyForFight() {
		return playerHasGear();
	}

	private boolean playerHasGear() {
		boolean bool = true;
		for (IaoxItem item : getGear().getFullGear()) {
			if (item == null) {
				// item is null because there is no required item to wear.
			} else if (script.equipment.contains(item.getName())) {
				script.log("we have the current item: " + item.getName());
			} else if (script.inventory.contains(item.getName())) {
				Item inventoryItem = script.inventory.getItem(item.getName());
				equipItem(inventoryItem);
			} else if (!Data.WITHDRAW_LIST.contains(item)) {
				script.log("we do not have the item: " + item.getName());
				Data.WITHDRAW_LIST.add(item);
				bool = false;
			} else {
				bool = false;
			}
		}
		return bool;
	}

	private void equipItem(Item item) {
		if (script.widgets.closeOpenInterface()) {
			if (item.hasAction("Wield")) {
				item.interact("Wield");
			} else if (item.hasAction("Wear")) {
				item.interact("Wear");
			}
			new ConditionalSleep(5000) {

				@Override
				public boolean condition() throws InterruptedException {
					return script.equipment.contains(item.getName());
				}

			}.sleep();
		}
		try {
			IaoxAIO.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public GearSetups getGear() {
		if (script.getSkills().getDynamic(Skill.ATTACK) < 30) {
			return GearSetups.STARTER_MELEE_SETUP;
		}

		if (script.getSkills().getDynamic(Skill.ATTACK) >= 30 && script.getSkills().getDynamic(Skill.DEFENCE) < 30) {
			return GearSetups.ADDY_SCIM;
		}
		
		if (script.getSkills().getDynamic(Skill.ATTACK) >= 30 && script.getSkills().getDynamic(Skill.DEFENCE) >= 30) {
			return GearSetups.FULL_ADAMANT_ADDY_SCIM;
		}

		return GearSetups.STARTER_MELEE_SETUP;
	}

	public void withdrawNeededItems() {
		IaoxItem item = Data.WITHDRAW_LIST.get(0);
		if (script.inventory.contains(item.getName()) || script.equipment.contains(item.getName())) {
			Data.WITHDRAW_LIST.remove(item);
		} else {
			bankingMethods.withdraw(item.getName());
		}
	}

	public boolean playerInFightArea() {
		return getAssignment() != null && getAssignment().getFightArea().contains(script.myPlayer());
	}

	public void fight() {
		IaoxAIO.CURRENT_ACTION = "Lets Attack!";
		FightingAssignment assignment = getAssignment();

		NPC enemy = getClosestFreeNPC(assignment.getName());

		if (!rightStyle()) {
			checkStyle();
		} else if (enemy != null && enemy.interact("Attack")) {
			combatSleep(enemy);
		}
	}

	public boolean rightStyle() {
		Skill skill = IaoxAIO.CURRENT_TASK.getAssignment().getSkill();
		if (skill.equals(Skill.STRENGTH) && (attackStyle() != 1)) {
			return false;
		} else if (skill.equals(Skill.ATTACK) && (attackStyle() != 0)) {
			return false;
		} else if (skill.equals(Skill.DEFENCE) && (attackStyle() != 3)) {
			return false;
		} else if (skill.equals(Skill.RANGED) && (attackStyle() != 1)) {
			return false;
		}
		return true;
	}

	public void checkStyle() {
		if (script.widgets.isVisible(593) && getAssignment() != null) {
			Skill skill = IaoxAIO.CURRENT_TASK.getAssignment().getSkill();
			if (skill.equals(Skill.STRENGTH) && (attackStyle() != 1)) {
				script.mouse.click(689, 270, false);// click "train strength"
			} else if (skill.equals(Skill.ATTACK) && (attackStyle() != 0)) {
				script.mouse.click(601, 269, false); // click "train attack"
			} else if (skill.equals(Skill.DEFENCE) && (attackStyle() != 3)) {
				script.mouse.click(701, 335, false); // click "train def"
			} else if (skill.equals(Skill.RANGED) && (attackStyle() != 1)) {
				script.mouse.click(689, 270, false);// click "train range"
			}

		} else {
			script.mouse.click(545, 190, false);
		}

	}

	public int attackStyle() {

		return script.configs.get(43);

	}

	public void combatSleep() {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done fighting";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return !script.myPlayer().isAnimating() && script.myPlayer().isAttackable()
						&& !script.myPlayer().isUnderAttack() && !script.myPlayer().isMoving();
			}
		}.sleep();
	}

	public void combatSleep(NPC npc) {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done fighting";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return !(npc.getHealthPercent() > 0) && !script.myPlayer().isAnimating()
						&& script.myPlayer().isAttackable() && !script.myPlayer().isUnderAttack()
						&& !script.myPlayer().isMoving();
			}
		}.sleep();
	}

}
