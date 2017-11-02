package com.jaoafa.PeriodMatch;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ucchyocean.lc.LunaChat;
import com.github.ucchyocean.lc.LunaChatAPI;
import com.jaoafa.PeriodMatch.Command.Period;
import com.jaoafa.PeriodMatch.Command.Period_Manage;
import com.jaoafa.PeriodMatch.Discord.Discord;
import com.jaoafa.PeriodMatch.Event.PeriodFailCounter;
import com.jaoafa.PeriodMatch.Event.PeriodSuccessCounter;

public class PeriodMatch extends JavaPlugin {

	public static Connection c = null;
	public static String sqluser;
	public static String sqlpassword;
	public static JavaPlugin instance = null;
	public static PeriodMatch periodmatch = null;
    private PeriodMatchManager manager;
	public static LunaChatAPI lunachatapi;
	public static LunaChat lunachat;
	public static Discord discord = null;

	/**
	 * プラグインが起動したときに呼び出し
	 * @author mine_book000
	 * @since 2017/10/31
	 */
	@Override
	public void onEnable() {
		// クレジット
		getLogger().info("(c) jao Minecraft Server PeriodMatch Project.");
		getLogger().info("Product by tomachi.");

		Load_Plugin("LunaChat");

		instance = this;
		periodmatch = this;
		manager = new PeriodMatchManager();

		lunachat = (LunaChat)getServer().getPluginManager().getPlugin("LunaChat");
		lunachatapi = lunachat.getLunaChatAPI();

		// コマンドを設定
		Import_Command_Executor();
		// コマンド入力補充を設定
		Import_Command_TabCompleter();
		// リスナーを設定
		Import_Listener();
		// コンフィグロード
		loadConfig();
	}

	/**
	 * コマンドの設定
	 * @author mine_book000
	 */
	private void Import_Command_Executor(){
		getCommand(".").setExecutor(new Period(this));
		getCommand("period").setExecutor(new Period_Manage(this));
	}

	/**
	 * コマンド入力補充の設定
	 * @author mine_book000
	 */
	private void Import_Command_TabCompleter(){

	}

	/**
	 * リスナー設定
	 * @author mine_book000
	 */
	private void Import_Listener(){
		getServer().getPluginManager().registerEvents(new PeriodSuccessCounter(this), this);
		getServer().getPluginManager().registerEvents(new PeriodFailCounter(this), this);
	}

	/**
     * PeriodMatchAPIを取得する
     * @return PeriodMatchAPI
     */
    public PeriodMatchAPI getPeriodMatchAPI() {
        return manager;
    }

	private void loadConfig(){
		FileConfiguration conf = getConfig();
		if(conf.contains("sqluser") && conf.contains("sqlpassword")){
			PeriodMatch.sqluser = conf.getString("sqluser");
			PeriodMatch.sqlpassword = conf.getString("sqlpassword");
			MySQL_Enable(conf.getString("sqluser"), conf.getString("sqlpassword"));
		}else{
			getLogger().info("MySQL Connect err. [conf NotFound]");
			getLogger().info("Disable PeriodMatch...");
			getServer().getPluginManager().disablePlugin(this);
		}
		if(conf.contains("discordtoken")){
			discord = new Discord(this, conf.getString("discordtoken"));
			discord.start();
		}else{
			getLogger().info("Discordへの接続に失敗しました。 [conf NotFound]");
			getLogger().info("Disable PeriodMatch...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	/**
	 * MySQLの初期設定
	 * @author mine_book000
	 */
	private void MySQL_Enable(String user, String password){
		MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", user, password);

		try {
			c = MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [ClassNotFoundException]");
			getLogger().info("Disable PeriodMatch...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [SQLException: " + e.getSQLState() + "]");
			getLogger().info("Disable PeriodMatch...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getLogger().info("MySQL Connect successful.");
	}

	/**
	 * プラグインが停止したときに呼び出し
	 * @author mine_book000
	 * @since 2017/10/31
	 */
	@Override
	public void onDisable() {

	}

	/**
	 * 連携プラグイン確認
	 * @author mine_book000
	 */
	private void Load_Plugin(String PluginName){
		if(getServer().getPluginManager().isPluginEnabled(PluginName)){
			getLogger().info("PeriodMatch Success(LOADED: " + PluginName + ")");
			getLogger().info("Using " + PluginName);
		}else{
			getLogger().warning("PeriodMatch ERR(NOTLOADED: " + PluginName + ")");
			getLogger().info("Disable PeriodMatch...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	/**
	 * コマンドの実行に対してメッセージを返信します。
	 * @param sender 実行者のCommandSender
	 * @param cmd コマンド情報
	 * @param text 返信するテキスト
	 * @author mine_book000
	 */
	public static void CommandReply(CommandSender sender, Command cmd, String text){
		sender.sendMessage("[PeriodMatch] " + ChatColor.GREEN + text);
	}

	public static JavaPlugin getJavaPlugin(){
		return instance;
	}
	public static PeriodMatch getInstance(){
		return periodmatch;
	}
}
