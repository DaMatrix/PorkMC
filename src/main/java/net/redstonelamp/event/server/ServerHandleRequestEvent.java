package net.redstonelamp.event.server;

import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;
import net.redstonelamp.request.Request;

public class ServerHandleRequestEvent extends Event implements Cancellable {
	
	private final Request request;
	private boolean cancelled = false;
	
	public ServerHandleRequestEvent(Request request) {
		this.request = request;
	}
	
	public Request getRequest() {
		return this.request;
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
