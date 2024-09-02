package online.allcraft.zombies.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import online.allcraft.zombies.Zombies;

public class ZombiesCommand implements CommandExecutor {

	private final Zombies plugin;

	public ZombiesCommand(Zombies plugin) {
		this.plugin = plugin;
	}
	
	/* /zombies <subcommand> [parameter]
	 * subcommands:
	 * TODO barricade
	 * TODO weapon [name]
	 * TODO zone [name]
	 * start
	 * stop
	 */
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// check if no parameters
		if (args.length <= 0) {
			sender.sendMessage(ChatColor.RED + "Not enough parameters!");
			return false;
		}
		
		// check if start
		if (args[0].equals("start")) {
			plugin.startGame();
		}
		
		
		return true;
	}
}