package com.iaox.farmer.task;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.crafting.CraftingAssignment;
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
	private CraftingAssignment specifiedCraftingAssignment;
	private long playTime;
	private long breakTime;

	
	public Task(Assignment assignment) {
		this.assignment = assignment;
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, long playTime) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		this.playTime = playTime;
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, MiningAssignment specifiedAssignment, long playTime) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		this.setPlayTime(playTime);
		setSpecifiedMiningAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, WoodcuttingAssignment specifiedAssignment, long playTime) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		this.setPlayTime(playTime);
		setSpecifiedWoodcuttingAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, FightingAssignment specifiedAssignment, long playTime) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		this.setPlayTime(playTime);
		setSpecifiedFightingAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, AgilityAssignment specifiedAssignment, long playTime) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		this.setPlayTime(playTime);
		setSpecifiedAgilityAssignment(specifiedAssignment);
	}
	
	public Task(Assignment assignment, int goalExperience, Skill skill, FishingAssignment specifiedAssignment, long playTime) {
		this.assignment = assignment;
		this.goalExperience = goalExperience;
		this.skill = skill;
		this.setPlayTime(playTime);
		setSpecifiedFishingAssignment(specifiedAssignment);
	}
	
	public boolean isCompleted(Script script) {
		
		//if the task has a time session, check if it is done.
		if(breakTime > 0 && breakTime < System.currentTimeMillis()){
			return true;
		}
		
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
	
	public CraftingAssignment getSpecifiedCraftingAssignment() {
		return specifiedCraftingAssignment;
	}
	public void setSpecifiedCraftingAssignment(CraftingAssignment specifiedCraftingAssignment) {
		this.specifiedCraftingAssignment = specifiedCraftingAssignment;
	}
	
	public String toString(){
		return getGoalExperience() + ":" + getSkill();
	}

	public long getPlayTime() {
		return playTime;
	}

	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}
	
	public long getBreakTime(){
		return breakTime;
	}
	
	public void setBreakTime(long playTime){
		this.breakTime = System.currentTimeMillis() + playTime;
	}

}
