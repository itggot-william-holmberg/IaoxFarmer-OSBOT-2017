package com.iaox.farmer.task;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.fishing.FishingAssignment;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.assignment.woodcutting.WoodcuttingAssignment;

public class Task {

	private Assignment assignment;
	private int goalExperience;
	private Skill skill;
	private MiningAssignment specifiedMiningAssignment;
	private WoodcuttingAssignment specifiedWoodcuttingAssignment;
	private FightingAssignment specifiedFightingAssignment;
	private AgilityAssignment specifiedAgilityAssignment;
	private FishingAssignment specifiedFishingAssignment;

	
	public Task(Assignment assignment) {
		this.assignment = assignment;
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, MiningAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		setSpecifiedMiningAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, WoodcuttingAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		setSpecifiedWoodcuttingAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, FightingAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		setSpecifiedFightingAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, AgilityAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		setSpecifiedAgilityAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, FishingAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		setSpecifiedFishingAssignment(specifiedAssignment);
	}
	
	
	




	public boolean isCompleted(Script script) {
		if(assignment.getType().equals(AssignmentType.QUEST)){
			switch(assignment){
			case TUTORIAL_ISLAND:
				return script.getConfigs().get(281) == 1000;
			}
		}
		return script.getSkills().getExperience(skill) >= goalExperience;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public int getGoalExperience() {
		return goalExperience;
	}
	
	private Skill getSkill() {
		return skill;
	}
	
	public MiningAssignment getSpecifiedMiningAssignment() {
		return specifiedMiningAssignment;
	}
	public void setSpecifiedMiningAssignment(MiningAssignment specifiedMiningAssignment) {
		this.specifiedMiningAssignment = specifiedMiningAssignment;
	}
	public WoodcuttingAssignment getSpecifiedWoodcuttingAssignment() {
		return specifiedWoodcuttingAssignment;
	}
	public void setSpecifiedWoodcuttingAssignment(WoodcuttingAssignment specifiedWoodcuttingAssignment) {
		this.specifiedWoodcuttingAssignment = specifiedWoodcuttingAssignment;
	}
	public FightingAssignment getSpecifiedFightingAssignment() {
		return specifiedFightingAssignment;
	}
	public void setSpecifiedFightingAssignment(FightingAssignment specifiedFightingAssignment) {
		this.specifiedFightingAssignment = specifiedFightingAssignment;
	}
	public AgilityAssignment getSpecifiedAgilityAssignment() {
		return specifiedAgilityAssignment;
	}
	public void setSpecifiedAgilityAssignment(AgilityAssignment specifiedAgilityAssignment) {
		this.specifiedAgilityAssignment = specifiedAgilityAssignment;
	}
	public FishingAssignment getSpecifiedFishingAssignment() {
		return specifiedFishingAssignment;
	}
	public void setSpecifiedFishingAssignment(FishingAssignment specifiedFishingAssignment) {
		this.specifiedFishingAssignment = specifiedFishingAssignment;
	}
	
	public String toString(){
		return  getGoalExperience() + ":" + getSkill();
	}

}
