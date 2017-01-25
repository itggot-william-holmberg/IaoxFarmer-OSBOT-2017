package com.iaox.farmer.node;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.node.methods.BankingMethods;
import com.iaox.farmer.node.methods.MuleMethods;
import com.iaox.farmer.node.methods.WalkingMethods;
import com.iaox.farmer.node.methods.agility.AgilityMethods;
import com.iaox.farmer.node.methods.combat.CombatMethods;
import com.iaox.farmer.node.methods.grandexchange.GrandExchangeMethods;
import com.iaox.farmer.node.methods.mining.MiningMethods;
import com.iaox.farmer.node.methods.woodcutting.WoodcuttingMethods;

public abstract class Node {

	protected Script script;
	protected MiningMethods miningMethods;
	protected BankingMethods bankingMethods;
	protected WalkingMethods walkingMethods;
	protected GrandExchangeMethods geMethods;
	protected MuleMethods muleMethods;
	protected CombatMethods combatMethods;
	protected WoodcuttingMethods wcMethods;
	protected AgilityMethods agilityMethods;
	
	public Node init(Script script){
		this.script = script;
		this.miningMethods = new MiningMethods(script);
		this.bankingMethods = new BankingMethods(script);
		this.walkingMethods = new WalkingMethods(script);
		this.geMethods = new GrandExchangeMethods(script);
		this.muleMethods = new MuleMethods(script);
		this.combatMethods = new CombatMethods(script);
		this.wcMethods = new WoodcuttingMethods(script);
		this.agilityMethods = new AgilityMethods(script);
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
		script.getDialogues().clickContinue();
		try {
			script.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean continueMessageIsVisible() {
		if (script.widgets.getWidgetContainingText("Click here to continue") != null) {
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
