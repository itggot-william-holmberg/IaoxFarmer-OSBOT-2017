package com.iaox.farmer.assignment.mining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.map.Area;

public enum MiningAssignment {
	IRON_ORE_RIMMINGTON(MiningAreas.RIMMINGTON_MINING_AREA, MiningAreas.RIMMINGTON_IRON_1,MiningObjectIDs.ironOreID, 15);
	
	private List<Integer> objectIDs;
	private Area miningArea;
	private Area objectArea;
	private int requiredLevel;
	
	/*
	 * Mining area contains whole mining area.
	 * For instance, if you are mining iron ores in rimmington, then the mining area would be rimmington
	 * But the objectArea would only be the specific area that contains the iron ores
	 */
	MiningAssignment(Area miningArea, Area objectArea, Integer[] objectIDs, int requiredLevel){
		this.miningArea = miningArea;
		this.objectArea = objectArea;
		this.objectIDs = new ArrayList<Integer>();
		Arrays.asList(objectIDs).forEach(object->{
			this.objectIDs.add(object);
		});
		this.requiredLevel = requiredLevel;
	}
	
	public List<Integer> getObjectIDs(){
		return objectIDs;
	}
	
	public Area getMiningArea(){
		return miningArea;
	}
	
	public Area getObjectArea(){
		return objectArea;
	}
	
	public int getRequiredLevel(){
		return requiredLevel;
	}
	
	
}
