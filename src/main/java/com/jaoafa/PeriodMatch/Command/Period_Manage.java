package com.jaoafa.PeriodMatch.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.PeriodMatch.PeriodMatch;

/**
 * コマンド「/period」用のプログラム
 * @author tomachi
 *
 */
public class Period_Manage implements CommandExecutor {
	JavaPlugin plugin;
	public Period_Manage(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		PeriodMatch.CommandReply(sender, cmd, "未実装");
		return true;
	}
}
