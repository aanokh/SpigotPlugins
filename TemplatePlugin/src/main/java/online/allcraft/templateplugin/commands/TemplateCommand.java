package online.allcraft.templateplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import online.allcraft.templateplugin.TemplateMain;

public class TemplateCommand implements CommandExecutor {

	private final TemplateMain plugin;

	public TemplateCommand(TemplateMain plugin) {
		this.plugin = plugin;
	}
	
	//  /command <required> [optional]
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// check if no parameters
		if (args.length <= 0) {
			sender.sendMessage(ChatColor.RED + "Not enough parameters!");
			return false;
		}
		
		return true;
	}
}