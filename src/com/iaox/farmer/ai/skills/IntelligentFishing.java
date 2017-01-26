package com.iaox.farmer.ai.skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.fishing.FishingAssignment;

public class IntelligentFishing {

	private Script script;

	public IntelligentFishing(Script script) {
		this.script = script;
	}

	public FishingAssignment getNewAssignment() {
		/*
		 * Should be randomly generated depending on various factors
		 * 
		 * Experience only? Moneymaking only?
		 * 
		 * Switch between these factors to prevent
		 * "typical bot behaviour: camp same spot for 20 hours"
		 * 
		 */
		
		if(IaoxAIO.CURRENT_TASK.getSpecifiedFishingAssignment() != null && IaoxAIO.CURRENT_TASK.getSpecifiedFishingAssignment().getRequiredLevel() <= getFishingLevel()){
			return IaoxAIO.CURRENT_TASK.getSpecifiedFishingAssignment();
		}
		
		if (getFishingLevel() < 20) {
			return FishingAssignment.SCHRIMPS_LUMBRIDGE_AREA_1;
		}
		
		return FishingAssignment.FLYFISHING_BARBARIAN_AREA_1;
	}

	private List<FishingAssignment> getAvailableAssignments() {
		List<FishingAssignment> tempList = new ArrayList<FishingAssignment>();
		Arrays.asList(FishingAssignment.values()).forEach(ass -> {
			if (ass.getRequiredLevel() < getFishingLevel()) {
				tempList.add(ass);
			}
		});
		return tempList;
	}

	private FishingAssignment getClosestAvailableAssignment(int levelRequired) {
		FishingAssignment tempBestAss = null;
		for (FishingAssignment FishingAss : getAvailableAssignments()) {
			if (tempBestAss == null && FishingAss.getRequiredLevel() == levelRequired) {
				tempBestAss = FishingAss;
			} else if (FishingAss.getRequiredLevel() == levelRequired
					&& FishingAss.getFishingArea().getRandomPosition().distance(script.myPlayer()) < tempBestAss
							.getFishingArea().getRandomPosition().distance(script.myPlayer())) {
				tempBestAss = FishingAss;
			}

		}
		return tempBestAss;

	}

	private int getFishingLevel() {
		return script.getSkills().getStatic(Skill.FISHING);
	}
}
