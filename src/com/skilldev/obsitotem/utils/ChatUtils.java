package com.skilldev.obsitotem.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class ChatUtils {
	public static String colorReplace(String input){
		String output = null;
		output = input.replace("%black%", ChatColor.BLACK.toString());
		output = output.replace("%dblue%", ChatColor.DARK_BLUE.toString());
		output = output.replace("%dgreen%", ChatColor.DARK_GREEN.toString());
		output = output.replace("%darkaqua%", ChatColor.DARK_AQUA.toString());
		output = output.replace("%dred%", ChatColor.DARK_RED.toString());
		output = output.replace("%darkred%", ChatColor.DARK_RED.toString());
		output = output.replace("%dpurple%", ChatColor.DARK_PURPLE.toString());
		output = output.replace("%gold%", ChatColor.GOLD.toString());
		output = output.replace("%gray%", ChatColor.GRAY.toString());
		output = output.replace("%dgray%", ChatColor.DARK_GRAY.toString());
		output = output.replace("%blue%", ChatColor.BLUE.toString());
		output = output.replace("%green%", ChatColor.GREEN.toString());
		output = output.replace("%aqua%", ChatColor.AQUA.toString());
		output = output.replace("%red%", ChatColor.RED.toString());
		output = output.replace("%lpurple%", ChatColor.LIGHT_PURPLE.toString());
		output = output.replace("%yellow%", ChatColor.YELLOW.toString());
		output = output.replace("%white%", ChatColor.WHITE.toString());
		output = output.replace("%bold%", ChatColor.BOLD.toString());
		output = output.replace("%italic%", ChatColor.ITALIC.toString());
		output = output.replace("%magic%", ChatColor.MAGIC.toString());
		output = output.replace("%default%", ChatColor.RESET.toString());

		output = ChatColor.translateAlternateColorCodes('&', output);
		return output;
	}
	public static String colorDelete(String input){
		String output = null;
		output = input.replace("%black%", "");
		output = output.replace("%dblue%", "");
		output = output.replace("%dgreen%", "");
		output = output.replace("%darkaqua%", "");
		output = output.replace("%dred%", "");
		output = output.replace("%dpurple%", "");
		output = output.replace("%gold%", "");
		output = output.replace("%gray%", "");
		output = output.replace("%dgray%", "");
		output = output.replace("%blue%", "");
		output = output.replace("%green%", "");
		output = output.replace("%aqua%", "");
		output = output.replace("%red%", "");
		output = output.replace("%lpurple%", "");
		output = output.replace("%yellow%", "");
		output = output.replace("%white%", "");
		output = output.replace("%bold%", "");
		output = output.replace("%italic%", "");
		output = output.replace("%magic%", "");
		output = output.replace("%default%", "");

		output = ChatColor.translateAlternateColorCodes('&', output);

		output = output.replace("§0", "");
		output = output.replace("§1", "");
		output = output.replace("§2", "");
		output = output.replace("§3", "");
		output = output.replace("§4", "");
		output = output.replace("§5", "");
		output = output.replace("§6", "");
		output = output.replace("§7", "");
		output = output.replace("§8", "");
		output = output.replace("§9", "");
		output = output.replace("§a", "");
		output = output.replace("§b", "");
		output = output.replace("§c", "");
		output = output.replace("§d", "");
		output = output.replace("§e", "");
		output = output.replace("§f", "");
		output = output.replace("§o", "");
		output = output.replace("§k", "");
		output = output.replace("§l", "");
		return output;
	}
	@SuppressWarnings("deprecation")
	public static void playSound(Sound sound){
		for(Player player : Bukkit.getOnlinePlayers()){
			player.playSound(player.getLocation(), sound, 3.0f, 1.0f);
		}
	}
	public static void sendTitlePlayer(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut){
		if(player == null) return;
		if(!title.isEmpty())
			sendMessagePlayer(player, title);
		if(!subtitle.isEmpty())
			sendMessagePlayer(player, subtitle);
//		CraftPlayer craftplayer = (CraftPlayer)player;
//		PlayerConnection connection = craftplayer.getHandle().playerConnection;
//		
//		IChatBaseComponent titleJSON = ChatSerializer.a("{\"text\": \"" + colorReplace(title) + "\"}");
//		IChatBaseComponent subtitleJSON = ChatSerializer.a("{\"text\": \"" + colorReplace(subtitle) + "\"}");	
//		Packet length = new PacketPlayOutTitle(EnumTitleAction.TIMES, titleJSON, fadeIn, stay, fadeOut);
//		Packet titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, fadeIn, stay, fadeOut);
//		Packet subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleJSON, fadeIn, stay, fadeOut);
		
//		connection.sendPacket(titlePacket);
//		connection.sendPacket(length);
//		connection.sendPacket(subtitlePacket);
	}
	public static void sendActionBarPlayer(Player p, String msg) {
		if(p == null) return;
//		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + colorReplace(msg) + "\"}");
//		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
//		((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoc);
	}
	
	@SuppressWarnings("deprecation")
	public static void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut){
		for(Player player : Bukkit.getOnlinePlayers()){
			sendTitlePlayer(player, title, subtitle, fadeIn, stay, fadeOut);
		}
	}
	/* Toute les possibilites d'envois de message */

	public static void broadcast(String message){
		Bukkit.broadcastMessage(colorReplace(message));
	}
	/* ARRAY */
	public static void sendMessagePlayer(Player player, String message[]){
		for(int i=0;i<message.length;i++)
			player.sendMessage(colorReplace(message[i]));
	}
	public static void sendMessagePlayer(CommandSender player, String message[]){
		for(int i=0;i<message.length;i++)
			player.sendMessage(colorReplace(message[i]));
	}
	public static void sendMessagePlayer(HumanEntity player, String message[]){
		for(int i=0;i<message.length;i++)
			((CommandSender) player).sendMessage(message[i]);
	}

	/* SIMPLE */
	public static void sendMessagePlayer(Player player, String message){
		if(player == null) return;
		sendMessagePlayer(player, message.split(";"));
	}
	public static void sendMessagePlayer(CommandSender player, String message){
		sendMessagePlayer(player, message.split(";"));
	}
	public static void sendMessagePlayer(HumanEntity player, String message) {
		sendMessagePlayer(((CommandSender) player), message.split(";"));
	}
}
