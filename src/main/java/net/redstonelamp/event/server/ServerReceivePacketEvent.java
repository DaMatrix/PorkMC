package net.redstonelamp.event.server;

import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;
import net.redstonelamp.network.UniversalPacket;

public class ServerReceivePacketEvent extends Event implements Cancellable {
	
	private final UniversalPacket packet;
	private boolean cancelled;
	
	public ServerReceivePacketEvent(UniversalPacket packet) {
		this.packet = packet;
	}
	
	public UniversalPacket getPacket() {
		return this.packet;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

}
