package com.iaox.farmer.assignment.crafting;

import org.osbot.rs07.api.map.Area;

import com.iaox.farmer.data.items.IaoxInventoryItem;
import com.iaox.farmer.data.items.IaoxItem;

public class CraftingData {
	public static IaoxInventoryItem[] REQUIRED_INVENTORY_ITEMS_FOR_SNAKESKIN= {new IaoxInventoryItem(IaoxItem.NEEDLE), 
		    new IaoxInventoryItem(100, IaoxItem.THREAD),
		    new IaoxInventoryItem(26, IaoxItem.SNAKESKIN, 12)};

public static IaoxInventoryItem[] ITEMS_REQUIRED = {new IaoxInventoryItem(14, IaoxItem.BUCKET_OF_SAND), 
		    new IaoxInventoryItem(14,IaoxItem.SODA_ASH)};

public static Area FURNACE_AREA = new Area(3110,3501,3105,3496);
}
