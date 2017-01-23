package com.iaox.farmer.ai;

import org.osbot.rs07.script.Script;

import com.iaox.farmer.assignment.combat.FightingAssignment;

public class IntelligentCombat {
	
	private Script script;
	
	public IntelligentCombat(Script script){
		this.script = script;
	}

	public FightingAssignment getNewAssignment() {
		return FightingAssignment.SEAGULL;
	}

}
