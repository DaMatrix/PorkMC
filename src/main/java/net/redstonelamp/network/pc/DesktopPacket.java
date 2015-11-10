package net.redstonelamp.network.pc;

import java.net.SocketAddress;
import java.nio.ByteOrder;

import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.nio.BinaryBuffer;

public class DesktopPacket extends UniversalPacket {

	public DesktopPacket(byte[] buffer, SocketAddress address) {
		super(buffer, address);
		BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
		bb.putVarInt(buffer.length);
		bb.put(buffer);
		this.buffer = bb.toArray();
	}

	public DesktopPacket(byte[] buffer, ByteOrder order, SocketAddress address) {
		super(buffer, order, address);
		BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
		bb.putVarInt(buffer.length);
		bb.put(buffer);
		this.buffer = bb.toArray();
	}

}
