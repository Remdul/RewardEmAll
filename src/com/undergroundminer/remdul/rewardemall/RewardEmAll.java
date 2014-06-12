package com.undergroundminer.remdul.rewardemall;

import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RewardEmAll extends JavaPlugin implements Listener
{

	static String maindir = "Plugins/RewardEmAll/";
	static File config = new File(maindir + "config.yml");
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Permission permission = null;
	public static Chat chat = null;
	public static Economy economy = null;
	public int checkBit = 0;

	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		setupPermissions();
		setupChat();
		if (!setupEconomy())
		{
			log.severe(String.format(
					"[%s] - Disabled due to no Vault dependency found!",
					getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		new File(maindir).mkdir();
		if (!config.exists())
		{
			try
			{
				config.createNewFile();
				this.getConfig().set("ConfigVersion", 1);
				this.getConfig().set("FireworksTotal", 4);
				this.getConfig().set("XPTotal", 1000);
				this.getConfig().set("FoodTotal", 20);
				this.getConfig().set("HealTotal", 20);
				this.getConfig().set("CookieTotal", 64);
				this.getConfig().set("CookieName", "Remdul's Amazing Cookie");
				this.getConfig().set("msgHeal", "has healed you!");
				this.getConfig().set("msgFeed", "has fed you!");
				this.getConfig().set("msgXp", "has given you some experience!");
				this.getConfig().set("msgFireworks",
						"celebrates with Fireworks!");
				this.getConfig().set("msgItem", "has bestowed a gift on you!");
				this.getConfig().set("msgPay", "has deepened your pocketbook!");
				this.getConfig().set("msgEffect",
						"has given you some exciting boosters!");
				this.getConfig()
						.set("msgCookie", "has given you some cookies!");
				this.saveConfig();
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
	public static String translateColor(String input)
	{
		return input = ChatColor.translateAlternateColorCodes('&', input);
	}

	private boolean setupEconomy()
	{
		if (getServer().getPluginManager().getPlugin("Vault") == null)
		{
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
		{
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}

	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null)
		{
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}
	private boolean setupChat()
	{
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager()
				.getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args)
	{
		int xptotal = getConfig().getInt("XPTotal");
		int healtotal = getConfig().getInt("HealTotal");
		int foodtotal = getConfig().getInt("FoodTotal");
		int cookietotal = getConfig().getInt("CookieTotal");
		int fireworkstotal = getConfig().getInt("FireworksTotal");
		String msgHeal = getConfig().getString("msgHeal");
		String msgFeed = getConfig().getString("msgFeed");
		String msgXp = getConfig().getString("msgXp");
		String msgFireworks = getConfig().getString("msgFireworks");
		String msgItem = getConfig().getString("msgItem");
		String msgPay = getConfig().getString("msgPay");
		String msgEffect = getConfig().getString("msgEffect");
		String msgCookie = getConfig().getString("msgCookie");
		Player p = (Player) sender;
		String cookiename = getConfig().getString("CookieName");
		String pprefix = chat.getPlayerPrefix(p.getWorld(), p.getName());

		if ((args.length > 1)
				&& (!(cmd.getName().equalsIgnoreCase("rea") && (args[0]
						.equalsIgnoreCase("item")
						|| args[0].equalsIgnoreCase("effect") || args[0]
							.equalsIgnoreCase("pay")))))
		{
			sender.sendMessage("Too many Arguments. Please refer to: /rea help");
			return true;
		}
		if ((args.length > 3)
				&& ((cmd.getName().equalsIgnoreCase("rea") && (args[0]
						.equalsIgnoreCase("item")))))
		{
			sender.sendMessage("Too many Arguments. Please refer to: /rea help");
			return true;
		}
		if ((args.length > 4)
				&& ((cmd.getName().equalsIgnoreCase("rea") && (args[0]
						.equalsIgnoreCase("effect")))))
		{
			sender.sendMessage("Too many Arguments. Please refer to: /rea help");
			return true;
		}
		if ((args.length > 2)
				&& ((cmd.getName().equalsIgnoreCase("rea") && args[0]
						.equalsIgnoreCase("pay"))))
		{
			sender.sendMessage("Too many Arguments. Please refer to: /rea help");
			return true;
		}
		else if (args.length == 0)
		{
			sender.sendMessage("Not Enough Arguments. Please refer to: /rea help");
			return true;
		}
		else if ((args.length < 3) && cmd.getName().equalsIgnoreCase("rea")
				&& (args[0].equalsIgnoreCase("item")))
		{
			sender.sendMessage("Not Enough Arguments. Please refer to: /rea help");
			return true;
		}
		else if ((args.length < 4) && cmd.getName().equalsIgnoreCase("rea")
				&& (args[0].equalsIgnoreCase("effect")))
		{
			sender.sendMessage("Not Enough Arguments. Please refer to: /rea help");
			return true;
		}

		else if ((args.length < 2) && cmd.getName().equalsIgnoreCase("rea")
				&& args[0].equalsIgnoreCase("pay"))
		{
			sender.sendMessage("Not Enough Arguments. Please refer to: /rea help");
			return true;
		}
		else if (args.length == 1
				|| (args[0].equalsIgnoreCase("item")
						|| args[0].equalsIgnoreCase("pay") || args[0]
							.equalsIgnoreCase("effect")))
		{
			if (cmd.getName().equalsIgnoreCase("rea"))
			{
				if (args[0].equalsIgnoreCase("Heal")
						&& permission.has(sender, "rea.heal"))
				{
					for (Player player : getServer().getOnlinePlayers())
					{
						player.setHealth(healtotal);
						player.sendMessage(translateColor(pprefix)
								+ sender.getName() + " " + msgHeal);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("Feed")
						&& permission.has(sender, "rea.feed"))
				{
					for (Player player : getServer().getOnlinePlayers())
					{
						player.setFoodLevel(foodtotal);
						player.sendMessage(translateColor(pprefix)
								+ sender.getName() + " " + msgFeed);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("xp")
						&& permission.has(sender, "rea.xp"))
				{
					for (Player player : getServer().getOnlinePlayers())
					{
						player.giveExp(xptotal);
						player.sendMessage(translateColor(pprefix)
								+ sender.getName() + " " + msgXp);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("effect")
						&& permission.has(sender, "rea.effect"))
				{

					int dur = Integer.parseInt(args[2]);
					int amp = Integer.parseInt(args[3]);
					String effect = args[1];
					for (Player player : getServer().getOnlinePlayers())
					{
						if (effectsChooser(player, effect, dur, amp) == true)
						{
							player.sendMessage(translateColor(pprefix)
									+ sender.getName() + " " + msgEffect);
							return true;
						}
						else
						{
							return true;
						}
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("help")
						&& permission.has(sender, "rea.help"))
				{
					sender.sendMessage(ChatColor.YELLOW
							+ "Reward Em' All! - Affects Everyone Online!.");
					sender.sendMessage(ChatColor.BLUE
							+ "+----------------------------------------------+");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea help - This Screen");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea cookie - Gives everyone cookies");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea xp - Gives everyone some xp");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea effect <effect> <duration> <amplitude>");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea heal - Heals everyone online");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea feed - Feeds everyone online");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea fireworks - No better way to celebrate!");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea item <item> <amount> - Gives everyone X Items");
					sender.sendMessage(ChatColor.DARK_GRAY
							+ "  /rea pay <amount> - Gives everyone X Currency");
					sender.sendMessage(ChatColor.BLUE
							+ "+----------------------------------------------+");
					return true;
				}
				else if (args[0].equalsIgnoreCase("Cookie")
						&& permission.has(sender, "rea.cookie"))
				{
					for (Player player : getServer().getOnlinePlayers())
					{
						ItemStack cookie = new ItemStack(Material.COOKIE,
								cookietotal);
						ItemMeta item = cookie.getItemMeta();
						item.setDisplayName(cookiename);
						cookie.setItemMeta(item);
						player.getInventory().addItem(cookie);
						player.sendMessage(translateColor(pprefix)
								+ sender.getName() + " " + msgCookie);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("Pay")
						&& permission.has(sender, "rea.pay"))
				{
					for (Player player : getServer().getOnlinePlayers())
					{
						EconomyResponse r = economy.depositPlayer(
								player.getName(), Integer.parseInt(args[1]));
						if (r.transactionSuccess())
						{
							sender.sendMessage(String.format(
									"You have given everyone %s!",
									economy.format(r.amount)));
						}
						else
						{
							sender.sendMessage(String.format(
									"An error occured: %s", r.errorMessage));
						}
						player.sendMessage(translateColor(pprefix)
								+ sender.getName() + " " + msgPay);
						player.sendMessage("You now have: " + r.balance);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("Item")
						&& permission.has(sender, "rea.item"))
				{
					for (Player player : getServer().getOnlinePlayers())
					{
						PlayerInventory inventory = player.getInventory();
						if (args[1].indexOf(":") != -1)
						{
							String itemvalue[] = args[1].split(":");
							String item = itemvalue[0];
							String value = itemvalue[1];
							short valuemod = 0;
							try
							{
								valuemod = Short.parseShort(value);
							}
							catch (NumberFormatException e)
							{
								sender.sendMessage("Error: Make sure you're using the proper data value!");
								return true;
							}

							Material modstack = Material.matchMaterial(item
									.toUpperCase());
							ItemStack stack = new ItemStack(modstack);
							stack.setAmount(Integer.parseInt(args[2]));
							stack.setDurability(valuemod);
							inventory.addItem(stack);
							player.sendMessage(translateColor(pprefix)
									+ sender.getName() + " " + msgItem);
							return true;
						}
						else
						{
							Material newstack = Material.matchMaterial(args[1]
									.toUpperCase());

							if (newstack == null)
							{
								sender.sendMessage("Invalid Item. If item has two words, replace the spaces with _ or use the Item ID.");
								return false;
							}
							ItemStack stack = new ItemStack(newstack);
							stack.setAmount(Integer.parseInt(args[2]));
							inventory.addItem(stack);
							player.sendMessage(translateColor(pprefix)
									+ sender.getName() + " " + msgItem);
						}
						return true;
					}
				}
				else if (args[0].equalsIgnoreCase("Fireworks")
						&& permission.has(sender, "rea.fireworks"))
				{
					for (Player player : getServer().getOnlinePlayers())
					{
						for (int x = fireworkstotal; x > 0; x = x - 1)
						{
							launchFireWorks(player);
						}
						player.sendMessage(translateColor(pprefix)
								+ sender.getName() + " " + msgFireworks);
					}
					return true;
				}
				else
				{
					sender.sendMessage("Not a recognized command. Usage: /rea help");
				}
			}
		}

		return true;
	}

	public boolean effectsChooser(Player player, String effect, int dur, int amp)
	{
		if (effect.equalsIgnoreCase("Absorption"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.ABSORPTION, dur, amp));
		}
		else if (effect.equalsIgnoreCase("Blindness"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
					dur, amp));
		}
		else if (effect.equalsIgnoreCase("Confusion"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					dur, amp));
		}
		else if (effect.equalsIgnoreCase("Damage_resistance"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.DAMAGE_RESISTANCE, dur, amp));
		}
		else if (effect.equalsIgnoreCase("FAST_DIGGING"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.FAST_DIGGING, dur, amp));
		}
		else if (effect.equalsIgnoreCase("FIRE_RESISTANCE"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.FIRE_RESISTANCE, dur, amp));
		}
		else if (effect.equalsIgnoreCase("HARM"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, dur,
					amp));
		}
		else if (effect.equalsIgnoreCase("HEAL"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, dur,
					amp));
		}
		else if (effect.equalsIgnoreCase("HEALTH_BOOST"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.HEALTH_BOOST, dur, amp));
		}
		else if (effect.equalsIgnoreCase("HUNGER"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,
					dur, amp));
		}
		else if (effect.equalsIgnoreCase("INCREASE_DAMAGE"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.INCREASE_DAMAGE, dur, amp));
		}
		else if (effect.equalsIgnoreCase("INVISIBILITY"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.INVISIBILITY, dur, amp));
		}
		else if (effect.equalsIgnoreCase("JUMP"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, dur,
					amp));
		}
		else if (effect.equalsIgnoreCase("NIGHT_VISION"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.NIGHT_VISION, dur, amp));
		}
		else if (effect.equalsIgnoreCase("POISON"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
					dur, amp));
		}
		else if (effect.equalsIgnoreCase("REGENERATION"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.REGENERATION, dur, amp));
		}
		else if (effect.equalsIgnoreCase("SATURATION"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.SATURATION, dur, amp));
		}
		else if (effect.equalsIgnoreCase("SLOW"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.SLOW_DIGGING, dur, amp));
		}
		else if (effect.equalsIgnoreCase("Speed"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
					dur, amp));
		}
		else if (effect.equalsIgnoreCase("WATER_BREATHING"))
		{
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.WATER_BREATHING, dur, amp));
		}
		else if (effect.equalsIgnoreCase("WEAKNESS"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
					dur, amp));
		}
		else if (effect.equalsIgnoreCase("WITHER"))
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,
					dur, amp));
		}
		else
		{
			player.sendMessage("Error: Please check your effect. If there's a space, use _.");
			player.sendMessage("Use one of these Effects:   Absorption, Blindness, Confusion, Damage_resistance, Fast_digging, Fire_resistance, Harm, Heal, Health_boost, Hunger, Increase_Damage, Invisibility, Jump, Night_vision, Poison, Regeneration, Saturation, Slow, Slow_digging, Speed, Water_breathing, Weakness, Wither");
			return false;
		}
		return true;
	}

	public void launchFireWorks(Player player)
	{
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

	private Color getColor(int i)
	{
		Color c = null;
		if (i == 1)
		{
			c = Color.AQUA;
		}
		if (i == 2)
		{
			c = Color.BLACK;
		}
		if (i == 3)
		{
			c = Color.BLUE;
		}
		if (i == 4)
		{
			c = Color.FUCHSIA;
		}
		if (i == 5)
		{
			c = Color.GRAY;
		}
		if (i == 6)
		{
			c = Color.GREEN;
		}
		if (i == 7)
		{
			c = Color.LIME;
		}
		if (i == 8)
		{
			c = Color.MAROON;
		}
		if (i == 9)
		{
			c = Color.NAVY;
		}
		if (i == 10)
		{
			c = Color.OLIVE;
		}
		if (i == 11)
		{
			c = Color.ORANGE;
		}
		if (i == 12)
		{
			c = Color.PURPLE;
		}
		if (i == 13)
		{
			c = Color.RED;
		}
		if (i == 14)
		{
			c = Color.SILVER;
		}
		if (i == 15)
		{
			c = Color.TEAL;
		}
		if (i == 16)
		{
			c = Color.WHITE;
		}
		if (i == 17)
		{
			c = Color.YELLOW;
		}
		return c;
	}
}
