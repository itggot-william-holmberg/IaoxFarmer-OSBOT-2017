package com.iaox.farmer.node.methods.combat;

import java.util.Arrays;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.combat.gear.GearSetups;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.data.WebBank;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.node.provider.BankProvider;

public class CombatMethods {

	private MethodProvider methodProvider;
	private BankProvider bankingMethods;

	public CombatMethods(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		this.bankingMethods = new BankProvider(methodProvider);
	}

	public FightingAssignment getAssignment() {
		return IaoxIntelligence.getFightingAssignment();
	}

	public boolean playerCanAttack() {
		return !methodProvider.myPlayer().isAnimating() && methodProvider.myPlayer().isAttackable()
				&& !methodProvider.myPlayer().isUnderAttack() && !methodProvider.myPlayer().isMoving();
	}

	public void attack() {
		NPC npc = getClosestFreeNPC(getAssignment().getName());
	}

	public NPC getClosestFreeNPC(String[] name) {
		return methodProvider.getNpcs()
				.closest(npc -> !npc.isUnderAttack() && npc.getHealthPercent() > 0 && npc.getInteracting() == null
						&& Arrays.asList(name).contains(npc.getName()) && npc.exists()
						&& getAssignment().getFightArea().contains(npc) && npc.hasAction("Attack"));
	}

	public boolean playerInBankArea() {
		return getBankArea().contains(methodProvider.myPlayer());
	}

	public Area getBankArea() {
		if (Banks.GRAND_EXCHANGE.contains(methodProvider.myPlayer())) {
			return Banks.GRAND_EXCHANGE;
		}
		if (getAssignment().getBankArea() != null) {
			return getAssignment().getBankArea();
		} else {
			return WebBank.getNearest(methodProvider).getArea();
		}
	}

	public boolean playerIsReadyForFight() {
		return playerHasGear() && playerHasInventoryItems() && methodProvider.inventory.getAmount(995) < 50000
				&& methodProvider.inventory.getEmptySlotCount() != 0;
	}

	private boolean playerHasInventoryItems() {
		boolean bool = true;
		if (getAssignment().getInventory() != null) {
			for (IaoxItem item : getAssignment().getInventory()) {
				if (methodProvider.inventory.contains(item.getID())) {
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

	private boolean playerHasGear() {
		boolean bool = true;
		for (IaoxItem item : getGear().getFullGear()) {
			if (item == null) {
				// item is null because there is no required item to wear.
			} else if (methodProvider.equipment.contains(item.getName())) {
			} else if (methodProvider.inventory.contains(item.getName())) {
				Item inventoryItem = methodProvider.inventory.getItem(item.getName());
				equipItem(inventoryItem);
			} else if (!Data.WITHDRAW_LIST.contains(item)) {
				Data.WITHDRAW_LIST.add(item);
				methodProvider.log("false!!!" + item.getName());
				bool = false;
			} else {
				methodProvider.log("false!!!");
				bool = false;
			}
		}
		return bool;
	}

	private void equipItem(Item item) {
		if (methodProvider.widgets.closeOpenInterface()) {
			if (item.hasAction("Wield")) {
				item.interact("Wield");
			} else if (item.hasAction("Wear")) {
				item.interact("Wear");
			}
			new ConditionalSleep(5000) {

				@Override
				public boolean condition() throws InterruptedException {
					return methodProvider.equipment.contains(item.getName());
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
		/*if (IaoxAIO.CURRENT_TASK.getAssignment().getType() == AssignmentType.MELEE) {
			return getMeleeGear();
		}

		if (IaoxAIO.CURRENT_TASK.getAssignment().getType() == AssignmentType.RANGED) {
			return getRangeGear();
		}*/
		return getMeleeGear();
	}

	/*private GearSetups getRangeGear() {
		if (script.getSkills().getStatic(Skill.RANGED) < 20) {
			return GearSetups.STARTER_RANGED_SETUP;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) < 40 && script.getSkills().getStatic(Skill.DEFENCE) < 30 &&
				script.worlds.isMembersWorld()) {
			return GearSetups.ADDY_SCIM_P2P;
		}
		
		if (script.getSkills().getStatic(Skill.ATTACK) < 40 && script.getSkills().getStatic(Skill.DEFENCE) < 30) {
			return GearSetups.ADDY_SCIM_F2P;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) < 60 && script.getSkills().getStatic(Skill.DEFENCE) < 30
				&& script.worlds.isMembersWorld()) {
			return GearSetups.RUNE_SCIM_P2P;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) < 60 && script.getSkills().getStatic(Skill.DEFENCE) < 30) {
			return GearSetups.RUNE_SCIM_F2P;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) >= 30 && script.getSkills().getStatic(Skill.DEFENCE) >= 30) {
			return GearSetups.FULL_ADAMANT_ADDY_SCIM;
		}

		return GearSetups.STARTER_MELEE_SETUP;
	}*/

	private GearSetups getMeleeGear() {
		if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 30) {
			return GearSetups.STARTER_MELEE_SETUP;
		}

		if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 40 && methodProvider.getSkills().getStatic(Skill.DEFENCE) < 30
				&& methodProvider.worlds.isMembersWorld()) {
			return GearSetups.ADDY_SCIM_P2P;
		}

		if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 40 && methodProvider.getSkills().getStatic(Skill.DEFENCE) < 30) {
			return GearSetups.ADDY_SCIM_F2P;
		}

		if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 60 && methodProvider.getSkills().getStatic(Skill.DEFENCE) < 30
				&& methodProvider.worlds.isMembersWorld()) {
			return GearSetups.RUNE_SCIM_P2P;
		}

		if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 60 && methodProvider.getSkills().getStatic(Skill.DEFENCE) < 30) {
			return GearSetups.RUNE_SCIM_F2P;
		}

		if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 40 && methodProvider.getSkills().getStatic(Skill.DEFENCE) < 45) {
			return GearSetups.FULL_ADAMANT_ADDY_SCIM;
		}
		if (methodProvider.getSkills().getStatic(Skill.ATTACK) < 99 && methodProvider.getSkills().getStatic(Skill.DEFENCE) < 45) {
			return GearSetups.FULL_ADAMANT_RUNE_SCIM;
		}

		return GearSetups.STARTER_MELEE_SETUP;
	}

	public void withdrawNeededItems() {
		IaoxItem item = Data.WITHDRAW_LIST.get(0);
		// noted
		if (methodProvider.inventory.contains(item.getID() + 1)) {
			bankingMethods.depositAll(item.getName());
		} else if (methodProvider.inventory.contains(item.getName()) || methodProvider.equipment.contains(item.getName())) {
			Data.WITHDRAW_LIST.remove(item);
		} else {
			switch (item) {
			case FALADOR_TELEPORT:
				bankingMethods.withdraw(4 + IaoxAIO.random(2), item.getName());
			case COOKED_TROUT:
				bankingMethods.withdraw(getFoodAmount(), item.getName());
				break;
			default:
				bankingMethods.withdraw(item.getName());
				break;
			}
		}
	}

	public int getFoodAmount() {
		if (methodProvider.getSkills().getStatic(Skill.DEFENCE) < 30 && IaoxIntelligence.getFightingAssignment() == FightingAssignment.SEAGULL){
			return 16;
		}
		if (methodProvider.getSkills().getStatic(Skill.DEFENCE) < 30) {
			return 4;
		}
		return 3;
	}

	public boolean playerInFightArea() {
		return getAssignment() != null && getAssignment().getFightArea().contains(methodProvider.myPlayer());
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
		if (methodProvider.widgets.isVisible(593) && getAssignment() != null) {
			Skill skill = IaoxAIO.CURRENT_TASK.getAssignment().getSkill();
			if (skill.equals(Skill.STRENGTH) && (attackStyle() != 1)) {
				methodProvider.mouse.click(689, 270, false);// click "train strength"
			} else if (skill.equals(Skill.ATTACK) && (attackStyle() != 0)) {
				methodProvider.mouse.click(601, 269, false); // click "train attack"
			} else if (skill.equals(Skill.DEFENCE) && (attackStyle() != 3)) {
				methodProvider.mouse.click(701, 335, false); // click "train def"
			} else if (skill.equals(Skill.RANGED) && (attackStyle() != 1)) {
				methodProvider.mouse.click(689, 270, false);// click "train range"
			}

		} else {
			methodProvider.mouse.click(545, 190, false);
		}

	}

	public int attackStyle() {

		return methodProvider.configs.get(43);

	}

	public void combatSleep() {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done fighting";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return playerHasToEat() || (!methodProvider.myPlayer().isAnimating() && methodProvider.myPlayer().isAttackable()
						&& !methodProvider.myPlayer().isUnderAttack() && !methodProvider.myPlayer().isMoving());
			}
		}.sleep();
	}

	public void combatSleep(NPC npc) {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done fighting";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(1200);
				return playerHasToEat() || (!(npc.getHealthPercent() > 0) && !methodProvider.myPlayer().isAnimating()
						&& methodProvider.myPlayer().isAttackable() && !methodProvider.myPlayer().isUnderAttack()
						&& !methodProvider.myPlayer().isMoving());
			}
		}.sleep();
	}

	public void loot() {
		IaoxAIO.CURRENT_ACTION = "Lets loot!";
		GroundItem item = getClosestLoot();

		if (item != null) {
			int amountBeforeLoot = (int) methodProvider.getInventory().getAmount(item.getId());
			item.interact("Take");
			try {
				IaoxAIO.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new ConditionalSleep(300, 5000) {

				@Override
				public boolean condition() throws InterruptedException {
					return methodProvider.inventory.getAmount(item.getId()) > amountBeforeLoot;
				}

			}.sleep();
		}

		GroundItem bones = getClosestLoot(IaoxItem.BONES);

		if (bones != null && Data.trainDefence) {
			int amountBeforeLoot = (int) methodProvider.getInventory().getAmount(bones.getId());
			bones.interact("Take");
			new ConditionalSleep(300, 5000) {

				@Override
				public boolean condition() throws InterruptedException {
					return methodProvider.inventory.getAmount(bones.getId()) > amountBeforeLoot;
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

	public GroundItem getClosestLoot(IaoxItem item) {
		return methodProvider.groundItems.closest(i -> item.getID() == i.getId() && getAssignment().getFightArea().contains(i)  && i.getPosition().distance(methodProvider.myPlayer()) > 7);
	}

	private GroundItem getClosestLoot() {
		return methodProvider.groundItems.closest(i -> IaoxItem.getItemIDS(getAssignment().getLoot()).contains(i.getId())
				&& getAssignment().getFightArea().contains(i) && i.getPosition().distance(methodProvider.myPlayer()) < 7);
	}

	public boolean playerHasToEat() {
		return methodProvider.myPlayer().getHealthPercent() < 45;
	}

	public void eat() {
		int hp = methodProvider.myPlayer().getHealthPercent();
		methodProvider.log("lets eat");
		methodProvider.inventory.interact("Eat", IaoxItem.COOKED_TROUT.getID());
		new ConditionalSleep(300, 5000) {

			@Override
			public boolean condition() throws InterruptedException {
				return hp != methodProvider.myPlayer().getHealthPercent();
			}

		}.sleep();
	}

	public void buryBones() {
		methodProvider.inventory.interact("Bury", "Bones");
		new ConditionalSleep(300, 5000) {

			@Override
			public boolean condition() throws InterruptedException {
				return !methodProvider.inventory.contains("Bones");
			}

		}.sleep();
	}

}
