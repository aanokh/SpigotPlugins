package online.allcraft.bedwars;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnCountdown extends BukkitRunnable {

	public final Bedwars plugin;
	public final Player player;
	public int secondsLeft;
	
	public SpawnCountdown(Bedwars plugin, Player player, int secondsLeft) {
		this.plugin = plugin;
		this.player = player;
		this.secondsLeft = secondsLeft;
	}
	
	@Override
	public void run() {
		if (secondsLeft <= 0) {
			player.teleport(plugin.playerTeams.get(player).spawn);
			player.setGameMode(GameMode.SURVIVAL);
			cancel();
			return;
		}

		if (player == null) {
			cancel();
			return;
		}
		
		player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
		if (secondsLeft < 6) {
			player.sendTitle(ChatColor.GOLD + Integer.toString(secondsLeft), "", 4, 15, 4);
		} else if (secondsLeft == 10) {
			player.sendTitle(ChatColor.GOLD + Integer.toString(secondsLeft), "", 4, 15, 4);
		} else if (secondsLeft == 15) {
			player.sendTitle(ChatColor.GOLD + Integer.toString(secondsLeft), "", 4, 15, 4);
		}
		secondsLeft--;
	}

}
