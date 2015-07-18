package redstonelamp.level;

import redstonelamp.Player;
import redstonelamp.Server;
import redstonelamp.level.provider.FakeLevelProvider;
import redstonelamp.network.NetworkChannel;
import redstonelamp.network.packet.FullChunkDataPacket;
import redstonelamp.network.packet.PlayStatusPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base level class.
 */
public class Level {
    public static int CHUNKS_PER_TICK = 4;

    private Server server;
    private LevelProvider provider;
    private Map<Player, List<ChunkLocation>> chunksToSend = new ConcurrentHashMap<>();

    public Level(Server server){
        this.server = server;
        provider = new FakeLevelProvider(); //TODO: Change.
    }

    public void tick(){
        if(chunksToSend.keySet().isEmpty()){
            return;
        }
        Player player = chunksToSend.keySet().iterator().next();
        List<ChunkLocation> chunks = chunksToSend.get(player);
        for(int i = 0; i < CHUNKS_PER_TICK; i++){
            if(i >= chunks.size()){
                PlayStatusPacket psp = new PlayStatusPacket();
                psp.status = PlayStatusPacket.Status.PLAYER_SPAWN;
                psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                player.sendDataPacket(psp);
                chunksToSend.remove(player);
                return;
            }
            byte[] data = provider.orderChunk(chunks.get(i).getX(), chunks.get(i).getZ());
            FullChunkDataPacket dp = new FullChunkDataPacket();
            dp.x = chunks.get(i).getX();
            dp.z = chunks.get(i).getZ();
            dp.payload = data;
            dp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
            player.sendDataPacket(dp);
            chunks.remove(i);
        }

        chunksToSend.put(player, chunks);
    }

    public void queueLoginChunks(Player player){
        if(chunksToSend.containsKey(player)){
            throw new IllegalArgumentException("Chunks already queued.");
        }

        List<ChunkLocation> chunks = new ArrayList<>();
        int chunkX = (int) player.getLocation().getX();
        int chunkZ = (int) player.getLocation().getZ();
        for (int distance = 5; distance >= 0; distance--) {
            for (int x = chunkX - distance; x < chunkX + distance; x++) {
                for (int z = chunkZ - distance; z < chunkZ + distance; z++) {
                    if (Math.sqrt((chunkX - x) * (chunkX - x) + (chunkZ - z) * (chunkZ - z)) < 5) {
                        chunks.add(new ChunkLocation(x, z));
                    }
                }
            }
        }
        chunksToSend.put(player, chunks);
    }
}
