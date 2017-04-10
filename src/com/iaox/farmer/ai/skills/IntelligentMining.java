package com.iaox.farmer.ai.skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.mining.MiningAssignment;

public class IntelligentMining {

	private Script script;

	public IntelligentMining(Script script) {
		this.script = script;
	}

	public MiningAssignment getNewAssignment() {
		/*
		 * Should be randomly generated depending on various factors
		 * 
		 * Experience only? Moneymaking only?
		 * 
		 * Switch between these factors to prevent
		 * "typical bot behaviour: camp same spot for 20 hours"
		 * 
		 */
		
		if(IaoxAIO.CURRENT_TASK.getSpecifiedMiningAssignment() != null && IaoxAIO.CURRENT_TASK.getSpecifiedMiningAssignment().getRequiredLevel() <= getMiningLevel()){
			return IaoxAIO.CURRENT_TASK.getSpecifiedMiningAssignment();
		}
		
		if (getMiningLevel() < 15 && getClosestAvailableAssignment(1) != null) {
			return getClosestAvailableAssignment(1);
		}
		
		if (getMiningLevel() < 99 && getClosestAvailableAssignment(15) != null) {
			return getClosestAvailableAssignment(15);
		}
		return MiningAssignment.TIN_ORE_RIMMINGTON;
	}

	private List<MiningAssignment> getAvailableAssignments() {
		List<MiningAssignment> tempList = new ArrayList<MiningAssignment>();
		Arrays.asList(MiningAssignment.values()).forEach(ass -> {
			if (ass.getRequiredLevel() <= getMiningLevel()) {
				tempList.add(ass);
			}
		});
		return tempList;
	}

	private MiningAssignment getClosestAvailableAssignment(int levelRequired) {
		MiningAssignment tempBestAss = null;
		for (MiningAssignment miningAss : getAvailableAssignments()) {
			if (tempBestAss == null && miningAss.getRequiredLevel() == levelRequired) {
				tempBestAss = miningAss;
			} else if (miningAss.getRequiredLevel() == levelRequired
					&& miningAss.getObjectArea().getRandomPosition().distance(script.myPlayer()) < tempBestAss
							.getObjectArea().getRandomPosition().distance(script.myPlayer())) {
				tempBestAss = miningAss;
			}

		}
		return tempBestAss;

	}

	private int getMiningLevel() {
		return script.getSkills().getStatic(Skill.MINING);
	}
}
