package com.iaox.farmer.node.methods;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.MagicSpell;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.combat.FightingAreas;
import com.iaox.farmer.data.Data;

public class WalkingMethods {
	private Script script;

	public WalkingMethods(Script script) {
		this.script = script;
	}

	// if player position right side of mountain and target position is in
	// leftside, use cammy tab

	public void webWalk(Area area) {
		if (area != null) {
			IaoxAIO.CURRENT_ACTION = "Webwalking...";
			if (area != FightingAreas.CHAOS_DRUIDS_TAVERLEY_AREA && script.myPosition().getY() > 9000
					&& script.inventory.contains("Falador teleport")) {
				breakTablet("Falador teleport");
			}
			// if we are gonna walk to right side of runescape and we are in
			// left side
			else if (area.getRandomPosition().getX() > 2850 && script.myPosition().getX() < 2850
					&& script.inventory.contains("Varrock teleport")) {
				breakTablet("Varrock teleport");
			} else if (area.getRandomPosition().getX() > 2850 && script.myPosition().getX() < 2850
					&& (Data.lastHomeTeleport == 0 || (System.currentTimeMillis() - Data.lastHomeTeleport) > 1800000)) {
				homeTeleport();
			}else{
			script.walking.webWalk(area);
			}
		}
	}

	private void homeTeleport() {
		script.magic.castSpell(Spells.NormalSpells.HOME_TELEPORT);
		try {
			script.sleep(IaoxAIO.random(15000,20000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Data.lastHomeTeleport = System.currentTimeMillis();
	}

	public void walk(Position position) {
		script.walking.walk(position);
	}

	public void webWalk(Position position) {
		if (position != null) {
			IaoxAIO.CURRENT_ACTION = "Webwalking...";
			if (!FightingAreas.CHAOS_DRUIDS_TAVERLEY_AREA.contains(position) && script.myPosition().getY() > 9000
					&& script.inventory.contains("Falador teleport")) {
				script.inventory.interact("Break", "Falador teleport");
				try {
					IaoxAIO.sleep(5000 + IaoxAIO.random(1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				script.walking.webWalk(position);
			}
		}
	}

	public void breakTablet(String tabletName) {
		script.inventory.interact("Break", tabletName);
		try {
			IaoxAIO.sleep(5000 + IaoxAIO.random(1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
