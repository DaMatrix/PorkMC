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
package net.redstonelamp.response;

/**
 * Represents a protocol-independent DisconnectionResponse
 *
 * @author RedstoneLamp Team
 */
public class DisconnectResponse extends Response{
    public static final boolean DEFAULT_notifyClient = true;

    public String reason;
    public boolean notifyClient = DEFAULT_notifyClient;

    public DisconnectResponse(String reason){
        this.reason = reason;
    }
}
