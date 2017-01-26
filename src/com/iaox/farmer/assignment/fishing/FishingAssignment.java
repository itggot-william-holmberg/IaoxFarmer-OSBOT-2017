package com.iaox.farmer.assignment.fishing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

import com.iaox.farmer.data.WebBank;
import com.iaox.farmer.data.items.IaoxItem;

public enum FishingAssignment {
	
	SCHRIMPS_LUMBRIDGE_AREA_1(FishingAreas.SHRIMP_LUMBRIDGE_AREA_1, Banks.LUMBRIDGE_UPPER, FishingData.SHRIMP_FISHING_SPOT_IDS, 1, new IaoxItem[]{IaoxItem.SMALL_FISHING_NET}, "Net"),
	FLYFISHING_BARBARIAN_AREA_1(FishingAreas.FLYFISHING_BARBARIAN_AREA_1, Banks.EDGEVILLE, FishingData.FLYFISHING_SPOT_IDS, 20, new IaoxItem[]{IaoxItem.FLY_FISHING_ROD, IaoxItem.FEATHER}, "Lure"),
	HARPOON_SEERS_AREA_1(FishingAreas.SEERS_VILLAGE_FISHING_AREA, WebBank.SEERS.getArea(), FishingData.HARPOON_FISHING_SPOT_IDS, 50, new IaoxItem[]{IaoxItem.HARPOON}, "Harpoon");

	private List<Integer> fishingSpotIDs;
	private Area fishingArea;
	private Area bankArea;
	private int requiredLevel;
	private List<IaoxItem> inventoryItems;
	private String actionName;
	
	/*
	 * Mining area contains whole mining area.
	 * For instance, if you are mining iron ores in rimmington, then the mining area would be rimmington
	 * But the objectArea would only be the specific area that contains the iron ores
	 */
	FishingAssignment(Area fishingArea,  Area bankArea, Integer[] objectIDs, int requiredLevel, IaoxItem[] inventoryItems, String actionName){
		this.fishingArea = fishingArea;
		this.bankArea = bankArea;
		this.fishingSpotIDs = new ArrayList<Integer>();
		Arrays.asList(objectIDs).forEach(object->{
			this.fishingSpotIDs.add(object);
		});
		this.requiredLevel = requiredLevel;
		this.inventoryItems = new ArrayList<IaoxItem>();
		Arrays.asList(inventoryItems).forEach(item->{
			this.inventoryItems.add(item);
		});
		this.actionName = actionName;
	}
	
	public List<Integer> getObjectIDs(){
		return fishingSpotIDs;
	}
	
	public Area getFishingArea(){
		return fishingArea;
	}
	
	public Area getBankArea(){
		return bankArea;
	}
	
	public int getRequiredLevel(){
		return requiredLevel;
	}
	
	public List<IaoxItem> getInventoryItems(){
		return inventoryItems;
	}

	public String getActionName() {
		return actionName;
	}
	
	
}
