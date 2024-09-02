package online.allcraft.customTab.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import online.allcraft.customTab.CustomTab;

public class PlayerJoinListener implements Listener {
	
	public CustomTab plugin;
	
	public PlayerJoinListener(CustomTab plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
	}
	 
	
	
}
