package online.allcraft.zombies.listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.md_5.bungee.api.ChatColor;
import online.allcraft.zombies.Room;
import online.allcraft.zombies.Zombies;

public class PlayerDeathListener implements Listener {

	private final Zombies plugin;

	public PlayerDeathListener(Zombies plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Location loc = player.getLocation().getBlock().getLocation();
		for (Room room : plugin.staticOpenRooms) {
			if (room.containsLocation(loc)) {
				// MESSAGE
				ArrayList<Player> onlinePlayers = new ArrayList<Player>(plugin.getServer().getOnlinePlayers());
				for (Player p : onlinePlayers) {
					player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
					player.sendTitle(ChatColor.RED + player.getDisplayName() + " is down", ChatColor.RED + "in " + room.name, 4, 15, 4);
				}
				plugin.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + " is down in " + room.name);
				break;
			}
		}
	}
}
