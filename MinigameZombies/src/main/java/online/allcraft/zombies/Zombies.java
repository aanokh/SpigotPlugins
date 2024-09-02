package online.allcraft.zombies;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import online.allcraft.gunsCore.GunsCore;
import online.allcraft.gunsCore.ShooterMinigame;
import online.allcraft.gunsCore.Weapon;
import online.allcraft.gunsCore.WeaponType;
import online.allcraft.zombies.GameCountdown;
import online.allcraft.zombies.commands.ZombiesCommand;
import online.allcraft.zombies.listeners.EntityDeathListener;
import online.allcraft.zombies.listeners.PlayerDeathListener;
import online.allcraft.zombies.listeners.PlayerInteractListener;
import online.allcraft.zombies.listeners.PlayerJoinListener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.plugin.PluginManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.slikey.effectlib.util.ParticleEffect;
import net.md_5.bungee.api.ChatColor;

public class Zombies extends JavaPlugin implements ShooterMinigame {
	
	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	public final String ZOMBIES_FILE_NAME = "zombies.yml";
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";
	// ticks
	public final int CLOSE_GAME_DELAYER_DELAY = 1200;
	public final int ENEMY_GENERATOR_SPEED = 20;
	public final int ENEMY_GENERATOR_DELAY = 60;
	public final int GAME_CONTROLLER_SPEED = 20;
	public final int GAME_COUNTDOWN_SPEED = 20;
	public int roundNumber;
	public int playersPerGame;
	public boolean enemyGeneratorFinished;
	String fallbackServer;
	public boolean gameRunning;
	public Location startLocation;
	public ArrayList<Location> zombieSpawners = new ArrayList<Location>();

	public HashMap<Integer, RoundData> enemySpawningData;

	public ArrayList<EnemyType> enemyTypes;
	public ConcurrentHashMap<Creature, EnemyType> enemyMap;
	public ConcurrentHashMap<Location, Door> doorLocations;
	public ConcurrentHashMap<Location, WeaponStation> weaponStationLocations;
	public ConcurrentHashMap<Location, Room> roomLocations;
	public ConcurrentHashMap<Player, GamePlayer> gamePlayers;
	public YamlConfiguration zombies;

	public GameCountdown countdown;
	public EnemyGenerator enemyGenerator;
	public GameController gameController;
	GunsCore gunsCore;
	
	public int starterMoney;
	public int starterXp;

	public ArrayList<Room> staticRooms;
	public ArrayList<Room> staticOpenRooms;

	public void onLoad() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onEnable() {
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		startLocation = new Location(getServer().getWorld("world"), -53, 117, 296);
		enemySpawningData = new HashMap<Integer, RoundData>();
		
		enemyTypes = new ArrayList<EnemyType>();
		enemyMap = new ConcurrentHashMap<Creature, EnemyType>();
		doorLocations = new ConcurrentHashMap<Location, Door>();
		weaponStationLocations = new ConcurrentHashMap<Location, WeaponStation>();
		roomLocations = new ConcurrentHashMap<Location, Room>();
		gamePlayers = new ConcurrentHashMap<Player, GamePlayer>();

		zombies = new YamlConfiguration();
		enemyGenerator = new EnemyGenerator(this);
		gameController = new GameController(this);

		staticOpenRooms = new ArrayList<Room>();
		staticRooms = new ArrayList<Room>();
		
		roundNumber = 0;
		enemyGeneratorFinished = false;
		
		gameRunning = false;
		playersPerGame = 4;
		fallbackServer = "lobby";

		zombieSpawners.add(new Location(getServer().getWorld("world"), 100, 100, 100));
		initializeZombies();
		initializeDependencies();
		registerWeapons();
		initializeConfig();
		registerCommands();
		registerListeners();
		registerEnemies();
		registerRooms();
		registerCurrency();

		// enemyGenerator.runTaskTimer(this, 0, ENEMY_GENERATOR_SPEED);

		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onDisable() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}

	public void registerCommands() {
		getCommand("zombies").setExecutor(new ZombiesCommand(this));
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerInteractListener(this), this);
		pm.registerEvents(new EntityDeathListener(this), this);
		pm.registerEvents(new PlayerJoinListener(this), this);
		pm.registerEvents(new PlayerDeathListener(this), this);
	}
	
	public void registerCurrency() {
		starterMoney = 1000000;
		starterXp = 0;
	}
 
	public void initializeDependencies() {
		Plugin plugin = getServer().getPluginManager().getPlugin("GunsCore");
		if (plugin == null) {
			log.info("[" + DESCRIPTION_FILE.getName() + "]" + ChatColor.RED + "WARNING!" + ChatColor.RESET + 
					" This plugin requires GunsCore to function. GunsCore was not found.");
			setEnabled(false);
		} else if (!(plugin instanceof GunsCore)){
			log.info("[" + DESCRIPTION_FILE.getName() + "]" + ChatColor.RED + "WARNING!" + ChatColor.RESET + 
					" This plugin requires GunsCore to function. GunsCore is malfunctioning or is from the wrong plugin producer.");
			setEnabled(false);
		} else {
			gunsCore = (GunsCore) plugin;
			gunsCore.addToMinigames(this);
			log.info("[" + DESCRIPTION_FILE.getName() + "] Hooked into GunsCore.");
		}
	}
	
	public void registerEnemies() {
		EnemyType knight = new EnemyType(this, EntityType.ZOMBIE, 25, false, Material.DIAMOND_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.GOLD_LEGGINGS, Material.LEATHER_BOOTS, Material.IRON_SWORD);
		EnemyType archer = new EnemyType(this, EntityType.SKELETON, 20, false, null, Material.IRON_CHESTPLATE, null, null, Material.BOW);
		EnemyType worker = new EnemyType(this, EntityType.ZOMBIE, 10, false, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, null,  Material.LEATHER_BOOTS, Material.WOOD_AXE);

		enemyTypes.add(knight);
		enemyTypes.add(archer);
		enemyTypes.add(worker);
		
		// test generation
		// ROUND 1
		HashMap<EnemyType, Float> round1Probabilities = new HashMap<EnemyType, Float>();
		round1Probabilities.put(worker, 0.5F);
		round1Probabilities.put(archer, 0.5F);
		enemySpawningData.put(1, new RoundData(2, round1Probabilities));

		// ROUND 2
		HashMap<EnemyType, Float> round2Probabilities = new HashMap<EnemyType, Float>();
		round2Probabilities.put(worker, 0.8F);
		round2Probabilities.put(worker, 0.2F);
		enemySpawningData.put(2, new RoundData(10, round2Probabilities));

		// ROUND 3
		HashMap<EnemyType, Float> round3Probabilities = new HashMap<EnemyType, Float>();
		round3Probabilities.put(worker, 0.6F);
		round3Probabilities.put(worker, 0.3F);
		round3Probabilities.put(worker, 0.1F);
		enemySpawningData.put(3, new RoundData(12, round3Probabilities));

		// ROUND 4
		HashMap<EnemyType, Float> round4Probabilities = new HashMap<EnemyType, Float>();
		round4Probabilities.put(worker, 0.3F);
		round4Probabilities.put(worker, 0.4F);
		round4Probabilities.put(worker, 0.3F);
		enemySpawningData.put(4, new RoundData(20, round4Probabilities));

		// ROUND 5
		HashMap<EnemyType, Float> round5Probabilities = new HashMap<EnemyType, Float>();
		round5Probabilities.put(archer, 0.2F);
		round5Probabilities.put(archer, 0.4F);
		round5Probabilities.put(archer, 0.4F);
		enemySpawningData.put(5, new RoundData(30, round5Probabilities));

	}

	public void registerWeapons() {
		WeaponType pistol = new WeaponType(gunsCore, "DebugPistolv2", Material.IRON_HOE, Sound.ENTITY_FIREWORK_SHOOT, Sound.BLOCK_ANVIL_PLACE, Sound.BLOCK_DISPENSER_FAIL,
										   true, 5, 3, 2, 100, true, Particle.REDSTONE, 300, 20);
		gunsCore.registerStarterWeaponType(pistol);
		
		WeaponType rifle = new WeaponType(gunsCore, "Rifle", Material.IRON_HOE, Sound.BLOCK_ANVIL_BREAK, Sound.BLOCK_ANVIL_PLACE, Sound.BLOCK_DISPENSER_FAIL,
				   true, 10, 10, 10, 100, true, Particle.REDSTONE, 300, 20);
		
		gunsCore.registerWeaponType(rifle);
	}
	
	public void registerRooms() {
		// test generation
		// ROOMS
		ArrayList<Location> room1Spawners = new ArrayList<Location>();
		room1Spawners.add(new Location(getServer().getWorld("world"), -49, 117, 300));
		room1Spawners.add(new Location(getServer().getWorld("world"), -57, 117, 300));
		room1Spawners.add(new Location(getServer().getWorld("world"), -57, 117, 292));
		
		Room room1 = new Room(this, "Room 1", new Location(getServer().getWorld("world"), -57, 121, 300),
				new Location(getServer().getWorld("world"), -49, 117, 292), room1Spawners);
		staticRooms.add(room1);
		
		ArrayList<Location> room2Spawners = new ArrayList<Location>();
		room2Spawners.add(new Location(getServer().getWorld("world"), -57, 117, 290));
		room2Spawners.add(new Location(getServer().getWorld("world"), -57, 117, 282));
		room2Spawners.add(new Location(getServer().getWorld("world"), -49, 117, 282));
		
		Room room2 = new Room(this, "Room 2", new Location(getServer().getWorld("world"), -57, 121, 282),
				new Location(getServer().getWorld("world"), -49, 117, 290), room2Spawners);
		staticRooms.add(room2);
		
		ArrayList<Location> room3Spawners = new ArrayList<Location>();
		room3Spawners.add(new Location(getServer().getWorld("world"), -47, 117, 282));
		room3Spawners.add(new Location(getServer().getWorld("world"), -39, 117, 282));
		room3Spawners.add(new Location(getServer().getWorld("world"), -39, 117, 290));
		
		Room room3 = new Room(this, "Room 3", new Location(getServer().getWorld("world"), -39, 121, 282),
				new Location(getServer().getWorld("world"), -47, 117, 290), room3Spawners);
		staticRooms.add(room3);
		
		ArrayList<Location> room4Spawners = new ArrayList<Location>();
		room4Spawners.add(new Location(getServer().getWorld("world"), -39, 117, 292));
		room4Spawners.add(new Location(getServer().getWorld("world"), -39, 117, 300));
		room4Spawners.add(new Location(getServer().getWorld("world"), -47, 117, 300));
		
		Room room4 = new Room(this, "Room 4", new Location(getServer().getWorld("world"), -39, 121, 300),
				new Location(getServer().getWorld("world"), -47, 117, 292), room4Spawners);
		staticRooms.add(room4);

		staticOpenRooms.add(room1);
		
		// test generation
		// DOORS
		Door door1 = new Door(this, 10, new Location(getServer().getWorld("world"), -54, 117, 291),
				new Location(getServer().getWorld("world"), -52, 119, 291), room1, room2);
		for (Location loc1 : door1.calculateOpeningPositions()) {
			doorLocations.put(loc1, door1);
		}
		Door door2 = new Door(this, 10, new Location(getServer().getWorld("world"), -48, 117, 285),
				new Location(getServer().getWorld("world"), -48, 119, 287), room2, room3);
		for (Location loc2 : door2.calculateOpeningPositions()) {
			doorLocations.put(loc2, door2);
		}
		Door door3 = new Door(this, 10, new Location(getServer().getWorld("world"), -42, 117, 291),
				new Location(getServer().getWorld("world"), -44, 119, 291), room3, room4);
		for (Location loc3 : door3.calculateOpeningPositions()) {
			doorLocations.put(loc3, door3);
		}
		Door door4 = new Door(this, 10, new Location(getServer().getWorld("world"), -48, 117, 297),
				new Location(getServer().getWorld("world"), -48, 119, 295), room4, room1);
		for (Location loc4 : door4.calculateOpeningPositions()) {
			doorLocations.put(loc4, door4);
		}
		
		// STATIONS
		WeaponStation station1 = new WeaponStation(this, new Location(getServer().getWorld("world"), -39, 117, 296), gunsCore.weaponTypeMaterials.get(Material.IRON_HOE), 5);
		weaponStationLocations.put(station1.location, station1);
	}

	public void initializeZombies() {
		try {
			if (!getDataFolder().exists()) {
				getDataFolder().mkdirs();
			}

			File file = new File(getDataFolder(), ZOMBIES_FILE_NAME);
			if (!file.exists()) {
				zombies.save(new File(getDataFolder(), ZOMBIES_FILE_NAME));
			}

			zombies = YamlConfiguration.loadConfiguration(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		countdown = new GameCountdown(this, 20);
		countdown.runTaskTimer(this, 0, GAME_COUNTDOWN_SPEED);
	}
	
	public void startGame() {
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(getServer().getOnlinePlayers());
		// TELEPORT
		for (Player player : onlinePlayers) {
			player.teleport(startLocation);
		}
		
		nextRound();
	}

	public void nextRound() {
		roundNumber = roundNumber + 1;
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(getServer().getOnlinePlayers());
		// MESSAGE
		for (Player player : onlinePlayers) {
			player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1, 1);
			player.sendTitle(ChatColor.RED + "Round " + roundNumber, "", 4, 15, 4);
		}
		getServer().broadcastMessage(ChatColor.RED + "Round " + roundNumber);
		enemyGeneratorFinished = false;
		enemyGenerator = new EnemyGenerator(this);
		enemyGenerator.runTaskTimer(this, ENEMY_GENERATOR_DELAY, ENEMY_GENERATOR_SPEED);
		getServer().broadcastMessage("generator launched");
	}
	
	@Override
	public void onHit(EntityDamageByEntityEvent event, Weapon weapon) {
		GamePlayer gamePlayer = gamePlayers.get(weapon.player);
		gamePlayer.setMoney(gamePlayer.getMoney() + 10);
	}
	
	public void endGame() {
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(getServer().getOnlinePlayers());
		
		for (Player player : onlinePlayers) {
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 1, 0);
			int roundsSurvived = roundNumber - 1;
			player.sendTitle(ChatColor.RED + "Game Over!", ChatColor.RED + "You survived " + roundsSurvived + " rounds", 4, 15, 4);
			getServer().broadcastMessage(ChatColor.GREEN + "Game Over! You survived " + roundsSurvived + " rounds");
		}
		new CloseGameDelayer(this).runTaskLater(this, CLOSE_GAME_DELAYER_DELAY);
	}
		
	public void closeGame() {
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(getServer().getOnlinePlayers());
		
		for (Player player : onlinePlayers) {
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 0);
			getServer().broadcastMessage(ChatColor.YELLOW + "Redirecting " + player.getDisplayName() + " to another server");
			sendPlayerToFallback(player);
		}
	}
	
	public void sendPlayerToFallback(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(fallbackServer);
		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
}