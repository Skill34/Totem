package com.skilldev.obsitotem.listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

import com.skilldev.obsitotem.DigPlayer;
import com.skilldev.obsitotem.TotemManager;
import com.skilldev.obsitotem.utils.ChatUtils;
import com.skilldev.obsitotem.utils.LanguageUtils;

public class TotemListener implements Listener{
	@EventHandler
	public void onBlockBamage(BlockDamageEvent e){
		if(!TotemManager.isEventLaunch()){
	        return;
	    }else if(TotemManager.getTotemBlocks().isEmpty() || !TotemManager.getTotemBlocks().containsKey(e.getBlock())){
     	    return;
        }else if(e.getInstaBreak() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
			e.setCancelled(true);
			TotemManager.setCheater(e.getPlayer());
			return;
		}
		if(!TotemManager.getDigPlayers().containsKey(e.getPlayer().getName().toLowerCase())){
			TotemManager.getDigPlayers().put(e.getPlayer().getName().toLowerCase(), new DigPlayer(e.getPlayer().getName().toLowerCase()));
 	    }
		TotemManager.getDigPlayers().get(e.getPlayer().getName().toLowerCase()).addStartPacket(e.getBlock());
		
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(!TotemManager.isEventLaunch()){
	    	   return;
	    }else if(e.getPlayer() == null || !e.getPlayer().isOnline()){
	    	   return;
	    }else if(!TotemManager.getTotemBlocks().containsKey(e.getBlock())){
	    	return;
	    }
	    
	    else if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
	    	ChatUtils.sendMessagePlayer(e.getPlayer(), "&cPas de gamemode créatif tricheur :D");
  		    e.setCancelled(true);
     	    return;
		}else if(!e.getPlayer().getItemInHand().getType().equals(TotemManager.getItemUseToBreak())){
  		    ChatUtils.sendMessagePlayer(e.getPlayer(), LanguageUtils.getMessage("musthaveiteminhand", 0, null, null));
  		    e.setCancelled(true);
     	    return;
        }
		TotemManager.getDigPlayers().get(e.getPlayer().getName().toLowerCase()).receiveStop(e);
		
	}
}
