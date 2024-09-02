package online.allcraft.guiCore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import online.allcraft.guiCore.GuiItem;
import online.allcraft.guiCore.InventoryGui;
import online.allcraft.guiCore.GuiCore;

public class InventoryClickListener implements Listener {
	
	public GuiCore plugin;
	
	public InventoryClickListener(GuiCore plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if (event.getCurrentItem() == null) {
			return;
		}
		
		Inventory invClicked = event.getInventory();
		InventoryGui invGui = null;
		boolean found = false;
		
		for (InventoryGui inv : plugin.inventories) {
			if (inv.inventory.equals(invClicked)) {
				found = true;
				invGui = inv;
				break;
			}
		}
		
		if (!found) {
			
			return;
		}
		
		GuiItem guiItem = null;
		found = false;
		
		for (GuiItem item : invGui.items) {
			if (event.getCurrentItem().getType().equals(item.material)) {
				found = true;
				guiItem = item;
				break;
			}
		}
		
		if (!found) {
			return;
		}
		
		event.setCancelled(true);
		event.getWhoClicked().closeInventory();
		guiItem.onClick(event);
	}
	 
	
	
}
