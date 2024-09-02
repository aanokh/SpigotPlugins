package online.allcraft.playerDispatcher;

import java.util.LinkedList;

import org.bukkit.entity.Player;

public class ServerType {
	public LinkedList<Player> playerQueue;
	public LinkedList<Party> partyQueue;
	
	// Unique Server Type ID
	public String type;
	
	public ServerType(String type) {
		this.playerQueue = new LinkedList<Player>();
		this.partyQueue = new LinkedList<Party>();
		this.type = type;
	}
}
