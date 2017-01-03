package com.iaox.farmer.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.assignment.mining.MiningAssignment;

public class IntelligentMining {
	
	private Script script;
	public IntelligentMining(Script script){
		this.script = script;
	}
	public MiningAssignment getNewAssignment(){
		return MiningAssignment.IRON_ORE_RIMMINGTON;
	}
	
	private List<MiningAssignment> getAvailableAssignments(){
		List<MiningAssignment> tempList = new ArrayList<MiningAssignment>();
		Arrays.asList(MiningAssignment.values()).forEach(ass->{
			if(ass.getRequiredLevel() > getMiningLevel()){
				tempList.add(ass);
			}
		});
		return tempList;
	}

	private int getMiningLevel(){
		return script.getSkills().getDynamic(Skill.MINING);
	}
}