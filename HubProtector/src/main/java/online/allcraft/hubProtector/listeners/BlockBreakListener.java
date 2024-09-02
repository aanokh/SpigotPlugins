package online.allcraft.hubProtector.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import online.allcraft.hubProtector.HubProtector;

public class BlockBreakListener implements Listener {
	
	public HubProtector plugin;
	
	public BlockBreakListener(HubProtector plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (!plugin.enabled) {
			return;
		}
		
		if (!event.getPlayer().hasPermission(plugin.getServer().getPluginManager().getPermission(plugin.exemptPermissionName))) {
			event.setCancelled(true);
		}
	}
	 
	
	
}
