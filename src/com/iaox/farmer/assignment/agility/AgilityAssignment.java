package com.iaox.farmer.assignment.agility;

import java.util.ArrayList;
import java.util.List;

public enum AgilityAssignment {
	
	GNOME(GnomeData.GNOME_OBSTACLES, 1);
	
	private List<AgilityObstacle> AgilityObstacle;
	private int requiredLevel;
	AgilityAssignment(AgilityObstacle[] obstacles, int requiredLevel){
		this.AgilityObstacle = new ArrayList<AgilityObstacle>();
		for(AgilityObstacle obstacle : obstacles){
			this.AgilityObstacle.add(obstacle);
		}
		this.requiredLevel = requiredLevel;
	}
	
	public List<AgilityObstacle> getObstacles(){
		return AgilityObstacle;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

}
