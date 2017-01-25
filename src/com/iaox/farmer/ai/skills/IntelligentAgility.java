package com.iaox.farmer.ai.skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.mining.MiningAssignment;

public class IntelligentAgility {
	
	private Script script;
	public IntelligentAgility(Script script){
		this.script = script;
	}
	public AgilityAssignment getNewAssignment(){
		/*
		 * Should be randomly generated depending on various factors
		 * 
		 * Experience only?
		 * Moneymaking only?
		 * 
		 * Switch between these factors to prevent "typical bot behaviour: camp same spot for 20 hours"
		 * 
		 */
		return AgilityAssignment.GNOME;

	}
	
	
	
	

	private int getAgilityLevel(){
		return script.getSkills().getStatic(Skill.AGILITY);
	}
}
