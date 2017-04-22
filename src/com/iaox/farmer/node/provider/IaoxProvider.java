package com.iaox.farmer.node.provider;

import java.util.ArrayList;
import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.data.items.IaoxInventoryItem;

public class IaoxProvider {
	
	public MethodProvider methodProvider;
	
	
	public IaoxProvider(MethodProvider methodProvider){
		this.methodProvider = methodProvider;
	}
	
	public boolean inArea(Area area) {
		return area.contains(methodProvider.myPlayer());
	}
	
	public void conditionalSleepAnimating(int timeout, int sleepCycle){
		new ConditionalSleep(timeout, sleepCycle){

			@Override
			public boolean condition() throws InterruptedException {
				return !methodProvider.myPlayer().isAnimating();
			}
			
		}.sleep();
	}
	
	public void sleep(int milli){
		try {
			methodProvider.sleep(milli);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sleep(int milli, int milli2){
		try {
			methodProvider.sleep(methodProvider.random(milli, milli2));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void conditionalSleep(int initialSleep, int timeout, int sleepCycle, Condition breakCondition){
		sleep(initialSleep);
		new ConditionalSleep(timeout, sleepCycle){

			@Override
			public boolean condition() throws InterruptedException {
				return breakCondition.evaluate();
			}
			
		}.sleep();
	}
	
	public List<Integer> getItemIDs(List<IaoxInventoryItem> list){
		List<Integer> idList = new ArrayList<Integer>();
		for(IaoxInventoryItem item : list){
			idList.add(item.getItem().getID());
		}
		return idList;
	}

}
