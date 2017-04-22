package com.iaox.farmer.node;

import org.osbot.rs07.script.MethodProvider;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.node.methods.MuleMethods;
import com.iaox.farmer.node.methods.WalkingMethods;
import com.iaox.farmer.node.methods.agility.AgilityMethods;
import com.iaox.farmer.node.methods.combat.CombatMethods;
import com.iaox.farmer.node.methods.fishing.FishingMethods;
import com.iaox.farmer.node.methods.grandexchange.GrandExchangeMethods;
import com.iaox.farmer.node.methods.mining.MiningMethods;
import com.iaox.farmer.node.methods.woodcutting.WoodcuttingMethods;
import com.iaox.farmer.node.methods.woodcutting.crafting.CraftingProvider;
import com.iaox.farmer.node.provider.BankProvider;
import com.iaox.farmer.node.provider.IaoxProvider;
import com.iaox.farmer.node.provider.InventoryProvider;
import com.iaox.farmer.node.provider.ObjectProvider;
import com.iaox.farmer.node.provider.WidgetProvider;

public abstract class Node {

	protected MethodProvider methodProvider;
	protected MiningMethods miningMethods;
	protected BankProvider bankProvider;
	protected WalkingMethods walkingMethods;
	protected GrandExchangeMethods geMethods;
	protected MuleMethods muleMethods;
	protected CombatMethods combatMethods;
	protected WoodcuttingMethods wcMethods;
	protected AgilityMethods agilityMethods;
	protected FishingMethods fishingMethods;
	protected CraftingProvider craftingProvider;
	protected InventoryProvider inventoryProvider;
	protected WidgetProvider widgetProvider;
	protected ObjectProvider objectProvider;
	protected IaoxProvider iaoxProvider;
	
	public Node init(MethodProvider methodProvider){
		this.methodProvider = methodProvider;
		this.iaoxProvider = new IaoxProvider(methodProvider);
		this.inventoryProvider = new InventoryProvider(methodProvider);
		this.widgetProvider = new WidgetProvider(methodProvider);
		this.objectProvider = new ObjectProvider(methodProvider);
		this.miningMethods = new MiningMethods(methodProvider);
		this.bankProvider = new BankProvider(methodProvider);
		this.walkingMethods = new WalkingMethods(methodProvider);
		this.geMethods = new GrandExchangeMethods(methodProvider);
		this.muleMethods = new MuleMethods(methodProvider);
		this.combatMethods = new CombatMethods(methodProvider);
		this.wcMethods = new WoodcuttingMethods(methodProvider);
		this.agilityMethods = new AgilityMethods(methodProvider);
		this.fishingMethods = new FishingMethods(methodProvider);
		this.craftingProvider = new CraftingProvider(methodProvider);
		return this;
	}
	
	
	public abstract boolean active();
	public abstract void execute();
	
	/*
	 * This method makes sure IaoxIntelligence knows whetever it is safe to interrupt a node or not.
	 */
	public abstract boolean safeToInterrupt();
	public abstract String toString();
	
	public void sleeps(int milli){
		try {
			IaoxAIO.sleep(milli);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clickContinue() {
		methodProvider.getDialogues().clickContinue();
		try {
			methodProvider.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean continueMessageIsVisible() {
		if (methodProvider.widgets.getWidgetContainingText("Click here to continue") != null) {
			return true;
		}
		return false;
	}

	public void checkContinue() {
		if (continueMessageIsVisible()) {
			clickContinue();
		}
		return;
	}

}
