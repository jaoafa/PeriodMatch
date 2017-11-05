package com.jaoafa.PeriodMatch.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.PeriodMatch.Command.Period;
import com.jaoafa.PeriodMatch.PeriodClass.PeriodSecEnd;

import net.md_5.bungee.api.ChatColor;

/**
 * ピリオド失敗時のカウンター
 * @author tomachi
 *
 */
public class PeriodFailCounter implements Listener {
	JavaPlugin plugin;
	public PeriodFailCounter(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(!player.isSleeping() && event.getMessage().equalsIgnoreCase(".")){
			//寝てなくてピリオドだったら無視
			return;
		}
		if(Period.InRun(player)){
			if(player.isSleeping()){
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "寝ながらのピリオドは失敗と判定されます。");
			}
			if(PeriodSecEnd.Unsuccess.containsKey(player.getUniqueId().toString())){
				PeriodSecEnd.Unsuccess.put(player.getUniqueId().toString(),
						PeriodSecEnd.Unsuccess.get(player.getUniqueId().toString()) + 1
					);
			}else{
				PeriodSecEnd.Unsuccess.put(player.getUniqueId().toString(), 1);
			}
		}
	}
}
