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
import net.redstonelamp.level.ChunkPosition;
import net.redstonelamp.request.ChunkRequest;
import net.redstonelamp.request.SpawnRequest;
import net.redstonelamp.ticker.CallableTask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Chunk Sender for MCPC clients. TODO: Merge this with the PE Chunk Sender
 *
 * @author RedstoneLamp Team
 */
public class PcChunkSender{
    public static final int REQUESTS_PER_TICK = 4; //TODO: correct this

    private PCProtocol protocol;
    private final Map<Player, List<ChunkPosition>> requestChunks = new ConcurrentHashMap<>();

    public PcChunkSender(PCProtocol protocol){
        this.protocol = protocol;
        protocol.getServer().getTicker().addRepeatingTask(new CallableTask("tick", this), 1);
    }

    public void tick(long tick){
        if(requestChunks.keySet().isEmpty()){
            return;
        }
        int sent = 0;
        int pLimit = REQUESTS_PER_TICK;
        if(requestChunks.keySet().size() > 1){
            pLimit = REQUESTS_PER_TICK / requestChunks.keySet().size();
            if(pLimit == 0){
                pLimit = 1;
            }
        }
        for(Player player : requestChunks.keySet()){
            if(sent >= REQUESTS_PER_TICK){
                break;
            }

            int pSent = 0;
            List<ChunkPosition> chunks = requestChunks.get(player);
            for(ChunkPosition location : chunks){
                if(pSent >= pLimit){
                    break;
                }

                ChunkRequest r = new ChunkRequest(location);
                player.handleRequest(r);
                chunks.remove(location);
                sent++;
                pSent++;
            }
            if(!chunks.isEmpty()){
                requestChunks.put(player, chunks);
            }else{
                System.out.println("ready!");
                player.handleRequest(new SpawnRequest());
                requestChunks.remove(player);
            }
        }
    }

    public void registerChunkRequests(Player player, int chunksNum){
        if(requestChunks.containsKey(player)){
            throw new IllegalArgumentException("Already in map");
        }
        List<ChunkPosition> chunks = new CopyOnWriteArrayList<>();
        int centerX = (int) player.getPosition().getX();
        int centerZ = (int) player.getPosition().getZ();
        //int centerX = 64;
        //int centerZ = -64;

        int cornerX = centerX - 64;
        int cornerZ = centerZ + 64;

        int x = cornerX;
        int z = cornerZ;

        int chunkNum = 0;
        try{
            while(chunkNum < 96){
                System.out.println("ChunkSender chunk " + x + ", " + z);

                chunks.add(new ChunkPosition(x / 16, z / 16));

                if(x < cornerX + 144){
                    x = x + 16;
                }else{
                    x = cornerX;
                    z = z - 16;
                }
                chunkNum++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        synchronized(requestChunks){
            requestChunks.put(player, chunks);
        }
    }
}
