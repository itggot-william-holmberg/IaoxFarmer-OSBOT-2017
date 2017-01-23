package com.iaox.farmer.data.items;

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
	
	//equipment
	ADAMANT_FULL_HELM("Adamant full helm", 1161),
	ADAMANT_PLATEBODY("Adamant platebody", 1123),
	ADAMANT_PLATELEGS("Adamant platelegs", 1073),
	
	//amulets
	AMULET_OF_STRENGTH("Amulet of strength", 1725);
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

	public int getID() {
		return itemID;
	}


}
