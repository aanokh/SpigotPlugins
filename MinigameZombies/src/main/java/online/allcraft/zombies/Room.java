package online.allcraft.zombies;

import java.util.ArrayList;

import org.bukkit.Location;

public class Room {
	
	Zombies plugin;
	
	public Location cornerMin;
	public Location cornerMax;
	public String name;
	public ArrayList<Location> spawners;
	// weapon stations
	
	public Room(Zombies plugin, String name, Location corner1, Location corner2, ArrayList<Location> spawners) {
		this.plugin = plugin;
		this.name = name;
		cornerMin = new Location(corner1.getWorld(), Math.min(corner1.getBlockX(), corner2.getBlockX()),
				Math.min(corner1.getBlockY(), corner2.getBlockY()), Math.min(corner1.getBlockZ(), corner2.getBlockZ()));

		cornerMax = new Location(corner1.getWorld(), Math.max(corner1.getBlockX(), corner2.getBlockX()),
				Math.max(corner1.getBlockY(), corner2.getBlockY()), Math.max(corner1.getBlockZ(), corner2.getBlockZ()));
		this.spawners = spawners;
	}
	
	public boolean containsLocation(Location loc) {
		Location location = loc.getBlock().getLocation();
		
		int xmin = cornerMin.getBlockX();
		int ymin = cornerMin.getBlockY();
		int zmin = cornerMin.getBlockZ();

		int xmax = cornerMax.getBlockX();
		int ymax = cornerMax.getBlockY();
		int zmax = cornerMax.getBlockZ();
		if (!(location.getX() >= xmin && location.getX() <= xmax)) {
			return false;
		}
		
		if (!(location.getY() >= ymin && location.getY() <= ymax)) {
			return false;
		}
		
		if (!(location.getZ() >= zmin && location.getZ() <= zmax)) {
			return false;
		}
		
		return true;
	}
}
