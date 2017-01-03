package com.iaox.farmer.node;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.ai.IaoxIntelligence;

public abstract class Node {

	protected Script script;
	
	public Node init(Script script){
		this.script = script;
		return this;
	}
	
	
	public abstract boolean active();
	public abstract void execute();
	
	/*
	 * This method makes sure IaoxIntelligence knows whetever it is safe to interrupt a node or not.
	 */
	public abstract boolean safeToInterrupt();
	public abstract String toString();
	
	
	
	//player methods
	public boolean playerInArea(Area area){
		return area.contains(script.myPlayer());
	}
	
	//mining methods
	public boolean readyToMine(){
		return IaoxIntelligence.getMiningAssignment() != null;
	}

}
