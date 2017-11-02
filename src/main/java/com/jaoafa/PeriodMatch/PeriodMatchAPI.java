package com.jaoafa.PeriodMatch;

import org.bukkit.entity.Player;

public interface PeriodMatchAPI {
	/**
	 * 現在ピリオド対決中かどうかチェックします。
	 * @param player
	 * @return 対決中かどうか
	 */
	public boolean getPeriodRunning(Player player);
}
