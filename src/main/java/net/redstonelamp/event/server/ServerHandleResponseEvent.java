package net.redstonelamp.event.server;

import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;
import net.redstonelamp.response.Response;

public class ServerHandleResponseEvent extends Event implements Cancellable {
	
	private final Response response;
	private boolean cancelled = false;
	
	public ServerHandleResponseEvent(Response response) {
		this.response = response;
	}
	
	public Response getResposne() {
		return response;
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
