package online.allcraft.customTab;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import online.allcraft.customTab.listeners.PlayerJoinListener;

public class CustomTab extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";
	
	PacketPlayOutPlayerListHeaderFooter packet;
	TabUpdater updater;
    ArrayList<ChatComponentText> headers;
	ArrayList<ChatComponentText> footers;
	
	public void onLoad() {   
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onEnable() {
		headers = new ArrayList<ChatComponentText>();
		footers = new ArrayList<ChatComponentText>();
		
		registerCommands();
		registerListeners();
		registerHeadersFooters();
		initializeConfig();
		
		packet = new PacketPlayOutPlayerListHeaderFooter();
		updater = new TabUpdater(this, packet);
		updater.runTaskTimer(this, 0, 20);
		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onDisable() {
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}
	
	public void registerCommands() {
		
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerJoinListener(this), this);
	}

	public void registerHeadersFooters() {
		headers.add(new ChatComponentText("Welcome to " + ChatColor.GREEN + "Allcraft!\n" + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "play.allcraft.online"));
		headers.add(new ChatComponentText("Welcome to " + ChatColor.DARK_GREEN + "Allcraft!\n" + ChatColor.RESET + ChatColor.ITALIC + ChatColor.GRAY + "play.allcraft.online"));
		
		footers.add(new ChatComponentText(ChatColor.ALL_CODES + "Discord: " + ChatColor.RESET + "https://discord.gg/HhKQpFB"));
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