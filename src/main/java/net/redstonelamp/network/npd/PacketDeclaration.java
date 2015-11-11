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

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PacketDeclaration{
    private final String name;
    private final int identifier; // I know this is byte or short in different protocols, but let's have a common one
    private String comment = "";
    private final List<PacketField> fields = new ArrayList<>();
}
