package com.iaox.farmer.data;

import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;

public class Areas {
	public static Area GRAND_EXCHANGE_AREA = new Area(3169,3484,3160,3495);
	
	public static Area getClosestArea(Script script, List<Area> areas){
		Area tempBestArea = areas.get(0);
		for(Area currArea : areas) {
			if(currArea.getRandomPosition().distance(script.myPosition()) < tempBestArea.getRandomPosition().distance(script.myPosition())){
				tempBestArea = currArea;
			}
		}
		return tempBestArea;
	}

	public static Area getClosestArea(Script script, List<Area> areas, Area currentArea) {
		Area tempBestArea = null;
		for(Area currArea : areas) {
			if(!currentArea.equals(currArea) && (tempBestArea == null ||currArea.getRandomPosition().distance(script.myPosition()) < tempBestArea.getRandomPosition().distance(script.myPosition()))){
				tempBestArea = currArea;
			}
		}
		return tempBestArea;
	}
}
