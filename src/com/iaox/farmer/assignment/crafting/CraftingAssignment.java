package com.iaox.farmer.assignment.crafting;

import java.util.ArrayList;
import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.data.items.IaoxInventoryItem;
import com.iaox.farmer.data.items.IaoxItem;

public enum CraftingAssignment{
	MOLTEN_GLASS(CraftingData.ITEMS_REQUIRED, IaoxItem.MOLTEN_GLASS,  Banks.EDGEVILLE, CraftingData.FURNACE_AREA, 20, 1),
	SNAKESKIN_CHAPS(CraftingData.REQUIRED_INVENTORY_ITEMS_FOR_SNAKESKIN, IaoxItem.SNAKESKIN_CHAPS, Banks.GRAND_EXCHANGE, Banks.GRAND_EXCHANGE,50, 51);
	
	private List<IaoxInventoryItem> requiredInventoryItems;
	private IaoxItem productItem;
	private Area bankArea;
	private Area craftArea;
	private int xpPerAction;
	private int requiredLevel;
	CraftingAssignment(IaoxInventoryItem[] requiredInventoryItems, IaoxItem productItem, Area bankArea, Area craftArea, int xpPerAction, int requiredLevel){
		this.requiredInventoryItems = new ArrayList<IaoxInventoryItem>();
		for(IaoxInventoryItem item : requiredInventoryItems){
			this.requiredInventoryItems.add(item);
		}
		this.productItem = productItem;
		this.bankArea = bankArea;
		this.craftArea = craftArea;
		this.xpPerAction = xpPerAction;
		this.requiredLevel = requiredLevel;
	}
	
	public List<IaoxInventoryItem> getRequiredInventoryItems() {
		return requiredInventoryItems;
	}
	
	public IaoxItem getProducetItem(){
		return productItem;
	}
	public Area getBankArea(){
		return bankArea;
	}
	
	public Area getActionArea(){
		return craftArea;
	}

	public AssignmentType getAssignmentType() {
		return AssignmentType.SKILL;
	}

	public int getXpPerAction() {
		return xpPerAction;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}
}
