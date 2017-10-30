package com.jaoafa.PeriodMatch.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ピリオド対決開始時・ピリオド成功時のカウンター
 * @author tomachi
 *
 */
public class PeriodSuccessCounter {
	JavaPlugin plugin;
	public PeriodSuccessCounter(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){

	}
}
