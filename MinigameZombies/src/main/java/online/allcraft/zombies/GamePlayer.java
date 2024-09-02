package online.allcraft.zombies;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GamePlayer {
	
	Zombies plugin;
	
	private Player player;
	private int money;
	private int xp;
	
	public GamePlayer(Player player, int starterMoney, int starterXp) {
		this.player = player;
		money = starterMoney;
		xp = starterXp;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int amount) {
		money = amount;
	}
}
