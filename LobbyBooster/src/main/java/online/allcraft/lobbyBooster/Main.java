package online.allcraft.lobbyBooster;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import online.allcraft.lobbyBooster.listeners.PlayerInteractListener;

public class Main extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";
	
	public Material boostMaterial;
	public double speedFactor;
	public double yBoost;
	
	public void onLoad() {   
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onEnable() {
		registerListeners();
		initializeConfig();
		
		boostMaterial = Material.GOLD_PLATE;
		speedFactor = 2.5;
		yBoost = 1;
		
		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onDisable() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerInteractListener(this), this);
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