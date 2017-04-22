package com.iaox.farmer.node.methods.agility;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;

import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.agility.AgilityObstacle;
import com.iaox.farmer.assignment.agility.GnomeData;
import com.iaox.farmer.node.methods.AreaMethods;

public class AgilityMethods {

	private MethodProvider methodProvider;
	private int lastID;
	private int failCheck;

	public AgilityMethods(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	public AgilityAssignment getAssignment() {
		return IaoxIntelligence.getAgilityAssignment();
	}

	public boolean playerInAgilityArea() {
		if (getAssignment() != null) {
			for (AgilityObstacle obstacle : getAssignment().getObstacles()) {
				if (AreaMethods.playerInArea(obstacle.getArea(), methodProvider)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean playerInGnomeAgilityArea(){
		for (AgilityObstacle obstacle : GnomeData.GNOME_OBSTACLES) {
			if (AreaMethods.playerInArea(obstacle.getArea(), methodProvider)) {
				return true;
			}
		}
		return false;
	}

	public void climbObs(String obsAction, int obsName) {
		RS2Object obs = methodProvider.objects.closest(obsName);

		if (obs != null && lastID != obs.getId()) {
			if (obs.isVisible()) {
				obs.interact(obsAction);
				lastID = obs.getId();
				methodProvider.log(lastID);
				failCheck = 0;
				methodProvider.camera.moveYaw(methodProvider.camera.getYawAngle() + 30);
			} else {
				methodProvider.camera.toEntity(obs);
				methodProvider.camera.moveYaw(methodProvider.camera.getYawAngle() + 30);
				methodProvider.log("Moving camera to obs");
			}
		} else {
			if (failCheck >= 5) {
				lastID = 0;
			}
			failCheck++;
		}

	}

}
