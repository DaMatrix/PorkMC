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

import net.redstonelamp.Player;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.Response;

/**
 * An implementation of the Minecraft: PC protocol.
 *
 * @author RedstoneLamp Team
 */
public class PCProtocol extends Protocol {


    public PCProtocol(NetworkManager manager) {
        super(manager);
        _interface = new NettyInterface(getServer());
    }

    @Override
    public String getName() {
        return "MCPC";
    }

    @Override
    public String getDescription() {
        return "Minecraft: PC edition version " + PCNetworkConst.MC_VERSION + ", protocol: " + PCNetworkConst.MC_PROTOCOL;
    }

    @Override
    public Request[] handlePacket(UniversalPacket packet) {
        return new Request[0];
    }

    @Override
    protected UniversalPacket[] _sendResponse(Response response, Player player) {
        return new UniversalPacket[0];
    }

    @Override
    protected UniversalPacket[] _sendQueuedResponses(Response[] responses, Player player) {
        return new UniversalPacket[0];
    }
}
