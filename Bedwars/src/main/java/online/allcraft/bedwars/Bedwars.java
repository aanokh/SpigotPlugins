package online.allcraft.bedwars;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import online.allcraft.bedwars.listeners.BlockBreakListener;
import online.allcraft.bedwars.listeners.BlockPlaceListener;
import online.allcraft.bedwars.listeners.EntityDamageByEntityListener;
import online.allcraft.bedwars.listeners.PlayerDeathListener;
import online.allcraft.bedwars.listeners.PlayerJoinListener;
import online.allcraft.bedwars.listeners.PlayerRespawnListener;
import online.allcraft.guiCore.GuiCore;
import online.allcraft.guiCore.GuiItem;
import online.allcraft.guiCore.InventoryGui;

public class Bedwars extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";
	//ticks
	public final int GENERATOR_MANAGER_SPEED = 1;
	public final int SPECTATOR_DELAYER_DELAY = 5;
	public final int CLOSE_GAME_DELAYER_DELAY = 1200;
	public final int SPAWN_COUNTDOWN_SPEED = 20;
	public final int GAME_COUNTDOWN_SPEED = 20;
	public ArrayList<Generator> generators;
	public HashMap<Generator, Integer> generatorDelays;
	public GeneratorManager generatorManager;
	public GameCountdown gameCountdown;
	public ArrayList<Team> teams;
	public HashMap<Player, Team> playerTeams;
	public boolean gameRunning;
	public HashSet<Block> placedBlocks;
	public int playersPerTeam;
	public String fallbackServer;
	GuiCore guiCore;
	public void onLoad() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}
	public void onEnable() {
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		generators = new ArrayList<Generator>();
		generatorDelays = new HashMap<Generator, Integer>();
		playerTeams = new HashMap<Player, Team>();
		teams = new ArrayList<Team>();
		generatorManager = new GeneratorManager(this);
		placedBlocks = new HashSet<Block>();
		gameRunning = false;
		
		playersPerTeam = 4;
		fallbackServer = "lobby";
		
		registerListeners();
		registerGenerators();
		initializeConfig();
		initializeDependencies();
		registerInventories();
		registerTeams();
		
		
		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onDisable() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EntityDamageByEntityListener(this), this);
		pm.registerEvents(new PlayerRespawnListener(this), this);
		pm.registerEvents(new PlayerDeathListener(this), this);
		pm.registerEvents(new BlockBreakListener(this), this);
		pm.registerEvents(new BlockPlaceListener(this), this);
		pm.registerEvents(new PlayerJoinListener(this), this);
	}
	
	public void registerGenerators() {
		Location location = new Location(getServer().getWorld("world"), 100, 100, 100);
		generators.add(new Generator(this, Material.DIAMOND, location, 50, 100, 150));
	}
	
	public void initializeDependencies() {
		Plugin plugin = getServer().getPluginManager().getPlugin("GuiCore");
		if (plugin == null) {
			log.info("[" + DESCRIPTION_FILE.getName() + "]" + ChatColor.RED + "WARNING!" + ChatColor.RESET + 
					" This plugin requires GuiCore to function. GuiCore was not found.");
			setEnabled(false);
		} else if (!(plugin instanceof GuiCore)){
			log.info("[" + DESCRIPTION_FILE.getName() + "]" + ChatColor.RED + "WARNING!" + ChatColor.RESET + 
					" This plugin requires GuiCore to function. GuiCore is malfunctioning or is from the wrong plugin producer.");
			setEnabled(false);
		} else {
			guiCore = (GuiCore) plugin;
			log.info("[" + DESCRIPTION_FILE.getName() + "]" + 
					" Hooked into GuiCore.");
		}
	}
	
	public void registerInventories() {
		ArrayList<GuiItem> shopItems = new ArrayList<GuiItem>();
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Click to buy");
		
		ArrayList<ItemStack> cost = new ArrayList<ItemStack>();
		ArrayList<ItemStack> product = new ArrayList<ItemStack>();
		
		cost.add(new ItemStack(Material.GOLD_INGOT, 10));
		product.add(new ItemStack(Material.IRON_SWORD));
		
		shopItems.add(new ShopItem(guiCore, "Iron Sword", lore, Material.IRON_SWORD, 2,	cost, product));
		
		InventoryGui shopInventory = new InventoryGui(guiCore, "Shop", Material.CHEST, true, shopItems);
		guiCore.registerInventory(shopInventory, DESCRIPTION_FILE.getName());
	}
	
	public void registerTeams() {
		Location tempSpawn = new Location(getServer().getWorld("world"), 100, 100, 100);
		teams.add(new Team(tempSpawn, "Red"));
		teams.add(new Team(tempSpawn, "Blue"));
		teams.add(new Team(tempSpawn, "Green"));
		teams.add(new Team(tempSpawn, "Yellow"));
	}

	public void initializeConfig() {
		try {
			if (!getDataFolder().exists()) {
				getDataFolder().mkdirs();
			}

			File file = new File(getDataFolder(), CONFIG_FILE_NAME);

			if (!file.exists()) {
				saveDefaultConfig();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startCountdown() {
		gameCountdown = new GameCountdown(this, 20);
		gameCountdown.runTaskTimer(this, 0, GAME_COUNTDOWN_SPEED);
	}
	
	public void startGame() {
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(getServer().getOnlinePlayers());
		
		// TEAMS
		List<List<Player>> teamLists = Lists.partition(onlinePlayers, 1);
				
		for (int i = 0; i < teamLists.size(); i++) {
			List<Player> list = teamLists.get(i);
			Team team = teams.get(i);
			team.setPlayers(new ArrayList<Player>(list));
			for (Player player : list) {
				playerTeams.put(player, team);
			}
		}
				
		// MESSAGE AND TELEPORT
		for (Player player : onlinePlayers) {
			player.teleport(playerTeams.get(player).spawn);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2);
			player.sendTitle(ChatColor.GREEN + "Game Started!", ChatColor.GREEN + "Good Luck!", 4, 15, 4);
		}
		getServer().broadcastMessage(ChatColor.GREEN + "Game Started!");
		
		// GENERATORS
		for (Generator generator : generators) {
			generatorDelays.put(generator, generator.delay);
		}
		
		generatorManager.runTaskTimer(this, 0, GENERATOR_MANAGER_SPEED);
	}
	
	public void endGame() {
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(getServer().getOnlinePlayers());
		// MESSAGE
		if (teams.size() <= 0) {
			for (Player player : onlinePlayers) {
				player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 1, 1);
				player.sendTitle(ChatColor.GREEN + "Game Over!", ChatColor.GREEN + "It's a Tie!", 4, 15, 4);
				getServer().broadcastMessage(ChatColor.GREEN + "Game Over! It's a Tie!");
			}
		} else if (teams.size() == 1) {
			Team team = teams.get(0);
			for (Player player : onlinePlayers) {
				player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 1, 1);
				player.sendTitle(ChatColor.GREEN + "Game Over!", ChatColor.GREEN + team.name + " Team Won!", 4, 15, 4);
				getServer().broadcastMessage(ChatColor.GREEN + "Game Over! " + team.name + " Team Won!");
			}
		}
		new CloseGameDelayer(this).runTaskLater(this, CLOSE_GAME_DELAYER_DELAY);
	}
	
	public void closeGame() {
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(getServer().getOnlinePlayers());
		
		for (Player player : onlinePlayers) {
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
			getServer().broadcastMessage(ChatColor.YELLOW + "Redirecting " + player.getDisplayName() + " to another server");
			sendPlayerToFallback(player);
		}
	}
	
	public void sendPlayerToFallback(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(fallbackServer);    
		//applies to the player you send it to aka Kick To Server.
		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
}