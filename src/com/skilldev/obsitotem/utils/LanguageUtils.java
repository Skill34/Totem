package com.skilldev.obsitotem.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.skilldev.obsitotem.ObsiTotemPlugin;
import com.skilldev.obsitotem.TotemManager;

public class LanguageUtils {
	private static File lang;
	private static FileConfiguration langConf;
	
	public LanguageUtils(){
		lang = new File(ObsiTotemPlugin.getInstance().getDataFolder(), "language.yml");
		if(!lang.exists()){
			ObsiTotemPlugin.getInstance().saveResource("language.yml", true);
		}
		try {
			langConf = YamlConfiguration.loadConfiguration(lang);
			langConf.save(lang);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static String getMessage(String type, int time, String pName, String oldFac){
		String message = langConf.getString(type);
		if(time != 0){
			if(time > 60){
				message = message.replace("%TIME%", (int) time/60 + " minutes");
			}else if(time == 1){
				message = message.replace("%TIME%", "1 seconde");
			}else{
				message = message.replace("%TIME%", time + " secondes");
			}
		}
		
		if(pName != null){
			message = message.replace("%PLAYERNAME%", pName);
		}
		
		if(oldFac != null){
			message = message.replace("%FACTIONNAME%", oldFac);
		}else if(message.contains("%FACTIONNAME%")){
			message = message.replace("%FACTIONNAME%", TotemManager.getFactionBreakName());
		}
		return ChatUtils.colorReplace(message);
	}
	
}
