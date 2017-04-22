package com.iaox.farmer.data.items;


public class IaoxInventoryItem {

	private int amount;
	private IaoxItem item;
	private int requiredAmount;
	public IaoxInventoryItem(int amount, IaoxItem item, int requiredAmount){
		this.amount = amount;
		this.item = item;
		this.requiredAmount = requiredAmount;
	}
	
	public IaoxInventoryItem(int amount, IaoxItem item){
		this.amount = amount;
		this.item = item;
		this.requiredAmount = 1;
	}
	public IaoxInventoryItem(IaoxItem item){
		this.amount = 1;
		this.item = item;
		this.requiredAmount = 1;
	}
	
	public int getAmount(){
		return amount;
	}
	
	public IaoxItem getItem(){
		return item;
	}
	public int getRequiredAmount() {
		return requiredAmount;
	}
}
