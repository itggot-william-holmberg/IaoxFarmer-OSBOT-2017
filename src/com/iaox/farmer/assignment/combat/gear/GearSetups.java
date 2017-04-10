package com.iaox.farmer.assignment.combat.gear;

import java.util.ArrayList;
import java.util.List;

import com.iaox.farmer.data.items.IaoxItem;

public enum GearSetups {

	STARTER_MELEE_SETUP(IaoxItem.MONKS_TOP_ROBE,IaoxItem.MONKS_BOTTOM_ROBE,null,null,IaoxItem.BRONZE_SWORD,IaoxItem.AMULET_OF_STRENGTH),
	ADDY_SCIM_F2P(null,null,null,null,IaoxItem.ADAMANT_SCIMITAR,IaoxItem.AMULET_OF_STRENGTH),	
	ADDY_SCIM_P2P(IaoxItem.MONKS_TOP_ROBE, IaoxItem.MONKS_BOTTOM_ROBE,IaoxItem.RED_HAT,null,IaoxItem.ADAMANT_SCIMITAR, IaoxItem.TEAM_20_CAPE, IaoxItem.DESERT_BOOTS, IaoxItem.AMULET_OF_STRENGTH, null, null),	
	RUNE_SCIM_F2P(null,null,null,null,IaoxItem.RUNE_SCIMITAR,IaoxItem.AMULET_OF_STRENGTH),
	RUNE_SCIM_P2P(IaoxItem.MONKS_TOP_ROBE, IaoxItem.MONKS_BOTTOM_ROBE,IaoxItem.RED_HAT,null,IaoxItem.RUNE_SCIMITAR, IaoxItem.TEAM_21_CAPE, IaoxItem.DESERT_BOOTS, IaoxItem.AMULET_OF_STRENGTH, null, null),
	FULL_ADAMANT_ADDY_SCIM(IaoxItem.ADAMANT_PLATEBODY, IaoxItem.ADAMANT_PLATELEGS, IaoxItem.ADAMANT_FULL_HELM, null, IaoxItem.ADAMANT_SCIMITAR, IaoxItem.AMULET_OF_STRENGTH),
	FULL_ADAMANT_RUNE_SCIM(IaoxItem.ADAMANT_PLATEBODY, IaoxItem.ADAMANT_PLATELEGS, IaoxItem.ADAMANT_FULL_HELM, null, IaoxItem.RUNE_SCIMITAR, IaoxItem.AMULET_OF_STRENGTH);
	
	
	//STARTER_RANGED_SETUP;
	//FULL_ADAMANT_RUNE_SCIM(IaoxItem.ADAMANT_PLATEBODY, IaoxItem.ADAMANT_PLATELEGS, IaoxItem.ADAMANT_FULL_HELM, null, IaoxItem.ADAMANT_SCIMITAR, null);

	List<IaoxItem> _fullGear = new ArrayList<IaoxItem>();
	private IaoxItem _chest;
	private IaoxItem _legs;
	private IaoxItem _hat;
	private IaoxItem _gloves;
	private IaoxItem _shield;
	private IaoxItem _weapon;
	private IaoxItem _cape;
	private IaoxItem _feet;
	private IaoxItem _amulet;
	private IaoxItem _ring;
	private IaoxItem _arrows;

	private GearSetups(IaoxItem chest, IaoxItem legs, IaoxItem hat, IaoxItem shield, IaoxItem weapon, IaoxItem amulet) {
		_chest = chest;
		_legs = legs;
		_hat = hat;
		_shield = shield;
		_weapon = weapon;
		_amulet = amulet;
		_fullGear.add(_chest);
		_fullGear.add(_legs);
		_fullGear.add(_hat);
		_fullGear.add(_shield);
		_fullGear.add(_weapon);
		_fullGear.add(_amulet);

	}

	private GearSetups(IaoxItem chest, IaoxItem legs, IaoxItem hat, IaoxItem gloves, IaoxItem weapon, IaoxItem cape,
			IaoxItem feet, IaoxItem amulet, IaoxItem ring, IaoxItem arrows) {
		_chest = chest;
		_legs = legs;
		_hat = hat;
		_gloves = gloves;
		_weapon = weapon;
		_cape = cape;
		_feet = feet;
		_amulet = amulet;
		_ring = ring;
		_arrows = arrows;
		_fullGear.add(_chest);
		_fullGear.add(_legs);
		_fullGear.add(_hat);
		_fullGear.add(_gloves);
		_fullGear.add(_weapon);
		_fullGear.add(_cape);
		_fullGear.add(_feet);
		_fullGear.add(_amulet);
		_fullGear.add(_ring);
		_fullGear.add(_arrows);

	}

	private GearSetups(IaoxItem chest, IaoxItem legs, IaoxItem hat, IaoxItem gloves, IaoxItem shield, IaoxItem weapon,
			IaoxItem cape, IaoxItem feet, IaoxItem amulet, IaoxItem ring, IaoxItem arrows) {
		_chest = chest;
		_legs = legs;
		_hat = hat;
		_gloves = gloves;
		_shield = shield;
		_weapon = weapon;
		_cape = cape;
		_feet = feet;
		_amulet = amulet;
		_ring = ring;
		_arrows = arrows;
		_fullGear.add(_chest);
		_fullGear.add(_legs);
		_fullGear.add(_hat);
		_fullGear.add(_gloves);
		_fullGear.add(_shield);
		_fullGear.add(_weapon);
		_fullGear.add(_cape);
		_fullGear.add(_feet);
		_fullGear.add(_amulet);
		_fullGear.add(_ring);
		_fullGear.add(_arrows);

	}

	public List<IaoxItem> getFullGear() {
		return _fullGear;
	}

	public IaoxItem getChest() {
		return _chest;
	}

	public IaoxItem getLegs() {
		return _legs;
	}

	public IaoxItem getHat() {
		return _hat;
	}

	public IaoxItem getGloves() {
		return _gloves;
	}

	public IaoxItem getShield() {
		return _shield;
	}

	public IaoxItem getWeapon() {
		return _weapon;
	}

	public IaoxItem getCape() {
		return _cape;
	}

	public IaoxItem getFeet() {
		return _feet;
	}

	public IaoxItem getAmulet() {
		return _amulet;
	}

	public IaoxItem getRing() {
		return _ring;
	}

}
