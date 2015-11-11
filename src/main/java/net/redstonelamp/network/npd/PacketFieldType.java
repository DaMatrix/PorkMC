package net.redstonelamp.network.npd;

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

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteOrder;

@Getter
@Setter
@Builder
public class PacketFieldType{
    private final PacketFieldTypeBase baseType;
    private boolean array = false;
    private ByteOrder endianness = ByteOrder.LITTLE_ENDIAN;
    private int skipSize = -1; // only use this if baseType is SKIP

    public void setSkipSize(int size){
        if(baseType != PacketFieldTypeBase.SKIP){
            throw new IllegalStateException("Could not set skip size for packet fields that are not of type SKIP");
        }
    }
}
