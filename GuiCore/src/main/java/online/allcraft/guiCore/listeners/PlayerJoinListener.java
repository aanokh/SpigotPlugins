package online.allcraft.guiCore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import online.allcraft.guiCore.InventoryGui;
import online.allcraft.guiCore.GuiCore;

public class PlayerJoinListener implements Listener {
	
	public GuiCore plugin;
	
	public PlayerJoinListener(GuiCore plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for (InventoryGui inv : plugin.inventories) {
			if (inv.giveOnJoin) {
				event.getPlayer().getInventory().addItem(inv.getItemStack());
			}
		}
	}
}
