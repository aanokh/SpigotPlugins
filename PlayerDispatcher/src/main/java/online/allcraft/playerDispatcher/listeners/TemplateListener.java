package online.allcraft.playerDispatcher.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import online.allcraft.playerDispatcher.PlayerDispatcher;

public class TemplateListener implements Listener {
	
	public PlayerDispatcher plugin;
	
	public TemplateListener(PlayerDispatcher plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
	}
}
