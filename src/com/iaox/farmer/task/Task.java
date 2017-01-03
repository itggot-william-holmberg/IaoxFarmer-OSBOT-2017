package com.iaox.farmer.task;

import org.osbot.rs07.api.ui.Skill;

import com.iaox.farmer.assignment.Assignment;

public class Task {

	private Assignment assignment;
	private int levelGoal;
	private Skill skill;

	public Task(Assignment assignment, int levelGoal, Skill skill) {
		this.assignment = assignment;
		this.levelGoal = levelGoal;
		this.skill = skill;
	}

	public boolean isCompleted() {
		return false;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public int getLevelGoal() {
		return levelGoal;
	}
	
	private Skill getSkill() {
		return skill;
	}
	
	public String toString(){
		return  getLevelGoal() + ":" + getSkill();
	}

}
