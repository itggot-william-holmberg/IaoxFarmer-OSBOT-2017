package com.iaox.farmer.assignment.mining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

public enum MiningAssignment {
	TIN_ORE_RIMMINGTON(MiningAreas.RIMMINGTON_MINING_AREA, MiningAreas.RIMMINGTON_TIN_1, MiningAreas.PORT_SARIM_DEPOSIT_AREA, MiningObjectIDs.tinRockID, 1),
	TIN_ORE_VARROCK(MiningAreas.VARROCK_WEST_MINING_AREA, MiningAreas.VARROCK_TIN_1, Banks.VARROCK_WEST, MiningObjectIDs.tinRockID, 1),
	
	IRON_ORE_RIMMINGTON(MiningAreas.RIMMINGTON_MINING_AREA, MiningAreas.RIMMINGTON_IRON_1, MiningAreas.PORT_SARIM_DEPOSIT_AREA, MiningObjectIDs.ironRockID, 15),
	IRON_ORE_RIMMINGTON_2(MiningAreas.RIMMINGTON_MINING_AREA, MiningAreas.RIMMINGTON_IRON_2, MiningAreas.PORT_SARIM_DEPOSIT_AREA, MiningObjectIDs.ironRockID, 15),
	IRON_ORE_VARROCK(MiningAreas.VARROCK_WEST_MINING_AREA, MiningAreas.VARROCK_IRON_1, Banks.VARROCK_WEST, MiningObjectIDs.ironRockID, 15),

	GOLD_ORE_RIMMINGTON(MiningAreas.RIMMINGTON_MINING_AREA, MiningAreas.RIMMINGTON_GOLD_1, MiningAreas.PORT_SARIM_DEPOSIT_AREA, MiningObjectIDs.goldRockID, 40);

	private List<Integer> objectIDs;
	private Area miningArea;
	private Area objectArea;
	private Area bankArea;
	private int requiredLevel;
	
	/*
	 * Mining area contains whole mining area.
	 * For instance, if you are mining iron ores in rimmington, then the mining area would be rimmington
	 * But the objectArea would only be the specific area that contains the iron ores
	 */
	MiningAssignment(Area miningArea, Area objectArea, Area bankArea, Integer[] objectIDs, int requiredLevel){
		this.miningArea = miningArea;
		this.objectArea = objectArea;
		this.bankArea = bankArea;
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
	
	public Area getBankArea(){
		return bankArea;
	}
	
	public int getRequiredLevel(){
		return requiredLevel;
	}
	
	
}
