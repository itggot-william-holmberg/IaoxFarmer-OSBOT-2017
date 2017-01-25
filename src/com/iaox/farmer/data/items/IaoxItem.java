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
	ADAMANT_FULL_HELM("Adamant full helm", 1161),
	ADAMANT_PLATEBODY("Adamant platebody", 1123),
	ADAMANT_PLATELEGS("Adamant platelegs", 1073),
	
	MONKS_BOTTOM_ROBE("Monk's robe", 542),
	MONKS_TOP_ROBE("Monk's robe", 544),
	DESERT_BOOTS("Desert boots", 1837),
	RED_HAT("Red hat", 2910),
	
	
	//amulets
	AMULET_OF_STRENGTH("Amulet of strength", 1725),
	
	//teleport tablets and runes
	FALADOR_TELEPORT("Falador teleport", 8009),
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
	
	//other
	COWHIDE("Cowhide", 1739);
	
	
	private String itemName;
	private int itemID;
	
	IaoxItem(String itemName, int itemID){
		this.itemName = itemName;
		this.itemID = itemID;
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


}
