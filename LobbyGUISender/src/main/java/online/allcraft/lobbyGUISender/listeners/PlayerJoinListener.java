package online.allcraft.lobbyGUISender.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import online.allcraft.lobbyGUISender.LobbyGUISender;

public class PlayerJoinListener implements Listener {
	
	public LobbyGUISender plugin;
	
	public PlayerJoinListener(LobbyGUISender plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		plugin.sendUDPMessage(true, Bukkit.getOnlinePlayers().size());
	}
}
