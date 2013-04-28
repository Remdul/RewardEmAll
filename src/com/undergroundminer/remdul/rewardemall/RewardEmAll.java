package com.undergroundminer.remdul.rewardemall;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RewardEmAll extends JavaPlugin implements Listener {

	static String maindir = "Plugins/RewardEmAll/";
	static File config = new File(maindir + "config.yml");

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		new File(maindir).mkdir();
		if (!config.exists()) {
			try {
				config.createNewFile();
				this.getConfig().set("XPTotal", 1000);
				this.getConfig().set("FoodTotal", 20);
				this.getConfig().set("HealTotal", 20);
				this.getConfig().set("CookieTotal", 64);
				this.getConfig().set("CookieName", "Remdul's Amazing Cookie");
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
			saveDefaultConfig();
		}
	}

	@Override
	public void onDisable() {
		this.saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		int xptotal = getConfig().getInt("XPTotal");
		int healtotal = getConfig().getInt("HealTotal");
		int foodtotal = getConfig().getInt("FoodTotal");
		int cookietotal = getConfig().getInt("CookieTotal");
		String cookiename = getConfig().getString("CookieName");

		if (args.length == 0) {
			if (args[0].equalsIgnoreCase("rea")) {
				if (args[1].equalsIgnoreCase("Heal")
						&& sender.hasPermission("rea.heal")) {
					for (Player player : getServer().getOnlinePlayers()) {
						player.setHealth(healtotal);
						player.sendMessage(sender.getName()
								+ " has healed you!");
					}
					return true;
				} else if (args[1].equalsIgnoreCase("Feed")
						&& sender.hasPermission("rea.feed")) {
					for (Player player : getServer().getOnlinePlayers()) {
						player.setFoodLevel(foodtotal);
						player.sendMessage(sender.getName() + " has fed you!");
					}
					return true;
				} else if (args[1].equalsIgnoreCase("xp")
						&& sender.hasPermission("rea.xp")) {
					for (Player player : getServer().getOnlinePlayers()) {
						player.giveExp(xptotal);
						player.sendMessage(sender.getName() + " has given you "
								+ xptotal + " experience!");
					}
					return true;
				} else if (args[1].equalsIgnoreCase("Speed")
						&& sender.hasPermission("rea.speed")) {
					for (Player player : getServer().getOnlinePlayers()) {
						player.addPotionEffect(new PotionEffect(
								PotionEffectType.SPEED, 2000, 2));
						player.sendMessage(sender.getName()
								+ " has given you Speeeeeeeeed!");
					}
					return true;
				} else if (args[1].equalsIgnoreCase("help")
						&& sender.hasPermission("rea.help")) {
					sender.sendMessage("This is the future help line 1. Blah.");
					sender.sendMessage("This is the future help line 2. Blah.");
					sender.sendMessage("This is the future help line 3. Blah.");
					sender.sendMessage("This is the future help line 4. Blah.");
					sender.sendMessage("This is the future help line 5. Blah.");
				} else if (args[1].equalsIgnoreCase("Cookie")
						&& sender.hasPermission("rea.cookie")) {
					for (Player player : getServer().getOnlinePlayers()) {
						ItemStack cookie = new ItemStack(Material.COOKIE,
								cookietotal);
						ItemMeta item = cookie.getItemMeta();
						item.setDisplayName(cookiename);
						cookie.setItemMeta(item);
						player.getInventory().addItem(cookie);
						player.sendMessage(sender.getName()
								+ " has given you some " + cookiename
								+ "! So Tasty!");
					}
					return true;
				} else if (args[1].equalsIgnoreCase("Fireworks")
						&& sender.hasPermission("rea.fireworks")) {
					for (Player player : getServer().getOnlinePlayers()) {
						launchFireWorks(player);
						launchFireWorks(player);
						launchFireWorks(player);
						launchFireWorks(player);
						player.sendMessage(sender.getName()
								+ " celebrates with Fireworks!");
					}
					return true;
				}
				return false;
			}
			return false;
		}
		return false;
	}

	public void launchFireWorks(Player player) {
		Player p = player.getPlayer();
		Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(),
				EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		Random r = new Random();
		int rt = r.nextInt(5) + 1;
		Type type = Type.BALL;
		if (rt == 1)
			type = Type.BALL;
		if (rt == 2)
			type = Type.BALL_LARGE;
		if (rt == 3)
			type = Type.BURST;
		if (rt == 4)
			type = Type.CREEPER;
		if (rt == 5)
			type = Type.STAR;
		int r1i = r.nextInt(17) + 1;
		int r2i = r.nextInt(17) + 1;
		Color c1 = getColor(r1i);
		Color c2 = getColor(r2i);
		FireworkEffect effect = FireworkEffect.builder()
				.flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type)
				.trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}

	private Color getColor(int i) {
		Color c = null;
		if (i == 1) {
			c = Color.AQUA;
		}
		if (i == 2) {
			c = Color.BLACK;
		}
		if (i == 3) {
			c = Color.BLUE;
		}
		if (i == 4) {
			c = Color.FUCHSIA;
		}
		if (i == 5) {
			c = Color.GRAY;
		}
		if (i == 6) {
			c = Color.GREEN;
		}
		if (i == 7) {
			c = Color.LIME;
		}
		if (i == 8) {
			c = Color.MAROON;
		}
		if (i == 9) {
			c = Color.NAVY;
		}
		if (i == 10) {
			c = Color.OLIVE;
		}
		if (i == 11) {
			c = Color.ORANGE;
		}
		if (i == 12) {
			c = Color.PURPLE;
		}
		if (i == 13) {
			c = Color.RED;
		}
		if (i == 14) {
			c = Color.SILVER;
		}
		if (i == 15) {
			c = Color.TEAL;
		}
		if (i == 16) {
			c = Color.WHITE;
		}
		if (i == 17) {
			c = Color.YELLOW;
		}
		return c;
	}
}
