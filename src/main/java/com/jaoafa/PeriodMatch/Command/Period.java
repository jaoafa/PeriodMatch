package com.jaoafa.PeriodMatch.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.PeriodMatch.PeriodMatch;

/**
 * コマンド「/.」用のプログラム
 * @author tomachi
 *
 */
public class Period implements CommandExecutor {
	JavaPlugin plugin;
	public Period(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	static Map<String, Integer> waiting = new HashMap<String, Integer>();
	static Map<String, Integer> running = new HashMap<String, Integer>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player)){
			// プレイヤー以外からのコマンド実行
			PeriodMatch.CommandReply(sender, cmd, "このコマンドはゲーム内でのみ使用できます。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 0){
			if(InRun(player)){
				PeriodMatch.CommandReply(sender, cmd, "現在あなたはピリオド対決を" + getRunningSec(player) + "秒で行っています。");
			}else if(InWait(player)){
				PeriodMatch.CommandReply(sender, cmd, "現在あなたはピリオド対決を" + getRunningSec(player) + "秒で待機しています。");
				PeriodMatch.CommandReply(sender, cmd, "次に「.」と打った瞬間から開始します。");
			}
			PeriodMatch.CommandReply(sender, cmd, "「/. [TimeSecound]」でピリオド対決を行うことができます。");
			PeriodMatch.CommandReply(sender, cmd, "「/. stop」で行っているピリオド対決を強制終了できます。");
			return true;
		}else if(args.length == 1){
			if(isNumber(args[0])){
				int sec = Integer.parseInt(args[0]);
				Start(sender, cmd, player, sec);
				return true;
			}else if(args[0].equalsIgnoreCase("stop")){
				ForceEnd(sender, cmd, player);
				return true;
			}
		}
		PeriodMatch.CommandReply(sender, cmd, "----- Period -----");
		PeriodMatch.CommandReply(sender, cmd, "/. [TimeSecond]: TimeSecond秒のピリオド対決を行います。");
		PeriodMatch.CommandReply(sender, cmd, "ピリオド対決では次の秒数のみランキングが公開されます。それ以外の秒数ではランキング付けがされません。");
		PeriodMatch.CommandReply(sender, cmd, "10秒(/. 10)・60秒(/. 60)・300秒(/. 300)");
		PeriodMatch.CommandReply(sender, cmd, "/. stop: 既に開始しているピリオド対決を強制終了します。");
		return true;
	}
	private boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	private boolean Start(CommandSender sender, Command cmd, Player player, Integer sec){
		if(InRun(player)){
			PeriodMatch.CommandReply(sender, cmd, "既にピリオド対決中です。");
			return false;
		}
		if(sec < 0){
			PeriodMatch.CommandReply(sender, cmd, "ピリオド対決の秒数は0秒以上を指定してください。");
			return false;
		}

		PeriodMatch.CommandReply(sender, cmd, "ピリオド対決を開始します。次に「.」を打った瞬間から" + sec + "秒間計測します。");
		PeriodMatch.CommandReply(sender, cmd, "正確にピリオド判定を行うため、かなローマ字変換をオフにして(/jp off)ご利用ください。");
		waiting.put(player.getUniqueId().toString(), sec); // Waitに突っ込む
		return true;
	}

	public static boolean InWait(Player player){
		return waiting.containsKey(player.getUniqueId().toString());
	}

	public static boolean RemoveWait(Player player){
		if(InWait(player)){
			waiting.remove(player.getUniqueId().toString());
			return true;
		}else{
			return false;
		}
	}

	public static boolean InRun(Player player){
		return running.containsKey(player.getUniqueId().toString());
	}
	public static void AddRun(Player player, int sec){
		running.put(player.getUniqueId().toString(), sec);
	}
	public static boolean RemoveRun(Player player){
		if(InRun(player)){
			running.remove(player.getUniqueId().toString());
			return true;
		}else{
			return false;
		}
	}

	public static Integer getRunningSec(Player player){
		if(InRun(player)){
			return running.get(player.getUniqueId().toString());
		}else{
			return -1;
		}
	}

	public static Integer getWaitingSec(Player player){
		if(InWait(player)){
			return waiting.get(player.getUniqueId().toString());
		}else{
			return -1;
		}
	}

	private void ForceEnd(CommandSender sender, Command cmd, Player player){
		if(InRun(player)){
			running.remove(player.getUniqueId().toString());
		}
		if(InWait(player)){
			waiting.remove(player.getUniqueId().toString());
		}
		PeriodMatch.CommandReply(sender, cmd, "ピリオド対決を強制終了しました。");
	}
}
