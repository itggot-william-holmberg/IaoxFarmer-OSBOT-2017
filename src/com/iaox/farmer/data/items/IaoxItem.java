package com.iaox.farmer.data.items;

import java.util.ArrayList;
import java.util.List;

public enum IaoxItem {
	//pickaxe
	BRONZE_PICKAXE("Bronze pickaxe",1265), MITHRIL_PICKAXE("Mithril pickaxe",1273), 
	ADAMANT_PICKAXE("Adamant pickaxe",1271), RUNE_PICKAXE("Rune pickaxe",1275),
	//ores
	IRON_ORE("Iron ore", 440),
	
	
	//axes
	BRONZE_AXE("Bronze axe",1351), MITHRIL_AXE("Mithril axe", 1355),
	ADAMANT_AXE("Adamant axe", 1357), RUNE_AXE("Rune axe", 1359),
	
	//swords
	BRONZE_SWORD("Bronze sword", 1277), ADAMANT_SCIMITAR("Adamant scimitar", 1331),
	
	RUNE_SCIMITAR("Rune scimitar", 1333),
	
	//equipment
	ADAMANT_FULL_HELM("Adamant full helm", 1161, 30),
	ADAMANT_PLATEBODY("Adamant platebody", 1123, 30),
	ADAMANT_PLATELEGS("Adamant platelegs", 1073, 30),
	

	
	
	//capes
	
	TEAM_20_CAPE("Team-20 cape", 4353, 1),
	TEAM_21_CAPE("Team-21 cape", 4355, 1),
	TEAM_22_CAPE("Team-22 cape", 4357, 1),
	TEAM_23_CAPE("Team-23 cape", 4359, 1),
	TEAM_24_CAPE("Team-24 cape", 4361, 1),
	TEAM_25_CAPE("Team-25 cape", 4363, 1),
	TEAM_26_CAPE("Team-26 cape", 4365, 1),
	TEAM_27_CAPE("Team-27 cape", 4367, 1),
	TEAM_28_CAPE("Team-28 cape", 4369, 1),
	TEAM_29_CAPE("Team-29 cape", 4371, 1),
	TEAM_30_CAPE("Team-30 cape", 4373, 1),
	
	//robes
	
	WIZARD_ROBE_TOP("Wizard robe top", 577, 1),
	WIZARD_ROBE_SKIRT("Wizard robe skirt", 1011, 1),
	BLACK_ROBE_TOP("Black robe top", 581, 1),
	BLACK_ROBE_SKIRT("Black robe skirt", 1015, 1),
	PINK_ROBE_TOP("Pink robe top", 636, 1),
	PINK_ROBE_BOTTOMS("Pink robe bottoms", 646, 1),
	GREEN_ROBE_TOP("Green robe top", 638, 1),
	GREEN_ROBE_BOTTOMS("Green robe bottoms", 648),
	BLUE_ROBE_TOP("Blue robe top", 640, 1),
	BLUE_ROBE_BOTTOMS("Blue robe bottoms", 650, 1),
	CREAM_ROBE_TOP("Cream robe top", 642, 1),
	CREAM_ROBE_BOTTOMS("Cream robe bottoms", 652, 1),
	MONKS_BOTTOM_ROBE("Monk's robe", 542, 1),
	MONKS_TOP_ROBE("Monk's robe", 544),
	
	
	//boots
	DESERT_BOOTS("Desert boots", 1837, 1),
	
	//hats
	RED_HAT("Red hat", 2910, 1),
	
	
	//Ranged EQUIPMENT
	LEATHER_BODY("Leather body", 1129, 1),
	LEATHER_CHAPS("Leather chaps", 1095, 1),
	LEATHER_VAMBRACES("Leather vambraces", 1063, 1),
	
	
	//amulets
	AMULET_OF_STRENGTH("Amulet of strength", 1725, 1),
	
	//teleport tablets and runes
	FALADOR_TELEPORT("Falador teleport", 8009),
	CAMELOT_TELEPORT("Camelot teleport", 8009),
	VARROCK_TELEPORT("Varrock teleport", 8009),
	LAW_RUNE("Law rune", 563),
	NATURE_RUNE("Nature rune", 561),
	
	//range arrows
	MITHRIL_BOLTS("Mithril bolts", 9142),
	
	//food
	COOKED_TROUT("Trout",333),
	
	//herbs
	GRIMY_IRIT("Grimy irit leaf", 209),
	GRIMY_HARRALANDER("Grimy harralander", 205),
	GRIMY_AVANTOE("Grimy avantoe", 211),
	GRIMY_KWUARM("Grimy kwuarm", 213),
	GRIMY_DWARF_WEED("Grimy dwarf weed", 217),
	GRIMY_LANTADYME("Grimy lantadyme", 2485),
	GRIMY_CADANTINE("Grimy cadantine", 215),
	GRIMY_RANARR_WEED("Grimy ranarr weed", 207),
	
	//fishing gear
	SMALL_FISHING_NET("Small fishing net", 303),
	FLY_FISHING_ROD("Fly fishing rod", 309),
	FEATHER("Feather", 314),
	HARPOON("Harpoon", 311),
	
	//other
	COWHIDE("Cowhide", 1739),
	BONES("Bones", 526);
	
	
	private String itemName;
	private int itemID;
	private int requiredLevel;
	
	IaoxItem(String itemName, int itemID){
		this.itemName = itemName;
		this.itemID = itemID;
	}
	
	IaoxItem(String itemName, int itemID, int requiredLevel){
		this.itemName = itemName;
		this.itemID = itemID;
		this.requiredLevel = requiredLevel;
	}
	
	public String getName(){
		return itemName;
	}
	
	public static IaoxItem getItem(String itemName) {
		for(IaoxItem item : IaoxItem.values()){
			if(item.getName().equals(itemName)){
				return item;
			}
		}
		return null;
	}
	
	public static IaoxItem getItem(int itemID) {
		for(IaoxItem item : IaoxItem.values()){
			if(item.getID() == itemID){
				return item;
			}
		}
		return null;
	}
	
	public static List<Integer> getItemIDS(List<IaoxItem> items){
		List<Integer> list = new ArrayList<Integer>();
		items.forEach(item ->{
			list.add(item.getID());
		});
		return list;
	}

	public int getID() {
		return itemID;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}


}
