package com.iaox.farmer.assignment.combat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resources;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

import com.iaox.farmer.assignment.combat.gear.GearSetups;
import com.iaox.farmer.data.Areas;

public enum FightingAssignment {

	SEAGULL("Seagull", FightingAreas.SEAGULL_AREA);
	private String _name;
	private Area _fightArea;
	private Area _bankArea;
	private Area _walkableBankArea;
	private GearSetups _gear;
	private ArrayList<Integer> _loot = null;
	private String[] _inventoryItems;
	private boolean _shouldEat;

	FightingAssignment(String name, Area fightArea) {
		_name = name;
		_fightArea = fightArea;
	}

	public String getName() {
		return _name;
	}

	public Area getFightArea() {
		return _fightArea;
	}

	public boolean getEat() {
		return _shouldEat;
	}

	public Area getBankArea() {
		return _bankArea;
	}

	public Area getWalkableBankArea() {
		return _walkableBankArea;
	}

	public GearSetups getGear() {
		return _gear;
	}

	public Position getBankPos() {
		return _walkableBankArea.getRandomPosition();
	}

	public List<Integer> getLoot() {
		return _loot;
	}

	public String[] getInventory() {
		return _inventoryItems;
	}

}
