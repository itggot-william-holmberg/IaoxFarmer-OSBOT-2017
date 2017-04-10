package com.iaox.farmer.ai.skills;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.task.Task;

public class IntelligentCombat {

	private Script script;

	public IntelligentCombat(Script script) {
		this.script = script;
	}

	public FightingAssignment getNewAssignment() {
		// if Data.FightingAssignment != null return Data.fightingassignment

		if (IaoxAIO.CURRENT_TASK.getSpecifiedFightingAssignment() != null) {
			script.log("not null");
			return IaoxAIO.CURRENT_TASK.getSpecifiedFightingAssignment();
		}

		if (IaoxAIO.CURRENT_TASK.getAssignment().getType() == AssignmentType.MELEE) {
			return getNewMeleeAssignment();
		}
		
		if (IaoxAIO.CURRENT_TASK.getAssignment().getType() == AssignmentType.RANGED) {
			return getNewRangeAssignment();
		}

		return FightingAssignment.COWS_LUMBRIDGE;
	}

	private FightingAssignment getNewRangeAssignment() {
		if (script.getSkills().getStatic(Skill.RANGED) < 15) {
			return FightingAssignment.SEAGULL;
		}

		if (script.getSkills().getStatic(Skill.RANGED) < 30) {
			return FightingAssignment.COWS_LUMBRIDGE;
		}

		if (script.getSkills().getStatic(Skill.RANGED) < 99
				&& script.worlds.isMembersWorld()) {
			if (script.getSkills().getStatic(Skill.AGILITY) >= 5) {
				return FightingAssignment.CHAOS_DRUIDS_TAVERLEY;
			} else {
				IaoxAIO.TASK_HANDLER.add(new Task(Assignment.AGILITY, 5, Skill.AGILITY));
			}
		}
		return FightingAssignment.COWS_LUMBRIDGE;
	}

	private FightingAssignment getNewMeleeAssignment() {
		if (script.getSkills().getStatic(Skill.ATTACK) < 15 || script.getSkills().getStatic(Skill.STRENGTH) < 15) {
			return FightingAssignment.SEAGULL;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) < 30 || script.getSkills().getStatic(Skill.STRENGTH) < 30) {
			return FightingAssignment.COWS_LUMBRIDGE;
		}

		if (script.getSkills().getStatic(Skill.ATTACK) < 99 && script.getSkills().getStatic(Skill.STRENGTH) < 99
				&& script.worlds.isMembersWorld()) {
			if (script.getSkills().getStatic(Skill.AGILITY) >= 5) {
				return FightingAssignment.CHAOS_DRUIDS_TAVERLEY;
			} else {
				IaoxAIO.TASK_HANDLER.add(new Task(Assignment.AGILITY, 5, Skill.AGILITY));
			}
		}
		return FightingAssignment.COWS_LUMBRIDGE;

	}

}
