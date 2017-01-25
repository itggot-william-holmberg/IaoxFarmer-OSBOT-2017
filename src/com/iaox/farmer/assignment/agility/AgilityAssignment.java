package com.iaox.farmer.assignment.agility;

import java.util.ArrayList;
import java.util.List;

public enum AgilityAssignment {
	
	GNOME(GnomeData.GNOME_OBSTACLES);
	
	private List<AgilityObstacle> AgilityObstacle;
	
	AgilityAssignment(AgilityObstacle[] obstacles){
		this.AgilityObstacle = new ArrayList<AgilityObstacle>();
		for(AgilityObstacle obstacle : obstacles){
			this.AgilityObstacle.add(obstacle);
		}
	}
	
	public List<AgilityObstacle> getObstacles(){
		return AgilityObstacle;
	}

}
