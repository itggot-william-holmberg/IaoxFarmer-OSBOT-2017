package com.iaox.farmer.data.items;

public enum IaoxItem {
	
	BRONZE_PICKAXE("Bronze pickaxe",1265), MITHRIL_PICKAXE("Mithril pickaxe",1273), 
	ADAMANT_PICKAXE("Adamant pickaxe",1271), RUNE_PICKAXE("Rune pickaxe",1275),
	
	IRON_ORE("Iron ore", 440);
	
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
