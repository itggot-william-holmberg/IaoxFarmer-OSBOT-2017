package com.iaox.farmer.node.methods.woodcutting.crafting;

import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.assignment.crafting.CraftingAssignment;
import com.iaox.farmer.data.items.IaoxInventoryItem;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.task.Task;

public class CraftingProvider {
	
	public MethodProvider methodProvider;
	
	
	public CraftingProvider(MethodProvider methodProvider){
		this.methodProvider = methodProvider;
	}
	
	public CraftingAssignment getCraftingAssignment() {
		CraftingAssignment assignment = IaoxIntelligence.getCraftingAssignment();
		if(assignment != null){
			return assignment;
		}
		return null;
	}
	
	public Area getActionArea(){
		return getCraftingAssignment().getActionArea();
	}
	public Area getBankArea(){
		return getCraftingAssignment().getBankArea();
	}
	
	public boolean hasExactAmountOfRequiredInventoryItems(){
		if(getCraftingAssignment().getRequiredInventoryItems() != null){
			for(IaoxInventoryItem item : getCraftingAssignment().getRequiredInventoryItems()){
				if(methodProvider.inventory.getAmount(item.getItem().getID()) != item.getAmount()){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean hasRequiredInventoryItems(){
		if(getCraftingAssignment().getRequiredInventoryItems() != null){
			for(IaoxInventoryItem item : getCraftingAssignment().getRequiredInventoryItems()){
				if(methodProvider.inventory.getAmount(item.getItem().getID()) < item.getRequiredAmount()){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean inBankArea(){
		Area bankArea = getBankArea();
		if(bankArea != null){
			return bankArea.contains(methodProvider.myPlayer());
		}//else, find closest webbank
		return false;
	}

	public boolean inActionArea() {
		Area actionArea = getActionArea();
		if(actionArea != null){
			return actionArea.contains(methodProvider.myPlayer());
		}//else, find closest webbank
		return false;
	}

	public IaoxItem getProductItem() {
		return getCraftingAssignment().getProducetItem();
	}
	
	public List<IaoxInventoryItem> getRequiredInventoryItems(){
		return getCraftingAssignment().getRequiredInventoryItems();
	}


}
