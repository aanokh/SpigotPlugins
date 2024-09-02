package online.allcraft.zombies;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Barricade {

	Zombies plugin;
	public Location cornerMin;
	public Location cornerMax;

	public ArrayList<Location> repairLocations;
	public ArrayList<Location> blockLocations;
	
	public Material material;
	
	public int rewardPerBlock;
	
	public Barricade(Zombies plugin, int rewardPerBlock, Material material, Location corner1, Location corner2) {
		this.plugin = plugin;
		this.repairLocations = new ArrayList<Location>();
		this.blockLocations = new ArrayList<Location>();
		this.rewardPerBlock = rewardPerBlock;
		this.material = material;
		
		cornerMin = new Location(corner1.getWorld(), Math.min(corner1.getBlockX(), corner2.getBlockX()),
				Math.min(corner1.getBlockY(), corner2.getBlockY()), Math.min(corner1.getBlockZ(), corner2.getBlockZ()));

		cornerMax = new Location(corner1.getWorld(), Math.max(corner1.getBlockX(), corner2.getBlockX()),
				Math.max(corner1.getBlockY(), corner2.getBlockY()), Math.max(corner1.getBlockZ(), corner2.getBlockZ()));
	}
}
