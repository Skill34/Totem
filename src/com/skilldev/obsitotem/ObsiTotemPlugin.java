package com.skilldev.obsitotem;

import java.sql.SQLException;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.skilldev.obsitotem.listener.TotemListener;
import com.skilldev.obsitotem.utils.Database;
import com.skilldev.obsitotem.utils.LanguageUtils;
import com.skilldev.obsitotem.utils.MySQL;

public class ObsiTotemPlugin extends JavaPlugin{
	private static ObsiTotemPlugin instance;
    private static Database db;
	
    public static ObsiTotemPlugin getInstance() {
    	return instance;
    }
    public static Database getDb() {
    	return db;
    }
	
	public WorldEditPlugin getWorldEdit() {
		Plugin we = getServer().getPluginManager().getPlugin("WorldEdit");
		if (we == null || !(we instanceof WorldEditPlugin)) {
			return null;
		}
		return (WorldEditPlugin) we;
	}
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new TotemListener(), this);
		instance = this;
		TotemManager.loadConfig();
		new LanguageUtils();
		db = new MySQL(getConfig().getString("database.host"), 
				getConfig().getString("database.port"),
				getConfig().getString("database.user"), 
				getConfig().getString("database.password"),
				getConfig().getString("database.database"));
		
		getCommand("totem").setExecutor(new TotemCommand());
		createTable();
	}
	
	public static void createTable() {
		new Thread(){
			@Override
			public void run(){
				try {
					getDb().updateSQL("CREATE TABLE IF NOT EXISTS totemwins (id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,fname VARCHAR(50) NOT NULL, fid VARCHAR(50) NOT NULL, win_number INT NOT NULL)");
				} catch (ClassNotFoundException | SQLException e) {e.printStackTrace();}
			}
		}.run();
	}
}
