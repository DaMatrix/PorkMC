package net.redstonelamp.response;

public class HandshakeResponse extends Response {
	
	public int protocol;
	public String address;
	public short port;
	public int state;
	
	public HandshakeResponse(int protocol, int state) {
		this.protocol = protocol;
		this.state = state;
	}
	
}
