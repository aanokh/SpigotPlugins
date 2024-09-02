package online.allcraft.guiCore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import online.allcraft.guiCore.InventoryGui;
import online.allcraft.guiCore.GuiCore;

public class PlayerInteractListener implements Listener {
	
	public GuiCore plugin;
	
	public PlayerInteractListener(GuiCore plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent event) {
		if (!event.hasItem()) {
			return;
		}
		
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
			return;
		}
		
		InventoryGui invGui = null;
		Boolean found = false;
		
		for (InventoryGui inv : plugin.inventories) {
			if (event.getMaterial() == inv.material) {
				found = true;
				invGui = inv;
				break;
			}
			
		}
		
		if (found) {
			event.getPlayer().openInventory(invGui.inventory);
			event.setCancelled(true);
		}
	}
}
