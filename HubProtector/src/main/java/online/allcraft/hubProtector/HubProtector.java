package online.allcraft.hubProtector;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import online.allcraft.hubProtector.commands.ProtectionToggleCommand;
import online.allcraft.hubProtector.listeners.BlockBreakListener;
import online.allcraft.hubProtector.listeners.BlockPlaceListener;

public class HubProtector extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";
	public String exemptPermissionName;
	public boolean enabled;
	
	public void onLoad() {   
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onEnable() {
		exemptPermissionName = "hubProtector.exempt";
		enabled = true;
		
		registerCommands();
		registerListeners();
		initializeConfig();
		
		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onDisable() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}
	
	public void registerCommands() {
		this.getCommand("protectionToggle").setExecutor(new ProtectionToggleCommand(this));
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new BlockBreakListener(this), this);
		pm.registerEvents(new BlockPlaceListener(this), this);
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