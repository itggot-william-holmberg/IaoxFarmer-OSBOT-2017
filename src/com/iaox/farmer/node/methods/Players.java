package com.iaox.farmer.node.methods;

import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.script.Script;

public class Players {

	private Script script;

	public Players(Script script) {
		this.script = script;
	}

	public int playersInArea(Area area) {
		List<Player> players = script.players.getAll();
		int playersInArea = 0;
		for (Player player : players) {
			if (area.contains(player)) {
				playersInArea++;
			}
		}
		return playersInArea;
	}
}
