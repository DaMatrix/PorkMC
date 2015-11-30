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

import net.redstonelamp.event.EventPlatform;
import net.redstonelamp.event.server.ServerHandleRequestEvent;
import net.redstonelamp.event.server.ServerHandleResponseEvent;
import net.redstonelamp.event.server.ServerReceivePacketEvent;
import net.redstonelamp.event.server.ServerSendPacketEvent;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.UniversalPacket;
import net.redstonelamp.nio.BinaryBuffer;
import net.redstonelamp.player.Player;
import net.redstonelamp.request.HandshakeRequest;
import net.redstonelamp.request.Request;
import net.redstonelamp.response.HandshakeResponse;
import net.redstonelamp.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the Minecraft: PC protocol.
 *
 * @author RedstoneLamp Team
 */
public class PCProtocol extends Protocol implements PCNetworkConst{

    public PCProtocol(NetworkManager manager){
        super(manager);
        _interface = new NettyInterface(getServer());
    }

    @Override
    public String getName(){
        return "MCPC";
    }

    @Override
    public String getDescription(){
        return "Minecraft: PC edition version " + PCNetworkConst.MC_VERSION + ", protocol: " + PCNetworkConst.MC_PROTOCOL;
    }

    @Override
    public Request[] handlePacket(UniversalPacket up){
        List<Request> requests = new ArrayList<>();
        ServerReceivePacketEvent receiveEvent = new ServerReceivePacketEvent(up);
        getManager().getServer().callEvent(EventPlatform.DESKTOP, receiveEvent);
        if(receiveEvent.isCancelled()){
            return new Request[0];
        }

        /*int length =*/
        up.bb().getVarInt();
        int id = up.bb().getVarInt();

        switch(id){
            case SB_HANDSHAKE_HANDSHAKE:
                int protocol = up.bb().getVarInt();
                String address = up.bb().getVarString();
                short port = up.bb().getUnsignedShort();
                int state = up.bb().getVarInt();

                HandshakeRequest handshake = new HandshakeRequest(protocol, state);
                handshake.address = address;
                handshake.port = port;
                requests.add(handshake);
                System.out.println("RECEIVED HANDSHAKE REQUEST");
                break;
            default:
                System.out.println("GOT ZEH PACKETSIES " + id);
                System.exit(0);
        }

        // Throw ServerHandleRequestEvent
        for(Request request : requests){
            ServerHandleRequestEvent requestEvent = new ServerHandleRequestEvent(request);
            getManager().getServer().getPluginManager().callEvent(EventPlatform.DESKTOP, requestEvent);
            if(requestEvent.isCancelled()){
                requests.remove(request);
            }
        }

        return requests.toArray(new Request[requests.size()]);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected UniversalPacket[] _sendResponse(Response response, Player player){
        List<UniversalPacket> packets = new ArrayList<>();
        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        ServerHandleResponseEvent handleEvent = new ServerHandleResponseEvent(response);
        getManager().getServer().getPluginManager().callEvent(EventPlatform.DESKTOP, handleEvent);
        if(handleEvent.isCancelled()){
            return new UniversalPacket[0];
        }

        if(response instanceof HandshakeResponse){
            HandshakeResponse handshake = (HandshakeResponse) response;
            if(handshake.state == 1){
                bb.putVarInt(PCProtocol.CB_STATUS_RESPONSE);
                JSONObject motd = new JSONObject();
                JSONObject version = new JSONObject();
                version.put("version", PCNetworkConst.MC_VERSION);
                version.put("protocol", PCNetworkConst.MC_PROTOCOL);
                JSONObject players = new JSONObject();
                players.put("max", getManager().getServer().getMaxPlayers());
                players.put("online", getManager().getServer().getPlayers().size());
                JSONArray sample = new JSONArray();
                List<Player> online = getManager().getServer().getPlayers();
                for(int i = 0; i < online.size(); i++){
                    JSONObject current = new JSONObject();
                    current.put("name", online.get(i).getName());
                    current.put("uuid", online.get(i).getUuid());
                    sample.add(i, current);
                }
                players.put("sample", sample);
                motd.put("description", getManager().getServer().getConfig().getString("motd"));
                // TODO: Favicon
                bb.putVarString(motd.toString());
            }
            System.out.println("RECEIVED HANDSHAKE RESPONSE");
            packets.add(new DesktopPacket(bb.toArray(), player.getAddress()));
        }

        for(UniversalPacket packet : packets){
            ServerSendPacketEvent sendEvent = new ServerSendPacketEvent(packet);
            getManager().getServer().getPluginManager().callEvent(EventPlatform.DESKTOP, sendEvent);
            if(sendEvent.isCancelled()){
                packets.remove(packet);
            }
        }

        return packets.toArray(new UniversalPacket[packets.size()]);
    }

    @Override
    protected UniversalPacket[] _sendQueuedResponses(Response[] responses, Player player){
        return new UniversalPacket[0];
    }
}
