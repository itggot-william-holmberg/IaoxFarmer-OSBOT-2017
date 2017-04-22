package com.iaox.farmer.node.provider;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;


public class ObjectProvider extends IaoxProvider{

	public ObjectProvider(MethodProvider methodProvider) {
		super(methodProvider);
	}
	
	public RS2Object get(int objectId){
		return methodProvider.objects.closest(objectId);
	}
	public boolean exists(RS2Object object){
		return object != null;
	}
	
	public void interact(String action, RS2Object object){
		if(exists(object)){
			object.interact(action);
		}
	}
	
	

}
