package online.allcraft.guiCore;

import java.io.File;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import online.allcraft.guiCore.listeners.InventoryClickListener;
import online.allcraft.guiCore.listeners.PlayerInteractListener;
import online.allcraft.guiCore.listeners.PlayerJoinListener;
import online.allcraft.guiCore.listeners.PlayerQuitListener;
import online.allcraft.guiCore.GuiItem;

public class GuiCore extends JavaPlugin {

	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";

	public boolean clearInventoryOnLeave = true;
	public ArrayList<InventoryGui> inventories;
	
	public void onLoad() {   
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}
	
	public void onEnable() {
		inventories = new ArrayList<InventoryGui>();
		registerListeners();
		initializeConfig();
		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}
	
	public void onDisable() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}
	
	public void registerInventory(InventoryGui inventory, String pluginName) {
		inventories.add(inventory);
		log.info("[" + DESCRIPTION_FILE.getName() + "]" + 
				" Inventory " + inventory.name + " registered by " + pluginName);
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new InventoryClickListener(this), this);
		pm.registerEvents(new PlayerInteractListener(this), this);
		pm.registerEvents(new PlayerJoinListener(this), this);
		pm.registerEvents(new PlayerQuitListener(this), this);
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