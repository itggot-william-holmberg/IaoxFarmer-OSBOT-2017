package com.iaox.farmer.data;

import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;

public class Areas {
	public static Area GRAND_EXCHANGE_AREA = new Area(3169,3484,3160,3495);
	
	public static Area FALADOR_TELEPORT_AREA = new Area(new Position[] {
			new Position(2968, 3376, 0),
			new Position(2960, 3376, 0),
			new Position(2958, 3382, 0),
			new Position(2959, 3385, 0),
			new Position(2968, 3386, 0),
			new Position(2970, 3386, 0)
	});
	
	public static Area TAVERLEY_DUNGEON_ENTRANCE = new Area(new Position[] {
			new Position(2887, 3395, 0),
			new Position(2879, 3396, 0),
			new Position(2881, 3402, 0),
			new Position(2890, 3402, 0)
	});
	
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
