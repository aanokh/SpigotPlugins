package online.allcraft.hubProtector.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import online.allcraft.hubProtector.HubProtector;

public class ProtectionToggleCommand implements CommandExecutor {

	private final HubProtector plugin;

	public ProtectionToggleCommand(HubProtector plugin) {
		this.plugin = plugin;
	}
	
	//  /command <required> [optional]
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// check if no parameters
		if (args.length > 0) {
			sender.sendMessage(ChatColor.RED + "Too many parameters!");
			return false;
		}
		
		if (plugin.enabled) {
			plugin.enabled = false;
			sender.sendMessage(ChatColor.RED + "Protection Disabled");
		} else {
			plugin.enabled = true;
			sender.sendMessage(ChatColor.GREEN + "Protection Enabled");
		}
		
		return true;
	}
}