package com.iaox.farmer.node.provider;

import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

public class WidgetProvider extends IaoxProvider{

	public WidgetProvider(MethodProvider methodProvider) {
		super(methodProvider);
	}
	
	public boolean isVisible(int rootId){
		return methodProvider.widgets.isVisible(rootId);
	}
	
	public boolean isVisible(int rootId, int childId){
		return methodProvider.widgets.isVisible(rootId, childId);
	}
	
	public boolean isVisible(int rootId, int childId, int superChildId){
		return methodProvider.widgets.isVisible(rootId, childId, superChildId);
	}
	
	public void interact(int rootId, int childId, String action){
		methodProvider.widgets.interact(rootId, childId, action);
	}
	
	public void makeAll(int rootId, int childId){
		methodProvider.widgets.interact(rootId, childId, "Make All");
	}

}
