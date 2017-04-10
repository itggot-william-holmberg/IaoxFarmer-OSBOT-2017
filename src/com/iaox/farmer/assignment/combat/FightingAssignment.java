package com.iaox.farmer.assignment.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resources;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;

import com.iaox.farmer.assignment.combat.gear.GearSetups;
import com.iaox.farmer.data.Areas;
import com.iaox.farmer.data.items.IaoxItem;

public enum FightingAssignment {

	SEAGULL(new String[]{"Seagull"}, FightingAreas.SEAGULL_AREA),
	COWS_LUMBRIDGE(new String[]{"Cow"}, FightingAreas.COW_LUMBRIDGE_AREA, Banks.LUMBRIDGE_UPPER, new IaoxItem[]{IaoxItem.COOKED_TROUT}),
	CHAOS_DRUIDS_TAVERLEY(new String[]{"Chaos druid"}, FightingAreas.CHAOS_DRUIDS_TAVERLEY_AREA, Banks.FALADOR_WEST, new IaoxItem[]{IaoxItem.COOKED_TROUT, IaoxItem.FALADOR_TELEPORT}, Loot.CHAOS_DRUID_LOOT);
	
	private String[] name;
	private Area fightArea;
	private Area bankArea;
	private Area walkableBankArea;
	private GearSetups gear;
	private List<IaoxItem> loot;
	private List<IaoxItem> inventoryItems;
	private boolean shouldEat;

	FightingAssignment(String[] name, Area fightArea) {
		this.name = name;
		this.fightArea = fightArea;
	}
	
	FightingAssignment(String[] name, Area fightArea, Area bankArea) {
		 this.name = name;
		 this.fightArea = fightArea;
		 this.bankArea = bankArea;
	}
	
	FightingAssignment(String[] name, Area fightArea, Area bankArea, IaoxItem[] loot) {
		 this.name = name;
		 this.fightArea = fightArea;
		 this.bankArea = bankArea;
		 this.loot = new ArrayList<IaoxItem>();
		 Arrays.asList(loot).forEach(item -> {
			 this.loot.add(item);
		 });
	}
	
	FightingAssignment(String[] name, Area fightArea, Area bankArea, IaoxItem[] inventoryItems, IaoxItem[] loot) {
		 this.name = name;
		 this.fightArea = fightArea;
		 this.bankArea = bankArea;
		 this.inventoryItems = new ArrayList<IaoxItem>();
		 Arrays.asList(inventoryItems).forEach(item -> {
			 this.inventoryItems.add(item);
		 });
		 this.loot = new ArrayList<IaoxItem>();
		 Arrays.asList(loot).forEach(item -> {
			 this.loot.add(item);
		 });
	}
	public String[] getName() {
		return name;
	}

	public Area getFightArea() {
		return  fightArea;
	}

	public boolean getEat() {
		return  shouldEat;
	}

	public Area getBankArea() {
		return  bankArea;
	}

	public Area getWalkableBankArea() {
		return  walkableBankArea;
	}

	public GearSetups getGear() {
		return  gear;
	}

	public Position getBankPos() {
		return  walkableBankArea.getRandomPosition();
	}

	public List<IaoxItem> getLoot() {
		return loot;
	}

	public List<IaoxItem> getInventory() {
		return inventoryItems;
	}

}
