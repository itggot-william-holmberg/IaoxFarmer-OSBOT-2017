package com.iaox.farmer.assignment;

import org.osbot.rs07.api.ui.Skill;


public enum Assignment {
	MINING(AssignmentType.SKILL, Skill.MINING),
	WOODCUTTING(AssignmentType.SKILL, Skill.WOODCUTTING),
	AGILITY(AssignmentType.SKILL, Skill.AGILITY),
	STRENGTH(AssignmentType.MELEE, Skill.STRENGTH),
	RANGE(AssignmentType.MELEE, Skill.RANGED),
	ATTACK(AssignmentType.MELEE, Skill.ATTACK),
	DEFENCE(AssignmentType.MELEE, Skill.DEFENCE), 
	FISHING(AssignmentType.SKILL, Skill.FISHING),
	TUTORIAL_ISLAND(AssignmentType.QUEST);
	
	private AssignmentType type;
	private Skill skill;
	

	Assignment(AssignmentType type) {
		this.type = type;
	}
	Assignment(AssignmentType type, Skill skill) {
		this.type = type;
		this.skill = skill;
	}


	public AssignmentType getType() {
		return type;
	}

	public Skill getSkill() {
		return skill;
	}

	
	

}
