package com.iaox.farmer.node.crafting;

import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.data.items.IaoxInventoryItem;
import com.iaox.farmer.node.Node;

public class CraftingBank extends Node {

	@Override
	public boolean active() {
		return !craftingProvider.hasExactAmountOfRequiredInventoryItems() && craftingProvider.inBankArea();
	}

	public void execute() {
		methodProvider.log("lets execute bank");
		Item itemToDeposit = bankProvider.itemToDeposit(craftingProvider.getRequiredInventoryItems());
		if (itemToDeposit != null) {
			methodProvider.log("Lets deposit: " + itemToDeposit.getName());
			bankProvider.depositAll(itemToDeposit.getName());
		} else {
			IaoxInventoryItem itemToWithdraw = bankProvider
					.itemToWithdraw(craftingProvider.getRequiredInventoryItems());
			if (itemToWithdraw != null) {
				bankProvider.withdrawExact(itemToWithdraw);
			}
		}

	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "CRAFT";
	}

}
