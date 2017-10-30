package com.jaoafa.PeriodMatch.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ピリオド失敗時のカウンター
 * @author tomachi
 *
 */
public class PeriodFailCounter {
	JavaPlugin plugin;
	public PeriodFailCounter(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){

	}
}
