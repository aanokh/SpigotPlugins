package online.allcraft.guiCore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import online.allcraft.guiCore.GuiCore;

public class PlayerQuitListener implements Listener {
	
	public GuiCore plugin;
	
	public PlayerQuitListener(GuiCore plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerQuitEvent event) {
		if (plugin.clearInventoryOnLeave) {
			event.getPlayer().getInventory().clear();
		}
	}
}
