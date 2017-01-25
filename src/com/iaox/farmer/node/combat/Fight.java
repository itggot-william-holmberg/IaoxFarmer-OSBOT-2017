package com.iaox.farmer.node.combat;

import org.osbot.rs07.api.ui.Skill;

import com.iaox.farmer.node.Node;


public class Fight extends Node {

	@Override
	public boolean active() {
		if (combatMethods.playerInFightArea() && combatMethods.playerIsReadyForFight()) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		if(combatMethods.playerHasToEat()){
			combatMethods.eat();
		} else if(!combatMethods.playerCanAttack()){
			combatMethods.combatSleep();
		} else if (combatMethods.lootIsAvailable()) {
			combatMethods.loot();
		} else {
			combatMethods.fight();
		}
	}
	
	public String toString() {
		return "Fight";
	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}
	

}
