package com.skilldev.obsitotem;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.skilldev.obsitotem.utils.ChatUtils;
import com.skilldev.obsitotem.utils.LanguageUtils;

import lombok.Getter;
import lombok.Setter;

public class DigPlayer {
	public String name;
	@Getter @Setter public Long lastStartInterract = 0L;
	@Getter public DigPlayer thisPlayer;
	@Getter @Setter public Block lastTotemBlock = null;
	
	public DigPlayer(String name){
		this.name = name;
		thisPlayer = this;
	}
	
	public void addStartPacket(Block block){
		this.lastTotemBlock = block;
		this.lastStartInterract = System.currentTimeMillis();
	}
	
	public void receiveStop(BlockBreakEvent e){
		if(e.isCancelled()){
			return;
		}else if(e.getBlock().equals(lastTotemBlock) && ((System.currentTimeMillis() - lastStartInterract) < 500L)){
			e.setCancelled(true);
			TotemManager.setCheater(e.getPlayer());
			return;
		}
		
        MPlayer mPlayer = MPlayer.get(e.getPlayer());
		
		if(mPlayer.getFaction().equals(FactionColl.get().getNone())){
			ChatUtils.sendMessagePlayer(e.getPlayer(), LanguageUtils.getMessage("musthavefaction", 0, null, null));
			e.setCancelled(true);
			return;
		}
		
		if(TotemManager.getFactionBreakName() != null && !TotemManager.getFactionBreakName().equalsIgnoreCase(mPlayer.getFactionName())){
			e.setCancelled(true);
			TotemManager.resetTotem(TotemManager.getFactionBreakName());
			return;
		}
		
		TotemManager.setFactionBreakName(mPlayer.getFactionName());
		
		TotemManager.getTotemBlocks().put(e.getBlock(), true);
		
		if(!TotemManager.getTotemBlocks().containsValue(false)){
			TotemManager.endEvent(mPlayer);
			new FireRunnable(1, e.getBlock().getLocation()).run();
			
		}
		
		ChatUtils.broadcast(LanguageUtils.getMessage("playerhasbreakablock", 0, e.getPlayer().getName(), mPlayer.getFactionName()));
	}
}
