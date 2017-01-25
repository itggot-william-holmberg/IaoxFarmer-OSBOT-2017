package com.iaox.farmer.ai.skills;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.assignment.combat.FightingAssignment;

public class IntelligentCombat {
	
	private Script script;
	
	public IntelligentCombat(Script script){
		this.script = script;
	}

	public FightingAssignment getNewAssignment() {
		//if Data.FightingAssignment != null return Data.fightingassignment
		
		
		if(script.getSkills().getStatic(Skill.ATTACK) > 30  && 
				script.getSkills().getStatic(Skill.STRENGTH) > 30 &&
					script.worlds.isMembersWorld() && 
						script.getSkills().getStatic(Skill.AGILITY) >= 5){
			return FightingAssignment.CHAOS_DRUIDS_TAVERLEY;
		}
		return FightingAssignment.SEAGULL;
	}

}
