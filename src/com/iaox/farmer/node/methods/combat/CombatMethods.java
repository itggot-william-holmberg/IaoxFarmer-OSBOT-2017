package com.iaox.farmer.node.methods.combat;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.combat.gear.GearSetups;
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
		if(Banks.GRAND_EXCHANGE.contains(script.myPlayer())){
			return Banks.GRAND_EXCHANGE;
		}
		if (getAssignment().getBankArea() != null) {
			return getAssignment().getBankArea();
		} else {
			return WebBank.getNearest(script).getArea();
		}
	}

	public boolean playerIsReadyForFight() {
		return playerHasGear() && playerHasInventoryItems() && script.inventory.getAmount(995) < 50000 && script.inventory.getEmptySlotCount() != 0;
	}

	private boolean playerHasInventoryItems() {
		boolean bool = true;
		for (IaoxItem item : getAssignment().getInventory()) {
			if (script.inventory.contains(item.getName())) {
				// inv contains the item. good
			} else if (!Data.WITHDRAW_LIST.contains(item)) {
				Data.WITHDRAW_LIST.add(item);
				bool = false;
			} else {
				bool = false;
			}
		}
		return bool;
	}

	private boolean playerHasGear() {
		boolean bool = true;
		for (IaoxItem item : getGear().getFullGear()) {
			if (item == null) {
				// item is null because there is no required item to wear.
			} else if (script.equipment.contains(item.getName())) {
			} else if (script.inventory.contains(item.getName())) {
				Item inventoryItem = script.inventory.getItem(item.getName());
				equipItem(inventoryItem);
			} else if (!Data.WITHDRAW_LIST.contains(item)) {
				Data.WITHDRAW_LIST.add(item);
				script.log("false!!!" + item.getName());
				bool = false;
			} else {
				script.log("false!!!");
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
		if (script.getSkills().getStatic(Skill.ATTACK) < 30) {
			return GearSetups.STARTER_MELEE_SETUP;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) < 40 && script.getSkills().getStatic(Skill.DEFENCE) < 30) {
			return GearSetups.ADDY_SCIM;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) < 60 && script.getSkills().getStatic(Skill.DEFENCE) < 30 && script.worlds.isMembersWorld()) {
			return GearSetups.RUNE_SCIM_P2P;
		}
		
		if (script.getSkills().getStatic(Skill.ATTACK) < 60 && script.getSkills().getStatic(Skill.DEFENCE) < 30) {
			return GearSetups.RUNE_SCIM_F2P;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) >= 30 && script.getSkills().getStatic(Skill.DEFENCE) >= 30) {
			return GearSetups.FULL_ADAMANT_ADDY_SCIM;
		}

		return GearSetups.STARTER_MELEE_SETUP;
	}

	public void withdrawNeededItems() {
		IaoxItem item = Data.WITHDRAW_LIST.get(0);
		if (script.inventory.contains(item.getName()) || script.equipment.contains(item.getName())) {
			Data.WITHDRAW_LIST.remove(item);
		} else {
			switch (item) {
			case FALADOR_TELEPORT:
				bankingMethods.withdraw(4 + IaoxAIO.random(2), item.getName());
			case COOKED_TROUT:
				bankingMethods.withdraw(12, item.getName());
				break;
			default:
				bankingMethods.withdraw(item.getName());
				break;
			}
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
				return playerHasToEat() || (!script.myPlayer().isAnimating() && script.myPlayer().isAttackable()
						&& !script.myPlayer().isUnderAttack() && !script.myPlayer().isMoving());
			}
		}.sleep();
	}

	public void combatSleep(NPC npc) {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done fighting";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return playerHasToEat() || (!(npc.getHealthPercent() > 0) && !script.myPlayer().isAnimating()
						&& script.myPlayer().isAttackable() && !script.myPlayer().isUnderAttack()
						&& !script.myPlayer().isMoving());
			}
		}.sleep();
	}
	
	public void loot() {
		IaoxAIO.CURRENT_ACTION = "Lets loot!";
		GroundItem item = getClosestLoot();

		if(item != null){
			int amountBeforeLoot = (int) script.getInventory().getAmount(item.getId());
			item.interact("Take");
			new ConditionalSleep(300,5000){

				@Override
				public boolean condition() throws InterruptedException {
					return script.inventory.getAmount(item.getId()) > amountBeforeLoot;
				}
				
			}.sleep();
		}
	}

	public boolean lootIsAvailable() {
		if (getAssignment().getLoot() == null) {
			return false;
		} else {
			GroundItem item = getClosestLoot();
			if (item != null) {
				return true;
			}
			return false;

		}
	}

	private GroundItem getClosestLoot() {
		return script.groundItems.closest(i -> IaoxItem.getItemIDS(getAssignment().getLoot()).contains(i.getId())
				&& getAssignment().getFightArea().contains(i));
	}

	public boolean playerHasToEat() {
		script.log(script.myPlayer().getHealthPercent());
		return script.myPlayer().getHealthPercent() < 45;
	}

	public void eat() {
		int hp = script.myPlayer().getHealthPercent();
		script.log("lets eat");
		script.inventory.interact("Eat", IaoxItem.COOKED_TROUT.getID());
		new ConditionalSleep(300, 5000) {

			@Override
			public boolean condition() throws InterruptedException {
				return hp != script.myPlayer().getHealthPercent();
			}

		}.sleep();
	}

}
