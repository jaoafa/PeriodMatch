package com.jaoafa.PeriodMatch.Event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.jaoafa.PeriodMatch.PeriodMatch;
import com.jaoafa.PeriodMatch.Command.Period;
import com.jaoafa.PeriodMatch.PeriodClass.PeriodSecEnd;

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
		Player player = event.getPlayer();
		if(!event.getMessage().equalsIgnoreCase(".")){
			return;
		}
		if(Period.InRun(player)){
			if(PeriodSecEnd.Success.containsKey(player.getUniqueId().toString())){
				PeriodSecEnd.Success.put(player.getUniqueId().toString(), 1);
			}
		}
		if(Period.InWait(player)){
			int sec = Period.getWaitingSec(player);
			BukkitTask task;
			try{
				task = new PeriodSecEnd(plugin, player, PeriodMatch.lunachatapi, sec).runTaskLater(plugin, sec * 20);
			}catch(java.lang.NoClassDefFoundError e){
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ピリオド対決を開始できませんでした。");
				Period.RemoveWait(player);
				return;
			}
			Period.RemoveWait(player);

			PeriodSecEnd.Run.put(player.getUniqueId().toString(), task);
			PeriodSecEnd.Success.put(player.getUniqueId().toString(), 1);
			PeriodSecEnd.Unsuccess.put(player.getUniqueId().toString(), 0);
			return;
		}
	}
}
