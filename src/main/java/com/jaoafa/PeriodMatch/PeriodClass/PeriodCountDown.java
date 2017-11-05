package com.jaoafa.PeriodMatch.PeriodClass;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.ucchyocean.lc.LunaChatAPI;
import com.jaoafa.PeriodMatch.Command.Period;

public class PeriodCountDown extends BukkitRunnable{
	Player player;
	LunaChatAPI lunachatapi;
	int section;
	int remaining;
	public PeriodCountDown(JavaPlugin plugin, Player player, LunaChatAPI lunachatapi, Integer section, Integer remaining) {
		this.player = player;
		this.lunachatapi = lunachatapi;
		this.section = section;
		this.remaining = remaining;
	}

	@Override
	public void run() {
		// 残り～秒の通知
		if(!this.player.isOnline()){
			return;
		}
		if(!Period.InRun(player)){
			return;
		}
		player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "あなたが参加しているピリオド対決" + section + "秒部門があと" + remaining + "秒で終了します…。");
	}
}
