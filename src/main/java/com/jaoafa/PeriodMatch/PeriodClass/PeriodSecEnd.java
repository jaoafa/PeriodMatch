package com.jaoafa.PeriodMatch.PeriodClass;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.ucchyocean.lc.LunaChatAPI;
import com.github.ucchyocean.lc.channel.ChannelPlayer;
import com.jaoafa.PeriodMatch.MySQL;
import com.jaoafa.PeriodMatch.PeriodMatch;
import com.jaoafa.PeriodMatch.Command.Period;
import com.jaoafa.PeriodMatch.Discord.Discord;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PeriodSecEnd extends BukkitRunnable{
	Player player;
	LunaChatAPI lunachatapi;
	int section;
	public PeriodSecEnd(JavaPlugin plugin, Player player, LunaChatAPI lunachatapi, Integer section) {
		this.player = player;
		this.lunachatapi = lunachatapi;
		this.section = section;
	}

	private Integer Calc(int success, int unsuccess){
		if(success == 0) success = 1;
		if(unsuccess == 0) unsuccess = 1;
		return success * (success / (success + unsuccess)) - unsuccess;
	}
	//連投実行中か
	public static Map<String, BukkitTask> Run = new HashMap<String, BukkitTask>();

	//成功回数
	public static Map<String, Integer> Success = new HashMap<String, Integer>();

	//失敗回数
	public static Map<String, Integer> Unsuccess = new HashMap<String, Integer>();

	//DOTCOUNTERストップ
	public static Map<String, Boolean> PeriodCount_Stop = new HashMap<String, Boolean>();

	@Override
	public void run() {
		int success = 0;
		if(!this.player.isOnline()){
			return;
		}
		if(Success.containsKey(this.player.getUniqueId().toString())){
			success = Success.get(this.player.getUniqueId().toString());
		}

		int unsuccess = 0;
		if(Unsuccess.containsKey(this.player.getUniqueId().toString())){
			unsuccess = Unsuccess.get(this.player.getUniqueId().toString());
		}

		if(Run.containsKey(this.player.getUniqueId().toString())){
			BukkitTask task = Run.get(player.getUniqueId().toString());
			if(task != null){
				task.cancel();
			}
			Run.remove(player.getUniqueId().toString());
		}

		if(Success.containsKey(player.getUniqueId().toString())) Success.remove(player.getUniqueId().toString());

		if(Unsuccess.containsKey(player.getUniqueId().toString())) Unsuccess.remove(player.getUniqueId().toString());

		if(PeriodCount_Stop.containsKey(player.getUniqueId().toString())) PeriodCount_Stop.remove(player.getUniqueId().toString());

		this.lunachatapi.getChannel("_DOT_").removeMember(ChannelPlayer.getChannelPlayer(this.player.getName()));
		this.lunachatapi.removeDefaultChannel(this.player.getName());

		Statement statement;
		try {
			statement = PeriodMatch.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", PeriodMatch.sqluser, PeriodMatch.sqlpassword);
			try {
				PeriodMatch.c = MySQL.openConnection();
				statement = PeriodMatch.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ピリオド対決の終了処理に失敗しました。\n"
						+ "ErrorMessage: " + e1.getMessage());
				BugReport(e);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ピリオド対決の終了処理に失敗しました。\n"
					+ "ErrorMessage: " + e.getMessage());
			BugReport(e);
			return;
		}

		statement = MySQL.check(statement);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());

		if(this.section == 10){
			try {
				statement.execute("INSERT INTO dot_10s (player, uuid, success, unsuccess, date) "
						+ "VALUES "
						+ "(\"" + player.getName() + "\", \"" + player.getUniqueId().toString() + "\", " + success + "," + unsuccess + ", \"" + date + "\")");
				ResultSet res = statement.executeQuery("SELECT * FROM dot_10s");

				sendData(res, format, date, success, unsuccess);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ピリオド対決の結果データの保存に失敗しました。");
				BugReport(e);
				return;
			}
		}else if(this.section == 60){
			try {
				statement.execute("INSERT INTO dot (player, uuid, success, unsuccess, date) "
						+ "VALUES "
						+ "(\"" + player.getName() + "\", \"" + player.getUniqueId().toString() + "\", " + success + "," + unsuccess + ", \"" + date + "\")");
				ResultSet res = statement.executeQuery("SELECT * FROM dot");

				sendData(res, format, date, success, unsuccess);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ピリオド対決の結果データの保存に失敗しました。");
				BugReport(e);
				return;
			}
		}else if(this.section == 300){
			try {
				statement.execute("INSERT INTO dot_300s (player, uuid, success, unsuccess, date) "
						+ "VALUES "
						+ "(\"" + player.getName() + "\", \"" + player.getUniqueId().toString() + "\", " + success + "," + unsuccess + ", \"" + date + "\")");
				ResultSet res = statement.executeQuery("SELECT * FROM dot_300s");

				sendData(res, format, date, success, unsuccess);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				player.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "ピリオド対決の結果データの保存に失敗しました。");
				BugReport(e);
				return;
			}
		}else{
			Bukkit.broadcastMessage("[.] " + ChatColor.GREEN + this.player.getName() + "のピリオド対決(" + this.section + "秒例外部門)の結果: 成功回数" + success + " 失敗回数" + unsuccess + "(部門外のためrankingなし)");
			Discord.send(this.player.getName() + "のピリオド対決(" + this.section + "秒例外部門)の結果: 成功回数" + success + " 失敗回数" + unsuccess + "(部門外のためrankingなし)");
		}
		Period.RemoveRun(player);
		Period.RemoveWait(player);
	}
	private boolean sendData(ResultSet res, SimpleDateFormat format, String date, int success, int unsuccess){
		Map<Integer, String> sorts = new HashMap<Integer, String>();
		int allcount = 0;
		try {
			while(res.next()){
				String _date = res.getString("date");
				int _success = res.getInt("success");
				int _unsuccess = res.getInt("unsuccess");

				int _calc = Calc(_success, _unsuccess);

				sorts.put(_calc, _date);
				allcount++;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			BugReport(e);
			return false;
		}

		ArrayList<String> List = new ArrayList<String>();
		sorts.entrySet().stream()
        	.sorted(java.util.Map.Entry.comparingByKey())
        	.forEach(s -> List.add(s.getValue()));

		Integer rank = List.indexOf(date);
		String ranking = rank + "/" + allcount;

		Bukkit.broadcastMessage("[PeriodMatch] " + ChatColor.GREEN + this.player.getName() + "のピリオド対決(" + this.section + "秒部門)の結果: 成功回数" + success + " 失敗回数" + unsuccess + "(累計順位: " + ranking + "位)");
		Discord.send(this.player.getName() + "のピリオド対決(" + this.section + "秒部門)の結果: 成功回数" + success + " 失敗回数" + unsuccess + "(累計順位: " + ranking + "位)");
		return true;
	}

	private static void BugReport(Throwable exception){
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        exception.printStackTrace();
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
				p.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "PeriodMatchのシステム障害が発生しました。");
				p.sendMessage("[PeriodMatch] " + ChatColor.GREEN + "エラー: " + exception.getMessage());
			}
		}
		boolean res = Discord.send("293856671799967744", "PeriodMatchでエラーが発生しました。" + "\n"
					+ "```" + sw.toString() + "```\n"
					+ "Cause: `" + exception.getCause() + "`");
		if(res){
			PeriodMatch.getInstance().getLogger().info("Bugreport: Discord送信に成功");
		}else{
			PeriodMatch.getInstance().getLogger().info("Bugreport: Discord送信に失敗");
		}
	}
}