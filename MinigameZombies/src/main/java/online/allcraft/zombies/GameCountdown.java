package online.allcraft.zombies;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountdown extends BukkitRunnable {
	public final Zombies plugin;
	public int secondsLeft;
	
	public GameCountdown(Zombies plugin, int secondsLeft) {
		this.plugin = plugin;
		this.secondsLeft = secondsLeft;
		plugin.getServer().broadcastMessage(ChatColor.GOLD + "Game starting in " + secondsLeft + " seconds!");
	}
	
	@Override
	public void run() {
		if (!plugin.gameRunning) {
			plugin.gameRunning = true;
		}
		
		if (secondsLeft <= 0) {
			plugin.startGame();
			cancel();
			return;
		}
		
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(plugin.getServer().getOnlinePlayers());
		
		//if (!(plugin.getServer().getOnlinePlayers().size() == plugin.playersPerTeam * plugin.numberTeams)) {
		if (!(onlinePlayers.size() == 1)) {
			plugin.getServer().broadcastMessage(ChatColor.RED + "Game start aborted. Not enough players!");
			plugin.gameRunning = false;
			cancel();
			return;
		}
		
		for (Player player : onlinePlayers) {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
			if (secondsLeft < 6) {
				player.sendTitle(ChatColor.GOLD + Integer.toString(secondsLeft), "", 4, 15, 4);
			} else if (secondsLeft == 10) {
				player.sendTitle(ChatColor.GOLD + Integer.toString(secondsLeft), "", 4, 15, 4);
			} else if (secondsLeft == 15) {
				player.sendTitle(ChatColor.GOLD + Integer.toString(secondsLeft), "", 4, 15, 4);
			} else if (secondsLeft == 20) {
				player.sendTitle(ChatColor.GOLD + Integer.toString(secondsLeft), "", 4, 15, 4);
			}
		}
		
		secondsLeft--;
	}

}
