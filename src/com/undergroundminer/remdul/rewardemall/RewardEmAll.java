package com.undergroundminer.remdul.rewardemall;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class RewardEmAll extends JavaPlugin implements Listener
{

	static String maindir = "Plugins/RewardEmAll/";
	static File config = new File(maindir + "config.yml");

	public void onEnable() 
	{
		getServer().getPluginManager().registerEvents(this, this);
		new File(maindir).mkdir();
		if (!config.exists())
		{
			try
			{
				config.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			saveDefaultConfig();
		}
	}
    @Override
	public void onDisable()
	{
		this.saveConfig();
	}
    
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
    	int xptotal = getConfig().getInt("XPTotal");
    	int healtotal = getConfig().getInt("HealTotal");
    	int foodtotal = getConfig().getInt("FoodTotal");
    	int cookietotal = getConfig().getInt("CookieTotal");

		
    	if(cmd.getName().equalsIgnoreCase("ReaHeal") && sender.hasPermission("rea.heal")) 
        {
    		for(Player player: getServer().getOnlinePlayers())
    		{
			player.setHealth(healtotal);
			player.sendMessage(sender.getName()+" has healed you!");
        	}
		return true;
        } 
    	else if(cmd.getName().equalsIgnoreCase("ReaFeed") && sender.hasPermission("rea.feed"))
    	{
    		for(Player player: getServer().getOnlinePlayers())
    		{
			player.setFoodLevel(foodtotal);
    	    		player.sendMessage(sender.getName()+" has fed you!");
        	}
        	return true;
    	}
    	else if(cmd.getName().equalsIgnoreCase("Reaxp") && sender.hasPermission("rea.xp"))
    	{
    		for(Player player: getServer().getOnlinePlayers())
    		{
			player.giveExp(xptotal);
    	    		player.sendMessage(sender.getName()+" has given you "+xptotal+" experience!");
        	}
        	return true;
    	}	
    	else if(cmd.getName().equalsIgnoreCase("ReaSpeed") && sender.hasPermission("rea.speed"))
    	{
    		for(Player player: getServer().getOnlinePlayers())
    		{
    		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000, 2));
    	    		player.sendMessage(sender.getName()+" has given you Speeeeeeeeed!");
        	}
        	return true;
    	}
    	else if(cmd.getName().equalsIgnoreCase("ReaCookie") && sender.hasPermission("rea.cookie"))
        {
    		for(Player player: getServer().getOnlinePlayers())
    		{
                ItemStack cookie = new ItemStack(Material.COOKIE, cookietotal);
                ItemMeta item = cookie.getItemMeta();
                item.setDisplayName("Remdul's Amazing Cookie");
                cookie.setItemMeta(item);
                player.getInventory().addItem(cookie);
    		}
    		return true;
        }
        return false;
    }
}

