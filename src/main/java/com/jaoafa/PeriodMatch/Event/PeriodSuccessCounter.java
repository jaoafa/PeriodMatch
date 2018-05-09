package com.jaoafa.PeriodMatch.Event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.ucchyocean.lc.channel.ChannelPlayer;
import com.jaoafa.PeriodMatch.PeriodMatch;
import com.jaoafa.PeriodMatch.Command.Period;
import com.jaoafa.PeriodMatch.PeriodClass.PeriodCountDown;
import com.jaoafa.PeriodMatch.PeriodClass.PeriodSecEnd;
import com.jaoafa.jaoSuperAchievement.AchievementAPI.AchievementAPI;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.AchievementType;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.Achievementjao;

/**
 * ピリオド対決開始時・ピリオド成功時のカウンター
 * @author tomachi
 *
 */
public class PeriodSuccessCounter implements Listener {
	JavaPlugin plugin;
	public PeriodSuccessCounter(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(!event.getMessage().equalsIgnoreCase(".")){
			return;
		}
		if(Period.InRun(player)){
			if(player.isSleeping()){
				return;
			}
			if(PeriodSecEnd.Success.containsKey(player.getUniqueId().toString())){
				PeriodSecEnd.Success.put(player.getUniqueId().toString(),
						PeriodSecEnd.Success.get(player.getUniqueId().toString()) + 1
					);
			}else{
				PeriodSecEnd.Success.put(player.getUniqueId().toString(), 1);
			}
		}
		if(Period.InWait(player) && !Period.InRun(player)){
			int sec = Period.getWaitingSec(player);
			if(player.isSleeping()){
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ピリオド対決を開始できませんでした。");
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ルールを確認し、最初からやり直してください。");
				Period.RemoveWait(player);
				return;
			}
			BukkitTask task;
			try{
				task = new PeriodSecEnd(plugin, player, PeriodMatch.lunachatapi, sec).runTaskLater(plugin, sec * 20);
			}catch(java.lang.NoClassDefFoundError e){
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ピリオド対決を開始できませんでした。");
				Period.RemoveWait(player);
				return;
			}
			try{
				if(sec == 10){
					// 3s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 3).runTaskLater(plugin, (sec - 3) * 20);
					// 2s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 2).runTaskLater(plugin, (sec - 2) * 20);
					// 1s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 1).runTaskLater(plugin, (sec - 1) * 20);
				}else if(sec == 60){
					// 30s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 30).runTaskLater(plugin, (sec - 30) * 20);
					// 10s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 10).runTaskLater(plugin, (sec - 10) * 20);
					// 3s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 3).runTaskLater(plugin, (sec - 3) * 20);
					// 2s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 2).runTaskLater(plugin, (sec - 2) * 20);
					// 1s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 1).runTaskLater(plugin, (sec - 1) * 20);
				}else if(sec == 300){
					// 200s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 200).runTaskLater(plugin, (sec - 200) * 20);
					// 100s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 100).runTaskLater(plugin, (sec - 100) * 20);
					// 60s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 60).runTaskLater(plugin, (sec - 60) * 20);
					// 30s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 30).runTaskLater(plugin, (sec - 30) * 20);
					// 10s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 10).runTaskLater(plugin, (sec - 10) * 20);
					// 3s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 3).runTaskLater(plugin, (sec - 3) * 20);
					// 2s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 2).runTaskLater(plugin, (sec - 2) * 20);
					// 1s
					new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 1).runTaskLater(plugin, (sec - 1) * 20);
				}
				//new PeriodCountDown(plugin, player, PeriodMatch.lunachatapi, sec, 200).runTaskLater(plugin, sec * 20);
			}catch(java.lang.NoClassDefFoundError e){
			}
			PeriodMatch.lunachatapi.getChannel("_DOT_").addMember(ChannelPlayer.getChannelPlayer(player.getName()));
			PeriodMatch.lunachatapi.setDefaultChannel(player.getName(), "_DOT_");
			//DOT.kana.put(player.getName(), PeriodMatch.lunachatapi.isPlayerJapanize(player.getName()));
			PeriodMatch.lunachatapi.setPlayersJapanize(player.getName(), false);
			Period.RemoveWait(player);
			Period.AddRun(player, sec);

			if(!Achievementjao.getAchievement(player, new AchievementType(12))){
				player.sendMessage(AchievementAPI.getPrefix() + "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}

			PeriodSecEnd.Run.put(player.getUniqueId().toString(), task);
			PeriodSecEnd.Success.put(player.getUniqueId().toString(), 1);
			PeriodSecEnd.Unsuccess.put(player.getUniqueId().toString(), 0);
			return;
		}
	}
}
