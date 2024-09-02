package online.allcraft.lobbyGUISender.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import online.allcraft.lobbyGUISender.LobbyGUISender;

public class PlayerQuitListener implements Listener {
	
	public LobbyGUISender plugin;
	
	public PlayerQuitListener(LobbyGUISender plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.sendUDPMessage(true, Bukkit.getOnlinePlayers().size() - 1);
	}
}
