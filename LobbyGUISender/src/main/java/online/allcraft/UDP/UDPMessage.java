package online.allcraft.UDP;


import java.io.Serializable;

public class UDPMessage implements Serializable {
	public String server;
	public boolean serverOnline;
	public int playersOnline;
	public int maxPlayers;
	
	// Do not touch! Change if change code!
	private static final long serialVersionUID = 1L;
	
	public UDPMessage(String server, boolean serverOnline, int playersOnline, int maxPlayers) {
		this.server = server;
		this.serverOnline = serverOnline;
		this.playersOnline = playersOnline;
		this.maxPlayers = maxPlayers;
	}
	
}
