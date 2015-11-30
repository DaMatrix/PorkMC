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
package net.redstonelamp.network.pe;

import net.redstonelamp.level.ChunkPosition;
import net.redstonelamp.player.Player;
import net.redstonelamp.request.ChunkRequest;
import net.redstonelamp.request.SpawnRequest;
import net.redstonelamp.ticker.CallableTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Chunk Sender for MCPE clients. It works by feeding the Player class fake ChunkRequests, which
 * the Player responds with Chunk Responses.
 *
 * @author RedstoneLamp Team
 */
public class PeChunkSender{
    public static final int REQUESTS_PER_TICK = 4; //TODO: correct this

    private PEProtocol protocol;
    private final Map<Player, List<ChunkPosition>> loaded = new HashMap<>();
    private final Map<Player, Long> lastSent = new ConcurrentHashMap<>();
    private final Map<Player, List<ChunkPosition>> requestChunks = new ConcurrentHashMap<>();

    public PeChunkSender(PEProtocol protocol){
        this.protocol = protocol;
        protocol.getServer().getTicker().addRepeatingTask(new CallableTask("tick", this), 1);
    }

    public void tick(long tick){
        int sent = 0;
        lastSent.keySet().stream().filter(player -> System.currentTimeMillis() - lastSent.get(player) >= 3500).forEach(player -> {
            checkChunks(player);
            lastSent.put(player, System.currentTimeMillis());
        });

        if(requestChunks.keySet().isEmpty()){
            return;
        }

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
                protocol.getManager().getActionPool().execute(() -> player.handleRequest(r));
                chunks.remove(location);
                sent++;
                pSent++;
            }
            if(!chunks.isEmpty()){
                requestChunks.put(player, chunks);
            }else if(!player.isSpawned()){
                System.out.println("ready!");
                protocol.getManager().getActionPool().execute(() -> player.handleRequest(new SpawnRequest()));
                requestChunks.remove(player);
            }else{
                requestChunks.remove(player);
            }
        }
    }

    private boolean checkChunk(Player player, ChunkPosition pos){
        for(ChunkPosition loaded : this.loaded.get(player)){
            if(loaded.equals(pos)){
                return true;
            }
        }
        return false;
    }

    private synchronized void unloadChunk(Player player, ChunkPosition pos){
        List<ChunkPosition> l = loaded.get(player);
        loaded.get(player).stream().filter(loaded -> loaded.equals(pos)).forEach(loaded -> {
            l.remove(loaded);
            player.getPosition().getLevel().unloadChunk(pos);
        });
        loaded.put(player, l);
    }

    private synchronized void checkChunks(Player player){
        synchronized(loaded){
            if(!loaded.containsKey(player)){
                loaded.put(player, new ArrayList<>());
            }
            List<ChunkPosition> oldPositions = new ArrayList<>(loaded.get(player));
            List<ChunkPosition> chunks = new CopyOnWriteArrayList<>();
            List<ChunkPosition> positions = new CopyOnWriteArrayList<>();
            int chunkX = (int) player.getPosition().getX() / 16;
            int chunkZ = (int) player.getPosition().getZ() / 16;
            int sent = 0;
            for(int distance = 6; distance >= 0; distance--){
                for(int x = chunkX - distance; x < chunkX + distance; x++){
                    for(int z = chunkZ - distance; z < chunkZ + distance; z++){
                        if(Math.sqrt((chunkX - x) * (chunkX - x) + (chunkZ - z) * (chunkZ - z)) < 8){
                            if(!checkChunk(player, new ChunkPosition(x, z))){
                                chunks.add(new ChunkPosition(x, z));
                                sent++;
                            }else{
                                positions.add(new ChunkPosition(x, z));
                            }
                        }
                    }
                }
            }
            requestChunks.put(player, chunks);
            positions.addAll(chunks);
            oldPositions.stream().filter(pos -> !positions.contains(pos)).forEach(pos -> unloadChunk(player, pos));
            //System.out.println("Sent: " + sent);
            loaded.put(player, positions);
        }
    }

    public void clearData(Player player){
        loaded.get(player).forEach(chunk -> unloadChunk(player, chunk));
        loaded.remove(player);
        lastSent.remove(player);
        requestChunks.remove(player);
        //System.out.println("data cleared.");
    }

    public void onShutdown(){

    }

    /*
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
            if(sent >= REQUESTS_PER_TICK) break;

            int pSent = 0;
            List<ChunkPosition> chunks = requestChunks.get(player);
            for(ChunkPosition location : chunks){
                if(pSent >= pLimit) break;

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
    */

    public void registerChunkRequests(Player player, int chunksNum){
        if(requestChunks.containsKey(player)){
            throw new IllegalArgumentException("Already in map");
        }

        List<ChunkPosition> chunks = new CopyOnWriteArrayList<>();
        int chunkX = (int) player.getPosition().getX() / 16;
        int chunkZ = (int) player.getPosition().getZ() / 16;
        for(int distance = 5; distance >= 0; distance--){
            for(int x = chunkX - distance; x < chunkX + distance; x++){
                for(int z = chunkZ - distance; z < chunkZ + distance; z++){
                    if(Math.sqrt((chunkX - x) * (chunkX - x) + (chunkZ - z) * (chunkZ - z)) < 5){
                        chunks.add(new ChunkPosition(x, z));
                    }
                }
            }
        }

        /*
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
                //System.out.println("ChunkSender chunk " + x + ", " + z);

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
        */

        requestChunks.put(player, chunks);
        loaded.put(player, chunks);
        lastSent.put(player, System.currentTimeMillis());
    }
}
