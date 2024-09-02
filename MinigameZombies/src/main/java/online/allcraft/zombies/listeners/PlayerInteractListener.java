package online.allcraft.zombies.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import online.allcraft.zombies.Door;
import online.allcraft.zombies.GamePlayer;
import online.allcraft.zombies.WeaponStation;
import online.allcraft.zombies.Zombies;

public class PlayerInteractListener implements Listener {

	private final Zombies plugin;

	public PlayerInteractListener(Zombies plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			Location loc = player.getLocation().getBlock().getLocation();
			GamePlayer gamePlayer = plugin.gamePlayers.get(player);
			if (plugin.doorLocations.containsKey(loc)) {
				Door door = plugin.doorLocations.get(loc);
				if (!door.open) {
					if (gamePlayer.getMoney() >= door.cost) {
						door.open(player);
						gamePlayer.setMoney(gamePlayer.getMoney() - door.cost);
					}
				}
			}
			if (plugin.weaponStationLocations.containsKey(loc)) {
				WeaponStation station = plugin.weaponStationLocations.get(loc);
				if (gamePlayer.getMoney() >= station.cost) {
					station.giveItem(player);
					plugin.getServer().broadcastMessage("you bought a weapon lol its WIP");
					gamePlayer.setMoney(gamePlayer.getMoney() - station.cost);
				}
			}
		}
	}
}
