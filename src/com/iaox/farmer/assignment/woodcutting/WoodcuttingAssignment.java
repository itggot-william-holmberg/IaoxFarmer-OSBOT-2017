package com.iaox.farmer.assignment.woodcutting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.map.Area;

public enum WoodcuttingAssignment {

	NORMAL_TREE_DRAYNOR_LOCATION_1(WCAreas.WHOLE_DRAYNOR_AREA, WCAreas.NORMAL_TREE_DRAYNOR_LOCATION_1, WCObjectIDs.NORMAL_TREE_ID, 1),
	NORMAL_TREE_DRAYNOR_LOCATION_2(WCAreas.WHOLE_DRAYNOR_AREA, WCAreas.NORMAL_TREE_DRAYNOR_LOCATION_2, WCObjectIDs.NORMAL_TREE_ID, 1),	
	NORMAL_TREE_DRAYNOR_LOCATION_3(WCAreas.WHOLE_DRAYNOR_AREA, WCAreas.NORMAL_TREE_DRAYNOR_LOCATION_3, WCObjectIDs.NORMAL_TREE_ID, 1),
	NORMAL_TREE_DRAYNOR_LOCATION_4(WCAreas.WHOLE_DRAYNOR_AREA, WCAreas.NORMAL_TREE_DRAYNOR_LOCATION_4, WCObjectIDs.NORMAL_TREE_ID, 1),
	
	OAK_TREE_DRAYNOR_LOCATION_1(WCAreas.WHOLE_DRAYNOR_AREA, WCAreas.OAK_TREE_DRAYNOR_LOCATION_1, WCObjectIDs.OAK_TREE_ID, 15),
	OAK_TREE_DRAYNOR_LOCATION_2(WCAreas.WHOLE_DRAYNOR_AREA, WCAreas.OAK_TREE_DRAYNOR_LOCATION_2, WCObjectIDs.OAK_TREE_ID, 15),
	OAK_TREE_DRAYNOR_LOCATION_3(WCAreas.WHOLE_DRAYNOR_AREA, WCAreas.OAK_TREE_DRAYNOR_LOCATION_3, WCObjectIDs.OAK_TREE_ID, 15),
	
	WILLOW_TREE_DRAYNOR_LOCATION_1(WCAreas.WHOLE_DRAYNOR_AREA, WCAreas.WILLOW_TREE_DRAYNOR_LOCATION_1, WCObjectIDs.WILLOW_TREE_ID, 30);

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

	public Area getWholeWCArea() {
		return wholeWCArea;
	}
}
