package online.allcraft.lobbyGUISender;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import online.allcraft.UDP.UDPMessage;
import online.allcraft.lobbyGUISender.listeners.PlayerJoinListener;
import online.allcraft.lobbyGUISender.listeners.PlayerQuitListener;

public class LobbyGUISender extends JavaPlugin {
	public static Logger log = Logger.getLogger("Minecraft");
	private final PluginDescriptionFile DESCRIPTION_FILE = getDescription();
	// do not change value - saveDefaultConfig() will not work
	public final String CONFIG_FILE_NAME = "config.yml";
	
	public void onLoad() {   
		log.info("[" + DESCRIPTION_FILE.getName() + "] Loaded. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onEnable() {
		registerCommands();
		registerListeners();
		sendUDPMessage(true, 0);
		log.info("[" + DESCRIPTION_FILE.getName() + "] Starting up. v" + DESCRIPTION_FILE.getVersion());
	}

	public void onDisable() {
		sendUDPMessage(false, 0);
		log.info("[" + DESCRIPTION_FILE.getName() + "] Stopping. v" + DESCRIPTION_FILE.getVersion());
	}
	
	public void registerCommands() {
		
	}

	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
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
	
	public void sendUDPMessage(boolean online, int players) {
		try {
            InetAddress address = InetAddress.getLocalHost();
            DatagramSocket datagramSocket = new DatagramSocket();  
            
            // Serialize to a byte array
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream); 
            oo.writeObject(new UDPMessage("staff", online, players, Bukkit.getMaxPlayers()));
            oo.close();
            
            byte[] buffer = bStream.toByteArray();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 5000);
            datagramSocket.send(packet);
        } catch(Exception e) {
        }
	}
}