package com.iaox.farmer.node.mining.methods;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.mining.MiningAssignment;

public class MiningMethods {

	private Script script;
	
	public MiningMethods(Script script){
		this.script = script;
	}
	
	public boolean playerInArea(Area area){
		return area.contains(script.myPlayer());
	}
	
	public boolean readyToMine(){
		return IaoxIntelligence.getMiningAssignment() != null && !script.inventory.isFull();
	}
	
	@SuppressWarnings("unchecked")
	public void mine() {
		IaoxAIO.CURRENT_ACTION = "Lets Mine!";
		MiningAssignment assignment = IaoxIntelligence.getMiningAssignment();
		
		RS2Object ore = script.objects.closest(
				object -> assignment.getObjectIDs().contains(object.getId()) && assignment.getObjectArea().contains(object));
		if (ore != null && ore.interact("Mine")) {
			mineSleep(ore);
		}
	}

	/*
	 * If we are successfully mining, we should do a conditional sleep until we
	 * are ready to mine again.
	 */
	private void mineSleep(RS2Object ore) {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done mining";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(300);
				return ore != null && !ore.exists() && !script.myPlayer().isAnimating();
			}
		}.sleep();
	}
	
	public void mineSleep() {
		IaoxAIO.CURRENT_ACTION = "Lets sleep until we are done mining";
		new ConditionalSleep(5000) {
			@Override
			public boolean condition() throws InterruptedException {
				IaoxAIO.sleep(300);
				return !script.myPlayer().isAnimating();
			}
		}.sleep();
	}
}
