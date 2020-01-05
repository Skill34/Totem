package com.skilldev.obsitotem;

import org.bukkit.scheduler.BukkitRunnable;

import com.skilldev.obsitotem.utils.ChatUtils;
import com.skilldev.obsitotem.utils.LanguageUtils;

public class StartRunnable extends BukkitRunnable{
	int timeStart;
	public StartRunnable(int timeStart){
		this.timeStart = timeStart;
	}

	@Override
	public void run() {
		if(timeStart == 300){
			ChatUtils.broadcast(LanguageUtils.getMessage("willspawntotem", 300, null, null));
			new StartRunnable(60).runTaskLater(ObsiTotemPlugin.getInstance(), 20L  * 240);
		}else if(timeStart == 60){
			ChatUtils.broadcast(LanguageUtils.getMessage("willspawntotem", 60, null, null));
			new StartRunnable(30).runTaskLater(ObsiTotemPlugin.getInstance(), 20L  * 30);
		}else if(timeStart == 30){
			ChatUtils.broadcast(LanguageUtils.getMessage("willspawntotem", 30, null, null));
			new StartRunnable(10).runTaskLater(ObsiTotemPlugin.getInstance(), 20L  * 20);
		}else if(timeStart == 10){
			ChatUtils.broadcast(LanguageUtils.getMessage("willspawntotem", 10, null, null));
			new StartRunnable(5).runTaskLater(ObsiTotemPlugin.getInstance(), 20L  * 5);
		}else if(timeStart != 0){
			ChatUtils.broadcast(LanguageUtils.getMessage("willspawntotem", timeStart, null, null));
			new StartRunnable(timeStart - 1).runTaskLater(ObsiTotemPlugin.getInstance(), 20L  * 1);
		}else{
			TotemManager.launchEvent();
		}
	}

}
