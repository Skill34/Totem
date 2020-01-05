package com.skilldev.obsitotem;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class FireRunnable extends BukkitRunnable{
	int time;
	Location loc;
	public FireRunnable(int time, Location loc){
		this.time = time;
		this.loc = loc;
	}

	@Override
	public void run() {
		if(time <= 8){
			Firework f = (Firework) loc.getWorld().spawnEntity(loc.add(0, 1, 0), EntityType.FIREWORK);
			FireworkMeta meta = f.getFireworkMeta();
			meta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.BLUE).withFade(Color.FUCHSIA).trail(true).build());
			f.setFireworkMeta(meta);
            try
            {
                Class<?> entityFireworkClass = getClass("net.minecraft.server.", "EntityFireworks");
                Class<?> craftFireworkClass = getClass("org.bukkit.craftbukkit.", "entity.CraftFirework");
                Object firework = craftFireworkClass.cast(f);
                Method handle = firework.getClass().getMethod("getHandle", new Class[0]);
                Object entityFirework = handle.invoke(firework, new Object[0]);
                Field expectedLifespan = entityFireworkClass.getDeclaredField("expectedLifespan");
                Field ticksFlown = entityFireworkClass.getDeclaredField("ticksFlown");
                ticksFlown.setAccessible(true);
                ticksFlown.setInt(entityFirework, expectedLifespan.getInt(entityFirework) - 1);
                ticksFlown.setAccessible(false);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
			new FireRunnable(time + 1, loc).runTaskLater(ObsiTotemPlugin.getInstance(), 5L);
		}
	}
	
    private Class<?> getClass(String prefix, String nmsClassString) throws ClassNotFoundException{
        String version = (new StringBuilder(String.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]))).append(".").toString();
        String name = (new StringBuilder(String.valueOf(prefix))).append(version).append(nmsClassString).toString();
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
}

}
