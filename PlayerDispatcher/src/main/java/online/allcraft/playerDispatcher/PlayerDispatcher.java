package online.allcraft.playerDispatcher;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import online.allcraft.guiCore.GuiCore;
import online.allcraft.guiCore.GuiItem;
import online.allcraft.guiCore.InventoryGui;
import online.allcraft.playerDispatcher.listeners.TemplateListener;

public class PlayerDispatcher extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";
	public final int DISPATCH_MANAGER_SPEED = 40;
	public final String SQL_USERNAME = "REDACTED";
	public final String SQL_PASSWORD = "REDACTED";
	public final String SQL_DB = "REDACTED";
	
	public GuiCore guiCore;
	
	public DispatchManager dispatchManager;
	
	public ArrayList<ServerType> serverTypes;
	
	
	public void onLoad() {   
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onEnable() {
		dispatchManager = new DispatchManager(this);
		dispatchManager.runTaskTimer(this, 0, DISPATCH_MANAGER_SPEED);
		
		serverTypes = new ArrayList<ServerType>();
		
		initializeDependencies();
		registerListeners();
		registerServerTypes();
		registerInventories();
		initializeConfig();
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onDisable() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new TemplateListener(this), this);
	}
	
	public void registerServerTypes() {
		serverTypes.add(new ServerType("zombies"));
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
		ArrayList<GuiItem> serverItems = new ArrayList<GuiItem>();
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "Click to connect");
		
		serverItems.add(new ServerItem(guiCore, "Zombies", lore, Material.SKULL_ITEM, 3, serverTypes.get(0)));
		
		InventoryGui serverInventory = new InventoryGui(guiCore, "Travel", Material.COMPASS, true, serverItems);
		guiCore.registerInventory(serverInventory, DESCRIPTION_FILE.getName());
	}
	
	public void redirectPlayer(Player player, String UBID) {
		 ByteArrayDataOutput out = ByteStreams.newDataOutput();
		 out.writeUTF("Connect");
		 out.writeUTF(UBID);    
		 //applies to the player you send it to aka Kick To Server.
		 player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
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