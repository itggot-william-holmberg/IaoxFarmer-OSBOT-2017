package com.iaox.farmer.node.provider;

import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.data.items.IaoxItem;


public class InventoryProvider extends IaoxProvider{

	public InventoryProvider(MethodProvider methodProvider) {
		super(methodProvider);
	}
	
	public boolean contains(IaoxItem[] items){
		for(IaoxItem item : items){
			if(!methodProvider.inventory.contains(item.getID())){
				return false;
			}
		}
		return true;
	}
	
	public boolean contains(int itemId){
		return methodProvider.inventory.contains(itemId);
	}
	
	public boolean contains(int amount, int itemId){
		return methodProvider.inventory.getAmount(itemId) == amount;
	}
	
	public boolean isSelected(IaoxItem item){
		String selectedItem = methodProvider.inventory.getSelectedItemName();
		return selectedItem != null && selectedItem.equals(item.getName());
	}
	
	public void interact(IaoxItem item){
		methodProvider.log("Lets select item: " + item.getName());
		String selectedItem = methodProvider.inventory.getSelectedItemName();
		methodProvider.inventory.interact("Use", item.getID());
		new ConditionalSleep(3000,600){
			@Override
			public boolean condition() throws InterruptedException {
				return isSelected(item);
			}		
		}.sleep();
	}
	
	public void select(IaoxItem item){
		methodProvider.log("Lets select item: " + item.getName());
		String selectedItem = methodProvider.inventory.getSelectedItemName();
		if(selectedItem == null){
		methodProvider.inventory.interact("Use", item.getID());
		}else{
			methodProvider.inventory.deselectItem();
		}
		new ConditionalSleep(3000,600){
			@Override
			public boolean condition() throws InterruptedException {
				return isSelected(item);
			}		
		}.sleep();
	}
	

}
