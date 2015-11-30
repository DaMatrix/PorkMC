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
package net.redstonelamp.player;

import net.redstonelamp.Server;
import net.redstonelamp.inventory.PlayerInventory;
import net.redstonelamp.level.Level;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.nio.BinaryBuffer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Simple Implementation of a PlayerDatabase. This implementation
 * saves to one big file. The file is NOT compressed.
 *
 * @author RedstoneLamp Team
 */
public class SimplePlayerDatabase implements PlayerDatabase{
    /**
     * Storage Version of SimplePlayerDatabase. It is used to prevent strange things while
     * processing outdated databases. The version is always the first byte of the file.
     */
    public static final byte STORAGE_VERSION = 4;
    private final Server server;
    private final Map<String, PlayerData> entries = new ConcurrentHashMap<>();

    public SimplePlayerDatabase(Server server){
        this.server = server;
    }

    @Override
    public void loadFrom(File location) throws IOException{
        if(!location.isFile()){
            server.getLogger().warning("Could not locate PlayerDatabase, creating new...");
            location.createNewFile();
            saveTo(location);
            return;
        }
        byte[] data = FileUtils.readFileToByteArray(location);
        if(data == null || data.length < 1){
            throw new IOException("DatabaseFile empty (corrupt database?)");
        }
        BinaryBuffer bb = BinaryBuffer.wrapBytes(data, ByteOrder.LITTLE_ENDIAN);
        if(bb.getByte() != STORAGE_VERSION){
            throw new IOException("Database is outdated!");
        }
        int numEntries = bb.getVarInt();
        for(int i = 0; i < numEntries; i++){
            readEntry(bb);
        }
        if(bb.getByte() != 0x7E){
            server.getLogger().warning("[Malformed Database?] End of database entry does not match!");
        }
    }

    @SuppressWarnings("unchecked")
    private void readEntry(BinaryBuffer bb){
        PlayerData data = new PlayerData();
        data.setUuid(bb.getUUID());

        float x = bb.getFloat();
        float y = bb.getFloat();
        float z = bb.getFloat();
        float yaw = bb.getFloat();
        float pitch = bb.getFloat();
        String name = bb.getVarString();
        Level level = server.getLevelManager().getLevelByName(name);
        if(level == null){
            server.getLogger().warning("Could not find level: \"" + name + "\", player " + data.getUuid().toString() + " will spawn in main level");
            level = server.getLevelManager().getMainLevel();
        }
        data.setPosition(new Position(x, y, z, yaw, pitch, level));
        data.setHealth(bb.getVarInt());
        data.setGamemode(bb.getVarInt());

        String inventoryImpl = bb.getVarString();
        try{
            Class invClass = Class.forName(inventoryImpl);
            if(!PlayerInventory.class.isAssignableFrom(invClass)){
                server.getLogger().error("[Malformed Database?] Inventory Provider does not extend PlayerInventory!");
                return;
            }
            try{
                Method m = invClass.getDeclaredMethod("createFromBytes", byte[].class);
                data.setInventory((PlayerInventory) m.invoke(null, new Object[]{bb.get(bb.getVarInt())}));
                entries.put(data.getUuid().toString(), data);
                try{
                    if(bb.getByte() != 0x7F){
                        server.getLogger().warning("[Malformed Database?] End of entry byte does not match!");
                    }
                }catch(BufferUnderflowException e){
                    server.getLogger().warning("[Malformed Database?] End of entry byte could not be read!");
                }
            }catch(NoSuchMethodException e){
                server.getLogger().error("Inventory Provider MUST have method \"static PlayerInventory createFromBytes(byte[] bytes)\"");
                server.getLogger().trace(e);
            }catch(InvocationTargetException e){
                server.getLogger().error(e.getClass().getName() + " while attempting to load PlayerInventory for " + data.getUuid().toString() + ": " + e.getMessage());
                server.getLogger().trace(e);
            }catch(IllegalAccessException e){
                server.getLogger().error(e.getClass().getName() + " while attempting to load PlayerInventory for " + data.getUuid());
                server.getLogger().error("(Check if method public?) " + e.getMessage());
                server.getLogger().trace(e);
            }
        }catch(ClassNotFoundException e){
            server.getLogger().error("[Malformed Database?] FAILED TO FIND INVENTORY PROVIDER \"" + inventoryImpl + "\" FOR " + data.getUuid().toString());
            server.getLogger().trace(e);
        }
    }

    @Override
    public void saveTo(File location) throws IOException{
        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.LITTLE_ENDIAN);
        bb.putByte(STORAGE_VERSION);
        bb.putVarInt(entries.size());
        for(PlayerData entry : entries.values()){
            putEntry(bb, entry);
        }
        bb.putByte((byte) 0x7E); //Signal end of database
        FileUtils.writeByteArrayToFile(location, bb.toArray());
    }

    private void putEntry(BinaryBuffer bb, PlayerData entry){
        BinaryBuffer id = BinaryBuffer.newInstance(16, ByteOrder.LITTLE_ENDIAN);
        id.putUUID(entry.getUuid());
        bb.put(id.toArray());

        bb.putFloat(entry.getPosition().getX());
        bb.putFloat(entry.getPosition().getY());
        bb.putFloat(entry.getPosition().getZ());
        bb.putFloat(entry.getPosition().getYaw());
        bb.putFloat(entry.getPosition().getPitch());
        bb.putVarString(entry.getPosition().getLevel().getName());

        bb.putVarInt(entry.getHealth());
        bb.putVarInt(entry.getGamemode());

        bb.putVarString(entry.getInventory().getClass().getName());
        byte[] data = entry.getInventory().saveToBytes();
        bb.putVarInt(data.length);
        bb.put(data);

        bb.putByte((byte) 0x7F); //Signal end of entry
    }

    @Override
    public void updateData(PlayerData data){
        entries.put(data.getUuid().toString(), data);
    }

    @Override
    public PlayerData getData(UUID uuid){
        if(entries.containsKey(uuid.toString())){
            return entries.get(uuid.toString());
        }
        return null;
    }

    @Override
    public void release(){

    }
}
