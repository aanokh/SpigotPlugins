package online.allcraft.templateplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import online.allcraft.templateplugin.TemplateMain;

public class TemplateListener implements Listener {
	
	public TemplateMain plugin;
	
	public TemplateListener(TemplateMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
	}
}
