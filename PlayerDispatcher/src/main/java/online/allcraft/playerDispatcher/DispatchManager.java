package online.allcraft.playerDispatcher;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;
import org.javalite.activejdbc.Base;

public class DispatchManager extends BukkitRunnable {

	private final PlayerDispatcher plugin;

	public DispatchManager(PlayerDispatcher plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		try {
			Base.open("org.postgresql.Driver", 
					"jdbc:postgresql://localhost:5432/" + plugin.SQL_DB, plugin.SQL_USERNAME, plugin.SQL_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Server> servs = Server.where("online = true");
		plugin.log.info(servs.get(0).getString("UBID"));
		
		
		/*
		
		for (ServerType serverType : plugin.serverTypes) {
			if (serverType.playerQueue.isEmpty() && serverType.partyQueue.isEmpty()) {
				continue;
			}

			List<Server> servers = Server.where("online == true and type == ?", serverType.type);

			if (servers.isEmpty()) {
				continue;
			}
			
			for (Server server : servers) {
				int onlinePlayers = server.getInteger("onlinePlayers");
				int maxPlayers = server.getInteger("maxPlayers");
				int playersAvailable = maxPlayers - onlinePlayers;
				if (playersAvailable <= 0) {
					continue;
				}
				
				while (playersAvailable > 0 && !serverType.playerQueue.isEmpty()) {
					plugin.redirectPlayer(serverType.playerQueue.remove(), "zombies");
					playersAvailable--;
				}
			}
		}
		
		*/
		
		Base.close();
	}

}
