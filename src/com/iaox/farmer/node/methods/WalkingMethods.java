package com.iaox.farmer.node.methods;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;

public class WalkingMethods {
private Script script;
	
	public WalkingMethods(Script script){
		this.script = script;
	}

	public void webWalk(Area area){
		IaoxAIO.CURRENT_ACTION = "Webwalking...";
		script.walking.webWalk(area);
	}
}
