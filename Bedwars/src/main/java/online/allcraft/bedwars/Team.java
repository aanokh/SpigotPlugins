package online.allcraft.bedwars;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Team {

	public ArrayList<Player> players;
	public Block bed;
	public String name;
	public Location spawn;
	
	public Team(Location spawn, String name) {
		this.spawn = spawn;
		this.name = name;
	}
	
	public Team(ArrayList<Player> players, Location spawn, String name) {
		this.players = players;
		this.spawn = spawn;
		this.name = name;
	}
	
	public Team(ArrayList<Player> players, Location spawn, String name, Block bed) {
		this.players = players;
		this.spawn = spawn;
		this.name = name;
		this.bed = bed;
	}
	
	public void setBed(Block bed) {
		this.bed = bed;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
}
