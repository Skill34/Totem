package com.skilldev.obsitotem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skilldev.obsitotem.utils.ChatUtils;

// Referenced classes of package com.skilldev.obsitotem:
//            TotemManager, ObsiTotemPlugin

public class TotemCommand implements CommandExecutor{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
        if(args.length == 0){
            ChatUtils.sendMessagePlayer(sender, "&cUtilisation /obsitotem start|settotem.");
            return true;
        }else if(args[0].equalsIgnoreCase("start")){
            if(!sender.hasPermission("totem.start")){
                ChatUtils.sendMessagePlayer(sender, "&cVous n'avez pas la permission d'utiliser cette commande.");
                return true;
            }else if(TotemManager.isEventLaunch()){
            	ChatUtils.sendMessagePlayer(sender, "&cLe totem est déjà lancé !");
            	return true;
            }
            TotemManager.prepareEvent(sender);
        } else if(args[0].equalsIgnoreCase("settotem")){
             if(!(sender instanceof Player)){
                 ChatUtils.sendMessagePlayer(sender, "&cIl faut être un joueur pour utiliser cette commande.");
                 return true;
             }else if(!sender.hasPermission("totem.setposition")){
                 ChatUtils.sendMessagePlayer(sender, "&cVous n'avez pas la permission d'utiliser cette commande.");
                 return true;
             }else if(ObsiTotemPlugin.getInstance().getWorldEdit().getSelection((Player)sender) == null){
                 ChatUtils.sendMessagePlayer(sender, "&cIl faut faire une sélection avant.");
                 return true;
             }
             TotemManager.saveTotem(ObsiTotemPlugin.getInstance().getWorldEdit().getSelection((Player)sender), (Player)sender);
        }  
        return true;
    }
}