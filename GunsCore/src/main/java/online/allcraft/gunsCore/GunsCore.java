package online.allcraft.gunsCore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import online.allcraft.gunsCore.listeners.EntityDamageByEntityListener;
import online.allcraft.gunsCore.listeners.PlayerDropItemListener;
import online.allcraft.gunsCore.listeners.PlayerInteractListener;
import online.allcraft.gunsCore.listeners.PlayerItemHeldListener;
import online.allcraft.gunsCore.listeners.PlayerJoinListener;
import online.allcraft.gunsCore.listeners.ProjectileHitListener;

public class GunsCore extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";

	// ticks
	public final int SHOOTING_MANAGER_SPEED = 1;
	public final int PARTICLE_MANAGER_SPEED = 1;
	public HashMap<Snowball, Weapon> snowballToWeapon;
	public boolean preventWeaponDrops;
	public HashMap<Material, WeaponType> weaponTypeMaterials;
	public CopyOnWriteArrayList<Weapon> shootingDelays;
	public CopyOnWriteArrayList<Weapon> reloadDelays;
	public ConcurrentHashMap<Player, GunPlayer> gunPlayers;
	public ArrayList<ShooterMinigame> shooterMinigames;
	public ArrayList<WeaponType> starterWeaponTypes;

	public ProtocolManager protocolManager;
	
	public BukkitRunnable particles;
	public BukkitRunnable shootingManager;
	public int starterMoney;
	public int starterXp;
	
	public void onLoad() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		
		preventWeaponDrops = false;
		
		snowballToWeapon = new HashMap<Snowball, Weapon>();
		weaponTypeMaterials = new HashMap<Material, WeaponType>();
		shootingDelays = new CopyOnWriteArrayList<Weapon>();
		reloadDelays = new CopyOnWriteArrayList<Weapon>();
		gunPlayers = new ConcurrentHashMap<Player, GunPlayer>();
		particles = new ParticleManager(this);
		shootingManager = new ShootingManager(this);
		shooterMinigames = new ArrayList<ShooterMinigame>();

		starterWeaponTypes = new ArrayList<WeaponType>();

		initializeConfig();
		registerListeners();

		particles.runTaskTimer(this, 0, PARTICLE_MANAGER_SPEED);
		//shootingManager.runTaskTimerAsynchronously(this, 0, SHOOTING_MANAGER_SPEED);
		shootingManager.runTaskTimer(this, 0, SHOOTING_MANAGER_SPEED);
		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onDisable() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}

	public boolean addToMinigames(ShooterMinigame minigame) {
		if (shooterMinigames != null) {
			shooterMinigames.add(minigame);
			return true;
		} else {
			return false;
		}
	}
	
	public void registerWeaponType(WeaponType weaponType) {
		weaponTypeMaterials.put(weaponType.material, weaponType);
	}
	
	public void registerStarterWeaponType(WeaponType weaponType) {
		weaponTypeMaterials.put(weaponType.material, weaponType);
		starterWeaponTypes.add(weaponType);
	}
	
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerJoinListener(this), this);
		pm.registerEvents(new PlayerInteractListener(this), this);
		pm.registerEvents(new ProjectileHitListener(this), this);
		pm.registerEvents(new EntityDamageByEntityListener(this), this);
		pm.registerEvents(new PlayerItemHeldListener(this), this);
		pm.registerEvents(new PlayerDropItemListener(this), this);
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
}