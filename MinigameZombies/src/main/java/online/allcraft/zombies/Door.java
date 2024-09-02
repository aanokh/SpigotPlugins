package online.allcraft.zombies;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Door {

	Zombies plugin;

	public Location cornerMin;
	public Location cornerMax;

	public ArrayList<Location> openingLocations;
	public ArrayList<Location> blockLocations;
	
	public Room room1;
	public Room room2;
	
	public boolean open;
	public int cost;
	
	public Door(Zombies plugin, int cost, Location corner1, Location corner2, Room room1, Room room2) {
		this.plugin = plugin;
		this.openingLocations = new ArrayList<Location>();
		this.blockLocations = new ArrayList<Location>();
		this.open = false; 
		this.cost = cost;
		this.room1 = room1;
		this.room2 = room2;
		
		cornerMin = new Location(corner1.getWorld(), Math.min(corner1.getBlockX(), corner2.getBlockX()),
				Math.min(corner1.getBlockY(), corner2.getBlockY()), Math.min(corner1.getBlockZ(), corner2.getBlockZ()));

		cornerMax = new Location(corner1.getWorld(), Math.max(corner1.getBlockX(), corner2.getBlockX()),
				Math.max(corner1.getBlockY(), corner2.getBlockY()), Math.max(corner1.getBlockZ(), corner2.getBlockZ()));
	}

	public void open(Player player) {

		if (openingLocations.isEmpty()) {
			plugin.getServer().broadcastMessage("DEBUG MESSAGE: WARNING! open() CALLED BEFORE calculateOpeningPositions()");
			return;
		}
		
		for (Location location : blockLocations) {
			location.getWorld().getBlockAt(location).setType(Material.AIR);
		}
		
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(plugin.getServer().getOnlinePlayers());
		open = true;

		if (!plugin.staticOpenRooms.contains(room1)) {
			plugin.staticOpenRooms.add(room1);
			// MESSAGE
			for (Player p : onlinePlayers) {
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				p.sendTitle(ChatColor.GOLD + room1.name + " Opened", ChatColor.GOLD + "By " + player.getDisplayName(), 4, 15, 4);
			}

			plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getDisplayName() + " has opened " + room1.name);
		}
		
		if (!plugin.staticOpenRooms.contains(room2)) {
			plugin.staticOpenRooms.add(room2);
			// MESSAGE
			for (Player p : onlinePlayers) {
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				p.sendTitle(ChatColor.GOLD + room1.name + " Opened", ChatColor.GOLD + "By " + player.getDisplayName(), 4, 15, 4);
			}
			plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getDisplayName() + " has opened " + room1.name);
		}
	}

	public ArrayList<Location> calculateOpeningPositions() {
		if (!openingLocations.isEmpty()) {
			plugin.getServer().broadcastMessage("DEBUG MESSAGE: WARNING! calculateOpeningPositions() CALLED TWICE!");
			return new ArrayList<Location>(openingLocations);
		}
		
		World world = plugin.getServer().getWorld("world");
		
		int xmin = cornerMin.getBlockX();
		int ymin = cornerMin.getBlockY();
		int zmin = cornerMin.getBlockZ();

		int xmax = cornerMax.getBlockX();
		int ymax = cornerMax.getBlockY();
		int zmax = cornerMax.getBlockZ();
		if (zmin == zmax) {
			for (int x = xmin; x <= xmax; x++) {
				openingLocations.add(new Location(world, x, ymin, zmin + 1));
				openingLocations.add(new Location(world, x, ymin, zmin - 1));
				
				for (int y = ymin; y <= ymax; y++) {
					blockLocations.add(new Location(world, x, y, zmin));
				}
			}
		} else if (xmin == xmax) {
			for (int z = zmin; z <= zmax; z++) {
				openingLocations.add(new Location(world, xmin + 1, ymin, z));
				openingLocations.add(new Location(world, xmin - 1, ymin, z));
				
				for (int y = ymin; y <= ymax; y++) {
					blockLocations.add(new Location(world, xmin, y, z));
				}
			}
		}

		return new ArrayList<Location>(openingLocations);
	}
}
