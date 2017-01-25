package com.iaox.farmer.node.agility.gnome;

import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.agility.AgilityObstacle;
import com.iaox.farmer.node.Node;
import com.iaox.farmer.node.methods.AreaMethods;

public class GnomeCourse extends Node {

	@Override
	public boolean active() {
		return agilityMethods.playerInAgilityArea();
	}

	@Override
	public void execute() {

		for (AgilityObstacle obstacle : AgilityAssignment.GNOME.getObstacles()) {
			if (AreaMethods.playerInArea(obstacle.getArea(), script)) {
				agilityMethods.climbObs(obstacle.getAction(), obstacle.getID());
				sleeps(IaoxAIO.random(1000,2500));
				new ConditionalSleep(5000, 600) {
					@Override
					public boolean condition() throws InterruptedException {
						return agilityMethods.playerInAgilityArea();
					}
				}.sleep();
			}
		}
	}

	@Override
	public boolean safeToInterrupt() {
		return true;
	}

	@Override
	public String toString() {
		return "GnomeCourse";
	}
}
