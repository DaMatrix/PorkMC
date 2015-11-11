package net.redstonelamp.event.server;

import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;
import net.redstonelamp.network.UniversalPacket;

public class ServerSendPacketEvent extends Event implements Cancellable {
	
	private final UniversalPacket packet;
	private boolean cancelled;
	
	public ServerSendPacketEvent(UniversalPacket packet) {
		this.packet = packet;
	}
	
	public UniversalPacket getPacket() {
		return packet;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

}
