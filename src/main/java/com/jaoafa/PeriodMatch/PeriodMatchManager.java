package com.jaoafa.PeriodMatch;

import org.bukkit.entity.Player;

import com.jaoafa.PeriodMatch.Command.Period;

public class PeriodMatchManager implements PeriodMatchAPI {

	@Override
	public boolean getPeriodRunning(Player player) {
		return Period.InRun(player);
	}
}
