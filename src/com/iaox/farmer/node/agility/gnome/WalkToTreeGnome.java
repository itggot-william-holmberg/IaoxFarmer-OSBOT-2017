package com.iaox.farmer.node.agility.gnome;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;

import com.iaox.farmer.assignment.agility.GnomeData;
import com.iaox.farmer.node.Node;


public class WalkToTreeGnome extends Node{

	@Override
	public boolean active() {
		if(!agilityMethods.playerInAgilityArea() && walkingMethods.playerHasCammyTeleport()) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		if(!GnomeData.gnomeGateArea.contains(methodProvider.myPlayer())&& !GnomeData.outsideGnomeAgil.contains(methodProvider.myPlayer())) {
			walkingMethods.webWalk(new Position(2461, 3380, 0));
		}
		else if(GnomeData.gnomeGateArea.contains(methodProvider.myPlayer()) && methodProvider.myPlayer().getPosition().getY() < 3384) {
			if(continueMessageIsVisible()){
				checkContinue();
			}else{
				openGate();
			}
		}
		else if(methodProvider.myPlayer().getPosition().getY() > 3384 && GnomeData.outsideGnomeAgil.contains(methodProvider.myPlayer()) && !GnomeData.gnomeAgilityArea.contains(methodProvider.myPlayer())) {
			walkingMethods.webWalk(new Position(2474, 3438, 0));
		}
		else if(GnomeData.gnomeAgilityArea.contains(methodProvider.myPlayer()) && !agilityMethods.playerInAgilityArea()) {
			walkingMethods.walk(new Position(2474, 3438, 0));
		}
		else {
			try {
				methodProvider.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void openGate()  {
		RS2Object gate = methodProvider.objects.closest("Gate");
		
		if(gate != null) {
			if(gate.isVisible()) {
				gate.interact("Open");
				sleeps(2500);
			}else {
				methodProvider.camera.toEntity(gate);
			}
		}
		
	}

	@Override
	public boolean safeToInterrupt() {
		return false;
	}

	@Override
	public String toString() {
		return "Walk to Tree Gnome";
	}
	
	
}
