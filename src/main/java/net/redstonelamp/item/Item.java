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
package net.redstonelamp.item;

import lombok.Getter;
import lombok.Setter;
import net.redstonelamp.block.*;
import org.spout.nbt.CompoundTag;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an Item.
 *
 * @author RedstoneLamp Team
 */
public class Item implements Items{
    private static final Map<Integer, Class<? extends Item>> items = new HashMap<>();
    @Getter private static final List<Item> creativeItems = new ArrayList<>();

    @Getter private final int id;
    @Getter private final short meta;
    @Getter private final int count;
    @Getter @Setter private CompoundTag compoundTag;

    public Item(int id, short meta, int count){
        this.id = id;
        this.meta = meta;
        this.count = count;
    }

    public static void init(){
        creativeItems.clear();
        items.clear();
        initItems();
        initCreativeItems();
    }

    private static void initItems(){
        items.put(BED_BLOCK, BedBlock.class);
        items.put(BEDROCK, Bedrock.class);
        items.put(BOOKSHELF, Bookshelf.class);
        items.put(BRICKS, Bricks.class);
        items.put(BROWN_MUSHROOM, BrownMushroom.class);
        items.put(CLAY_BLOCK, ClayBlock.class);
        items.put(COAL_ORE, CoalOre.class);
        items.put(COBBLESTONE, Cobblestone.class);
        items.put(COBWEB, Cobweb.class);
        items.put(DANDELION, Dandelion.class);
        items.put(DEAD_BUSH, DeadBush.class);
        items.put(DIAMOND_BLOCK, DiamondBlock.class);
        items.put(DIAMOND_ORE, DiamondOre.class);
        items.put(DIRT, Dirt.class);
        items.put(FIRE, Fire.class);
        items.put(GLASS, Glass.class);
        items.put(GLOWING_REDSTONE_ORE, GlowingRedstoneOre.class);
        items.put(GOLD_BLOCK, GoldBlock.class);
        items.put(GOLD_ORE, GoldOre.class);
        items.put(GRASS, Grass.class);
        items.put(GRAVEL, Gravel.class);
        items.put(ICE, Ice.class);
        items.put(IRON_BLOCK, IronBlock.class);
        items.put(IRON_ORE, IronOre.class);
        items.put(LAPIS_BLOCK, LapisBlock.class);
        items.put(LAPIS_ORE, LapisOre.class);
        items.put(LAVA, Lava.class);
        items.put(LEAVES, Leaves.class);
        items.put(LOG, Log.class);
        items.put(MOSSY_STONE, MossyStone.class);
        items.put(MYCELIUM, Mycelium.class);
        items.put(OBSIDIAN, Obsidian.class);
        items.put(QUARTZ_BLOCK, Quartz.class);
        items.put(RED_MUSHROOM, RedMushroom.class);
        items.put(REDSTONE_ORE, RedstoneOre.class);
        items.put(ROSE, Rose.class);
        items.put(SAND, Sand.class);
        items.put(SANDSTONE, Sandstone.class);
        items.put(SAPLING, Sapling.class);
        items.put(SNOW_BLOCK, SnowBlock.class);
        items.put(MONSTER_SPAWNER, Spawner.class);
        items.put(SPONGE, Sponge.class);
        items.put(STILL_LAVA, StillLava.class);
        items.put(STILL_WATER, StillWater.class);
        items.put(STONE, Stone.class);
        items.put(STONE_BRICKS, StoneBricks.class);
        items.put(TALL_GRASS, TallGrass.class);
        items.put(TNT, TNT.class);
        items.put(TORCH, Torch.class);
        items.put(WATER, Water.class);
        items.put(WOODEN_PLANKS, WoodPlanks.class);
        items.put(WOOL, Wool.class);
    }

    private static void initCreativeItems(){
        addCreativeItem(get(BED_BLOCK, (short) 0));
        addCreativeItem(get(BEDROCK, (short) 0));
        addCreativeItem(get(BOOKSHELF, (short) 0));
        addCreativeItem(get(BRICKS, (short) 0));
        addCreativeItem(get(BROWN_MUSHROOM, (short) 0));
        addCreativeItem(get(CLAY_BLOCK, (short) 0));
        addCreativeItem(get(COAL_ORE, (short) 0));
        addCreativeItem(get(COBBLESTONE, (short) 0));
        addCreativeItem(get(COBWEB, (short) 0));
        addCreativeItem(get(DANDELION, (short) 0));
        addCreativeItem(get(DEAD_BUSH, (short) 0));
        addCreativeItem(get(DIAMOND_BLOCK, (short) 0));
        addCreativeItem(get(DIAMOND_ORE, (short) 0));
        addCreativeItem(get(DIRT, (short) 0));
        //addCreativeItem(get(FIRE, (short) 0));
        addCreativeItem(get(GLASS, (short) 0));
        addCreativeItem(get(GLOWING_REDSTONE_ORE, (short) 0));
        addCreativeItem(get(GOLD_BLOCK, (short) 0));
        addCreativeItem(get(GOLD_ORE, (short) 0));
        addCreativeItem(get(GRASS, (short) 0));
        addCreativeItem(get(GRAVEL, (short) 0));
        addCreativeItem(get(ICE, (short) 0));
        addCreativeItem(get(IRON_BLOCK, (short) 0));
        addCreativeItem(get(IRON_ORE, (short) 0));
        addCreativeItem(get(LAPIS_BLOCK, (short) 0));
        addCreativeItem(get(LAPIS_ORE, (short) 0));
        //addCreativeItem(get(LAVA, (short) 0));
        addCreativeItem(get(LEAVES, (short) 0));
        addCreativeItem(get(LOG, (short) 0));
        addCreativeItem(get(MOSSY_STONE, (short) 0));
        addCreativeItem(get(MYCELIUM, (short) 0));
        addCreativeItem(get(OBSIDIAN, (short) 0));
        addCreativeItem(get(QUARTZ_BLOCK, (short) 0));
        addCreativeItem(get(RED_MUSHROOM, (short) 0));
        addCreativeItem(get(REDSTONE_ORE, (short) 0));
        addCreativeItem(get(ROSE, (short) 0));
        addCreativeItem(get(SAND, (short) 0));
        addCreativeItem(get(SANDSTONE, (short) 0));
        addCreativeItem(get(SAPLING, (short) 0));
        addCreativeItem(get(SNOW_BLOCK, (short) 0));
        addCreativeItem(get(MONSTER_SPAWNER, (short) 0));
        addCreativeItem(get(SPONGE, (short) 0));
        //addCreativeItem(get(STILL_LAVA, (short) 0));
        //addCreativeItem(get(STILL_WATER, (short) 0));
        addCreativeItem(get(STONE, (short) 0));
        addCreativeItem(get(STONE_BRICKS, (short) 0));
        addCreativeItem(get(TALL_GRASS, (short) 0));
        addCreativeItem(get(TNT, (short) 0));
        addCreativeItem(get(TORCH, (short) 0));
        //addCreativeItem(get(WATER, (short) 0));
        addCreativeItem(get(WOODEN_PLANKS, (short) WoodPlanks.PlankType.OAK.getMetaId()));
        addCreativeItem(get(WOODEN_PLANKS, (short) WoodPlanks.PlankType.SPRUCE.getMetaId()));
        addCreativeItem(get(WOODEN_PLANKS, (short) WoodPlanks.PlankType.ACACIA.getMetaId()));
        addCreativeItem(get(WOODEN_PLANKS, (short) WoodPlanks.PlankType.JUNGLE.getMetaId()));
        addCreativeItem(get(WOODEN_PLANKS, (short) WoodPlanks.PlankType.BIRCH.getMetaId()));
        addCreativeItem(get(WOODEN_PLANKS, (short) WoodPlanks.PlankType.DARK_OAK.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.WHITE.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.ORANGE.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.MAGENTA.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.LIGHT_BLUE.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.YELLOW.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.LIME.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.PINK.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.GRAY.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.LIGHT_GRAY.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.CYAN.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.PURPLE.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.BLUE.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.BROWN.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.GREEN.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.RED.getMetaId()));
        addCreativeItem(get(WOOL, (short) Wool.Color.BLACK.getMetaId()));
    }

    public static synchronized void addCreativeItem(Item item){
        creativeItems.add(get(item.getId(), item.getMeta()));
    }

    public static Item get(int id){
        return get(id, (short) 0, 1);
    }

    public static Item get(int id, short meta){
        return get(id, meta, 1);
    }

    public static Item get(int id, short meta, int count){
        if(items.containsKey(id)){
            try{
                Constructor c = items.get(id).getConstructor(int.class, short.class, int.class);
                return (Item) c.newInstance(id, meta, count);
            }catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
                e.printStackTrace();
                return null;
            }
        }else if(id < 256){
            return new Block(id, meta, count);
        }else{
            return new Item(id, meta, count);
        }
    }
}
