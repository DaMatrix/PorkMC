package net.redstonelamp.request;

public class HandshakeRequest extends Request {
	
	public int protocol;
	public String address;
	public short port;
	public int state;
	
	public HandshakeRequest(int protocol,int state) {
		this.protocol = protocol;
		this.state = state;
	}
	
	@Override
	public void execute() {
		// TODO ?
	}

}
