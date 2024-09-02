package online.allcraft.hubProtector.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import online.allcraft.hubProtector.HubProtector;

public class BlockPlaceListener implements Listener {
	
	public HubProtector plugin;
	
	public BlockPlaceListener(HubProtector plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		
		if (!plugin.enabled) {
			return;
		}
		
		if (!event.getPlayer().hasPermission(plugin.getServer().getPluginManager().getPermission(plugin.exemptPermissionName))) {
			event.setCancelled(true);
		}
	}
	 
	
	
}
