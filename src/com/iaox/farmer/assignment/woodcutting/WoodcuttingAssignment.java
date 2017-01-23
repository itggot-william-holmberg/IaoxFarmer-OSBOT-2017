package com.iaox.farmer.assignment.woodcutting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.map.Area;

public enum WoodcuttingAssignment {

	//FIX WHOLE WC AREA
	NORMAL_TREE_DRAYNOR_LOCATION_1(null, WCAreas.NORMAL_TREE_DRAYNOR_LOCATION_1, WCObjectIDs.NORMAL_TREE_ID, 1);
//	OAK_TREE_DRAYNOR_LOCATION_1(W);
	private Area wholeWCArea;
	private Area wcArea;
	private List<Integer> objectIDs;
	private int requiredLevel;
	
	WoodcuttingAssignment(Area wholeWCArea, Area treeArea, Integer[] objectIDs, int requiredLevel){
		this.wholeWCArea = wholeWCArea;
		this.wcArea = treeArea;
		this.objectIDs = new ArrayList<Integer>();
		Arrays.asList(objectIDs).forEach(object->{
			this.objectIDs.add(object);
		});
		this.requiredLevel = requiredLevel;
	}
	
	public Area getWCArea(){
		return wcArea;
	}
	
	public List<Integer> getObjectIDs(){
		return objectIDs;
	}
	
	public int getRequiredLevel(){
		return requiredLevel;
	}

	public Object getWholeWCArea() {
		return wholeWCArea;
	}
}
