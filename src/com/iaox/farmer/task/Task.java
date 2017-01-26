package com.iaox.farmer.task;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.fishing.FishingAssignment;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.assignment.woodcutting.WoodcuttingAssignment;

public class Task {

	private Assignment assignment;
	private int levelGoal;
	private Skill skill;
	private MiningAssignment specifiedMiningAssignment;
	private WoodcuttingAssignment specifiedWoodcuttingAssignment;
	private FightingAssignment specifiedFightingAssignment;
	private AgilityAssignment specifiedAgilityAssignment;
	private FishingAssignment specifiedFishingAssignment;

	public Task(Assignment assignment, int levelGoal, Skill skill) {
		this.assignment = assignment;
		this.levelGoal = levelGoal;
		this.skill = skill;
	}
	
	public Task(Assignment assignment, int levelGoal, Skill skill, MiningAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.levelGoal = levelGoal;
		this.skill = skill;
		setSpecifiedMiningAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int levelGoal, Skill skill, WoodcuttingAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.levelGoal = levelGoal;
		this.skill = skill;
		setSpecifiedWoodcuttingAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int levelGoal, Skill skill, FightingAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.levelGoal = levelGoal;
		this.skill = skill;
		setSpecifiedFightingAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int levelGoal, Skill skill, AgilityAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.levelGoal = levelGoal;
		this.skill = skill;
		setSpecifiedAgilityAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int levelGoal, Skill skill, FishingAssignment specifiedAssignment) {
		this.assignment = assignment;
		this.levelGoal = levelGoal;
		this.skill = skill;
		setSpecifiedFishingAssignment(specifiedAssignment);
	}
	
	
	




	public boolean isCompleted(Script script) {
		return script.getSkills().getStatic(skill) >= levelGoal;
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
		return  getLevelGoal() + ":" + getSkill();
	}

}
