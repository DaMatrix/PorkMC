/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.network.pc;

import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.nio.BinaryBuffer;

import java.net.SocketAddress;
import java.nio.ByteOrder;

public class DesktopPacket extends UniversalPacket{

    public DesktopPacket(byte[] buffer, SocketAddress address){
        super(buffer, address);
        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        bb.putVarInt(buffer.length);
        bb.put(buffer);
        this.buffer = bb.toArray();
    }

    public DesktopPacket(byte[] buffer, ByteOrder order, SocketAddress address){
        super(buffer, order, address);
        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        bb.putVarInt(buffer.length);
        bb.put(buffer);
        this.buffer = bb.toArray();
    }
}
