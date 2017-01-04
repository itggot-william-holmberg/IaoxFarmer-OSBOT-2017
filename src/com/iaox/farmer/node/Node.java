package com.iaox.farmer.node;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.node.methods.BankingMethods;
import com.iaox.farmer.node.methods.WalkingMethods;
import com.iaox.farmer.node.mining.methods.MiningMethods;

public abstract class Node {

	protected Script script;
	protected MiningMethods miningMethods;
	protected BankingMethods bankingMethods;
	protected WalkingMethods walkingMethods;
	
	public Node init(Script script){
		this.script = script;
		this.miningMethods = new MiningMethods(script);
		this.bankingMethods = new BankingMethods(script);
		this.walkingMethods = new WalkingMethods(script);
		return this;
	}
	
	
	public abstract boolean active();
	public abstract void execute();
	
	/*
	 * This method makes sure IaoxIntelligence knows whetever it is safe to interrupt a node or not.
	 */
	public abstract boolean safeToInterrupt();
	public abstract String toString();

}
