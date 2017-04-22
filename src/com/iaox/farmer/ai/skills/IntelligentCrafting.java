package com.iaox.farmer.ai.skills;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.crafting.CraftingAssignment;

public class IntelligentCrafting {

	private Script script;

	public IntelligentCrafting(Script script) {
		this.script = script;
	}

	public CraftingAssignment getNewAssignment() {
		/*
		 * Should be randomly generated depending on various factors
		 * 
		 * Experience only? Moneymaking only?
		 * 
		 * Switch between these factors to prevent
		 * "typical bot behaviour: camp same spot for 20 hours"
		 * 
		 */
		
		if(IaoxAIO.CURRENT_TASK.getSpecifiedCraftingAssignment() != null && IaoxAIO.CURRENT_TASK.getSpecifiedCraftingAssignment().getRequiredLevel() <= getCraftingLevel()){
			return IaoxAIO.CURRENT_TASK.getSpecifiedCraftingAssignment();
		}
		
		if (getCraftingLevel() < 51) {
			return CraftingAssignment.MOLTEN_GLASS;
		}
		
		return CraftingAssignment.SNAKESKIN_CHAPS;
	}

	private int getCraftingLevel() {
		return script.getSkills().getStatic(Skill.CRAFTING);
	}
}
