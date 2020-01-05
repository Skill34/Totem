package com.skilldev.obsitotem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.entity.MPlayer;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.skilldev.obsitotem.utils.ChatUtils;
import com.skilldev.obsitotem.utils.LanguageUtils;

import lombok.Getter;
import lombok.Setter;

public class TotemManager {
	@Getter @Setter private static String factionBreakName;
	@Getter @Setter private static boolean isEventLaunch = false; 
	
	@Getter private static Material totemTypeBlock = Material.NETHERRACK;
	@Getter private static Material itemUseToBreak = Material.GOLD_AXE;
	@Getter private static String rewardCommand = null;
	
	@Getter private static HashMap<String, DigPlayer> digPlayers = new HashMap<String, DigPlayer>();
	
	@Getter private static HashMap<Block, Boolean> totemBlocks = new HashMap<Block, Boolean>();
	
	@SuppressWarnings("deprecation")
	public static void loadConfig(){
		ObsiTotemPlugin.getInstance().reloadConfig();
		
		if(!ObsiTotemPlugin.getInstance().getConfig().contains("database")) {
			ObsiTotemPlugin.getInstance().getConfig().set("database.host", "127.0.0.1");
			ObsiTotemPlugin.getInstance().getConfig().set("database.port", "3306");
			ObsiTotemPlugin.getInstance().getConfig().set("database.user", "root");
			ObsiTotemPlugin.getInstance().getConfig().set("database.password", "example");
			ObsiTotemPlugin.getInstance().getConfig().set("database.database", "db");
		}
		
		if(!ObsiTotemPlugin.getInstance().getConfig().contains("totemTypeBlock")){
			ObsiTotemPlugin.getInstance().getConfig().set("totemTypeBlock", 87);
		}
		
		if(!ObsiTotemPlugin.getInstance().getConfig().contains("itemUseToBreak")){
			ObsiTotemPlugin.getInstance().getConfig().set("itemUseToBreak", 286);
		}
		
		if(!ObsiTotemPlugin.getInstance().getConfig().contains("rewardCommand")){
			ObsiTotemPlugin.getInstance().getConfig().set("rewardCommand", "giverewards %players%");
		}
		
		ObsiTotemPlugin.getInstance().saveConfig();
		
		totemTypeBlock = Material.getMaterial(ObsiTotemPlugin.getInstance().getConfig().getInt("totemTypeBlock"));
		itemUseToBreak = Material.getMaterial(ObsiTotemPlugin.getInstance().getConfig().getInt("itemUseToBreak"));
		rewardCommand = ObsiTotemPlugin.getInstance().getConfig().getString("rewardCommand");
	}
	
	public static void saveTotem(Selection sel, Player p){
		//Do Check if sel != null
		int x, ymin, ymax, z;
		
		x = sel.getMaximumPoint().getBlockX();
		z = sel.getMaximumPoint().getBlockZ();
		
		if(sel.getMinimumPoint().getBlockY() <= sel.getMaximumPoint().getBlockY()){
			ymin = sel.getMinimumPoint().getBlockY();
			ymax = sel.getMaximumPoint().getBlockY();
		}else{
			ymin = sel.getMaximumPoint().getBlockY();
			ymax = sel.getMinimumPoint().getBlockY();
		}
		
		List<String> tempBlocks = new ArrayList<String>();
		
		for(int y = ymin; y <= ymax; y++){
			ChatUtils.broadcast(x + " " + y + " " + z);
			tempBlocks.add(sel.getWorld().getName() + ":" + x + ":" + y +":" + z);
		}
		
		if(!tempBlocks.isEmpty()){
			ObsiTotemPlugin.getInstance().getConfig().set("totemblocks", tempBlocks);
			ObsiTotemPlugin.getInstance().saveConfig();
			ChatUtils.sendMessagePlayer(p, "&aTotem défini avec succès.");
		}
	}
	
	public static void setCheater(Player p){
		ChatUtils.broadcast(LanguageUtils.getMessage("cheatplayer", 0, p.getName(), null));
		p.teleport(p.getWorld().getSpawnLocation());
	}
	
	public static void prepareEvent(CommandSender starter){
		if(!ObsiTotemPlugin.getInstance().getConfig().contains("totemblocks")){
			ChatUtils.sendMessagePlayer(starter, "&cAucun blocs définis dans la config.");
			return;
		}
		isEventLaunch = true;
		new StartRunnable(300).run();
	}
	
	public static void spawnTotem(boolean first){
		for(String s: ObsiTotemPlugin.getInstance().getConfig().getStringList("totemblocks")){
			String[] split = s.split(":");
			Block b = Bukkit.getWorld(split[0]).getBlockAt(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
			b.setType(totemTypeBlock);
			totemBlocks.put(b, false);
			b.getState().update();
		}
		if(first){
			ChatUtils.broadcast(LanguageUtils.getMessage("spawntotem", 0, null, null));
		}
	}
	
	public static void launchEvent(){
		spawnTotem(true);
	}
	
	public static void setEndTotem(){
		for(Block b : totemBlocks.keySet()){
			b.setType(Material.AIR);
		}
		totemBlocks.clear();
	}
	
	public static void resetTotem(String oldFac){
		spawnTotem(false);
		setFactionBreakName(null);
		ChatUtils.broadcast(LanguageUtils.getMessage("resettotem", 0, null, oldFac));
	}

	
	@SuppressWarnings("deprecation")
	public static void endEvent(MPlayer mp){
		isEventLaunch = false;
		Bukkit.getScheduler().scheduleSyncDelayedTask(ObsiTotemPlugin.getInstance(), 
			new BukkitRunnable(){
				@Override
				public void run() {
					ChatUtils.broadcast(LanguageUtils.getMessage("endTotem", 0, null, null));
					setEndTotem();
					for(Player p : Bukkit.getOnlinePlayers()){
						MPlayer mPlayer = MPlayer.get(p);
						if(getFactionBreakName().equalsIgnoreCase(mPlayer.getFactionName())){
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rewardCommand.replace("%players%", p.getName()));
						}
					}
					addWinToDb(mp.getFaction().getName(), mp.getFaction().getId());
					setFactionBreakName(null);
				}
		    }
		);
		
	}
	
	public static void addWinToDb(String fname, String fId){
		new Thread(){
			@Override
			public void run(){
				ResultSet result = ObsiTotemPlugin.getDb().query("SELECT win_number FROM totemwins WHERE fname='" + fname + "'");
					try {
						if(result.next()){
							int win_number = result.getInt("win_number") + 1;
							ObsiTotemPlugin.getDb().updateSQL("UPDATE totemwins SET win_number=" + win_number + " WHERE fname='" + fname + "'");
						}else{
							ObsiTotemPlugin.getDb().updateSQL("INSERT INTO totemwins (fname, fid, win_number) VALUES ('" + fname + "','" + fId + "'," + 1 + ")");
						}
					} catch (ClassNotFoundException | SQLException e) {e.printStackTrace();}
			}
		}.run();
	}
}
