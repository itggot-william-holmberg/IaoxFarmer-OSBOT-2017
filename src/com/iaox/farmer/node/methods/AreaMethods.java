package com.iaox.farmer.node.methods;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;

public class AreaMethods {

	private Script script;
	
	public AreaMethods(Script script){
		this.script = script;
	}
	
	public static boolean playerInArea(Area area, Script script) {
		return area.contains(script.myPlayer());
	}
}
