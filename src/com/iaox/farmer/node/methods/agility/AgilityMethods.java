package com.iaox.farmer.node.methods.agility;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.agility.AgilityObstacle;
import com.iaox.farmer.assignment.agility.GnomeData;
import com.iaox.farmer.node.methods.AreaMethods;

public class AgilityMethods {

	private Script script;
	private int lastID;
	private int failCheck;

	public AgilityMethods(Script script) {
		this.script = script;
	}

	public AgilityAssignment getAssignment() {
		return IaoxIntelligence.getAgilityAssignment();
	}

	public boolean playerInAgilityArea() {
		if (getAssignment() != null) {
			for (AgilityObstacle obstacle : getAssignment().getObstacles()) {
				if (AreaMethods.playerInArea(obstacle.getArea(), script)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean playerInGnomeAgilityArea(){
		for (AgilityObstacle obstacle : GnomeData.GNOME_OBSTACLES) {
			if (AreaMethods.playerInArea(obstacle.getArea(), script)) {
				return true;
			}
		}
		return false;
	}

	public void climbObs(String obsAction, int obsName) {
		RS2Object obs = script.objects.closest(obsName);

		if (obs != null && lastID != obs.getId()) {
			if (obs.isVisible()) {
				obs.interact(obsAction);
				lastID = obs.getId();
				script.log(lastID);
				failCheck = 0;
				script.camera.moveYaw(script.camera.getYawAngle() + 30);
			} else {
				script.camera.toEntity(obs);
				script.camera.moveYaw(script.camera.getYawAngle() + 30);
				script.log("Moving camera to obs");
			}
		} else {
			if (failCheck >= 5) {
				lastID = 0;
			}
			failCheck++;
		}

	}

}
