package com.iaox.farmer.assignment;

import org.osbot.rs07.api.ui.Skill;

public enum Assignment {
	MINING(AssignmentType.SKILL, Skill.MINING);
	
	private AssignmentType type;
	private Skill skill;
	
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
