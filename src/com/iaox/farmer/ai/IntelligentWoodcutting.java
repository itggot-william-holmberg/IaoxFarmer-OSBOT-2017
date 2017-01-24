package com.iaox.farmer.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.aS;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.assignment.woodcutting.WoodcuttingAssignment;
import com.iaox.farmer.data.Areas;

public class IntelligentWoodcutting {

	private Script script;

	public IntelligentWoodcutting(Script script) {
		this.script = script;
	}

	public WoodcuttingAssignment getNewAssignment() {
		/*
		 * Should be randomly generated depending on various factors
		 * 
		 * Experience only? Moneymaking only?
		 * 
		 * Switch between these factors to prevent
		 * "typical bot behaviour: camp same spot for 20 hours"
		 * 
		 */
		if (getClosestAvailableAssignment(getBestTree()) != null) {
			return getClosestAvailableAssignment(getBestTree());
		}

		return WoodcuttingAssignment.NORMAL_TREE_DRAYNOR_LOCATION_1;
	}

	private int getBestTree() {
		if (getWCLevel() < 15) {
			return 1;
		}

		return 15;
	}

	private List<WoodcuttingAssignment> getAvailableAssignments() {
		List<WoodcuttingAssignment> tempList = new ArrayList<WoodcuttingAssignment>();
		Arrays.asList(WoodcuttingAssignment.values()).forEach(ass -> {
			if (ass.getRequiredLevel() <= getWCLevel()) {
				tempList.add(ass);
			} else {
			}
		});
		return tempList;
	}

	private List<WoodcuttingAssignment> getAvailableAssignments(Area treeArea) {
		script.log("Get availableassignents area");
		List<WoodcuttingAssignment> tempList = new ArrayList<WoodcuttingAssignment>();
		getAvailableAssignments().forEach(ass -> {
			if (!ass.getWCArea().equals(treeArea)) {
				script.log("we found a good assignment that is not the currrent one");
				tempList.add(ass);
			} else {
				script.log("damn");
			}
		});
		return tempList;
	}

	private WoodcuttingAssignment getClosestAvailableAssignment(int levelRequired) {
		WoodcuttingAssignment tempBestAss = null;
		for (WoodcuttingAssignment currAss : getAvailableAssignments()) {
			if (tempBestAss == null && currAss.getRequiredLevel() == levelRequired) {
				tempBestAss = currAss;
			} else if (tempBestAss != null
					&& currAss.getWCArea().getRandomPosition().distance(script.myPosition()) < tempBestAss.getWCArea()
							.getRandomPosition().distance(script.myPosition())
					&& currAss.getRequiredLevel() == levelRequired) {
				tempBestAss = currAss;
			}
		}
		return tempBestAss;
	}

	private WoodcuttingAssignment getClosestAvailableAssignment(Area currentArea) {
		script.log("lets get the closest avialable");
		WoodcuttingAssignment tempBestAss = null;
		for (WoodcuttingAssignment currAss : getAvailableAssignments(currentArea)) {
			if (tempBestAss == null
					|| currAss.getWCArea().getRandomPosition().distance(script.myPosition()) < tempBestAss.getWCArea()
							.getRandomPosition().distance(script.myPosition())) {
				script.log("we found a good assignment");
				tempBestAss = currAss;
			}
		}
		return tempBestAss;
	}

	/**
	 * Get the closest area, dont "count" the current area
	 * 
	 * @param areas
	 * @param currentArea
	 * @return
	 */
	private Area getClosestAvailableAssignment(List<Area> areas, Area currentArea) {
		return Areas.getClosestArea(script, areas, currentArea);
	}

	private int getWCLevel() {
		return script.getSkills().getDynamic(Skill.WOODCUTTING);
	}

	public WoodcuttingAssignment getSimilarAssignment(Area currentArea) {
		script.log("lets return");
		return getClosestAvailableAssignment(currentArea);
	}
}
