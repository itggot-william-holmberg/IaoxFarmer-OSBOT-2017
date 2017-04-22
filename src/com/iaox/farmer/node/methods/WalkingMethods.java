package com.iaox.farmer.node.methods;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.MagicSpell;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.combat.FightingAreas;
import com.iaox.farmer.data.Areas;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.data.items.IaoxItem;
import com.iaox.farmer.node.methods.agility.AgilityMethods;

public class WalkingMethods {
	private MethodProvider methodProvider;
	private AgilityMethods agilityMethods;

	public WalkingMethods(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		this.agilityMethods = new AgilityMethods(methodProvider);
	}
	
	public void webWalkGE(Area area){
		methodProvider.walking.webWalk(area);
	}

	// if player position right side of mountain and target position is in
	// leftside, use cammy tab

	public void webWalk(Area area) {
		if (area != null) {
			IaoxAIO.CURRENT_ACTION = "Webwalking...";
			if (shouldTeleportFromTaverleyDung(area) || shouldTeleportToFalador(area)) {
				breakTablet("Falador teleport");
			}
			// if we are gonna walk to right side of runescape and we are in
			// left side
			else if (playerIsLeftOfMountain(area)
					&& methodProvider.inventory.contains("Varrock teleport")) {
				breakTablet("Varrock teleport");
			} else if (playerIsLeftOfMountain(area)
					&& (Data.lastHomeTeleport == 0 || (System.currentTimeMillis() - Data.lastHomeTeleport) > 1800000)
					|| agilityMethods.playerInGnomeAgilityArea()) {
				homeTeleport();
			} else {
				methodProvider.walking.webWalk(area);
			}
		}
	}
	
	private boolean playerIsLeftOfMountain(Area area){
		return area.getRandomPosition().getX() > 2850 && methodProvider.myPosition().getX() < 2850;
	}

	private boolean shouldTeleportToFalador(Area area) {
		// if falador teleport spot is closer than walk the whole distance
		return FightingAreas.CHAOS_DRUIDS_TAVERLEY_AREA.equals(area) && !Areas.FALADOR_TELEPORT_AREA.contains(methodProvider.myPlayer())
				&& methodProvider.getSkills().getStatic(Skill.AGILITY) >= 5
				&& Areas.FALADOR_TELEPORT_AREA.getRandomPosition()
						.distance(Areas.TAVERLEY_DUNGEON_ENTRANCE.getRandomPosition()) < Areas.TAVERLEY_DUNGEON_ENTRANCE
								.getRandomPosition().distance(methodProvider.myPlayer());
	}

	private boolean shouldTeleportFromTaverleyDung(Area area) {
		return area != FightingAreas.CHAOS_DRUIDS_TAVERLEY_AREA && playerInTaverleyDung()
				&& playerHasFallyTeleport();
	}

	private void homeTeleport() {
		methodProvider.magic.castSpell(Spells.NormalSpells.HOME_TELEPORT);
		try {
			methodProvider.sleep(IaoxAIO.random(15000, 20000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Data.lastHomeTeleport = System.currentTimeMillis();
	}

	public void walk(Position position) {
		methodProvider.walking.walk(position);
	}

	public void webWalk(Position position) {
		if (position != null) {
			IaoxAIO.CURRENT_ACTION = "Webwalking...";
			if (!FightingAreas.CHAOS_DRUIDS_TAVERLEY_AREA.contains(position) && methodProvider.myPosition().getY() > 9000
					&& playerHasFallyTeleport()) {
				methodProvider.inventory.interact("Break", "Falador teleport");
				try {
					IaoxAIO.sleep(5000 + IaoxAIO.random(1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				methodProvider.walking.webWalk(position);
			}
		}
	}

	public void breakTablet(String tabletName) {
		if (methodProvider.widgets.closeOpenInterface()) {
			methodProvider.inventory.interact("Break", tabletName);
			try {
				IaoxAIO.sleep(5000 + IaoxAIO.random(1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean playerInTaverleyDung() {
		return methodProvider.myPosition().getY() > 9000;
	}
	
	public boolean playerHasVarrockTeleport() {
		if(methodProvider.inventory.contains("Varrock teleport")){
			return true;
		}
		if(!Data.WITHDRAW_LIST.contains(IaoxItem.VARROCK_TELEPORT)){
			Data.WITHDRAW_LIST.add(IaoxItem.VARROCK_TELEPORT);
		}
		return false;
	}
	
	public boolean playerHasFallyTeleport() {
		if(methodProvider.inventory.contains("Falador teleport")){
			return true;
		}
		if(!Data.WITHDRAW_LIST.contains(IaoxItem.FALADOR_TELEPORT)){
			Data.WITHDRAW_LIST.add(IaoxItem.FALADOR_TELEPORT);
		}
		return false;
	}

	public boolean playerHasCammyTeleport() {
		if(methodProvider.inventory.contains("Camelot teleport")){
			return true;
		}
		if(!Data.WITHDRAW_LIST.contains(IaoxItem.CAMELOT_TELEPORT)){
			Data.WITHDRAW_LIST.add(IaoxItem.CAMELOT_TELEPORT);
		}
		return false;
	}
}
