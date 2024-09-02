package online.allcraft.lobbyBooster.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import online.allcraft.lobbyBooster.Main;

public class PlayerInteractListener implements Listener {
	
	public Main plugin;
	
	public PlayerInteractListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.PHYSICAL) {
			return;
		}
		
		if(event.getClickedBlock().getType() != plugin.boostMaterial) {
			return;
		}
		

		Player player = event.getPlayer();
		Vector direction = player.getLocation().getDirection();
		Vector vector = new Vector (direction.getX() * plugin.speedFactor, plugin.yBoost, direction.getZ() * plugin.speedFactor);
		vector.normalize();
		player.setVelocity(vector);
		
		event.setCancelled(true);
	}
	 
	
	
}
