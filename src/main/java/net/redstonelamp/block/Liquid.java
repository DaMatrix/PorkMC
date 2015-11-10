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
package net.redstonelamp.block;

import net.redstonelamp.level.position.BlockPosition;

/**
 * Abstract class for liquids (water & lava).
 *
 * @author RedstoneLamp Team
 */
public abstract class Liquid extends Transparent{

    public Liquid(int id, short meta, int count){
        super(id, meta, count);
    }

    @Override
    public void update(BlockPosition position) {
        //Super Basic Liquid Spread Technique. Now in stores near you!
        BlockPosition[] adj = new BlockPosition[5]; //It is five because we don't need the block above us.

        //Get the surrounding block's positions.
        adj[0] = new BlockPosition(position.getX() - 1, position.getY(), position.getZ(), position.getLevel());
        adj[1] = new BlockPosition(position.getX() + 1, position.getY(), position.getZ(), position.getLevel());
        adj[2] = new BlockPosition(position.getX(), position.getY() - 1, position.getZ(), position.getLevel());
        adj[3] = new BlockPosition(position.getX(), position.getY(), position.getZ() - 1, position.getLevel());
        adj[4] = new BlockPosition(position.getX(), position.getY(), position.getZ() + 1, position.getLevel());

        for(int i = 0; i < 5; i++) {
            //Check if the adjecent block is empty (air (0))
            if(position.getLevel().getBlock(adj[i]).getId() == 0) {
                //Block is air, fill with water.
                position.getLevel().setBlockNoUpdate(adj[i], position.getLevel().getBlock(position));
            }
            //Block occupied, do nothing.
        }
    }               
}
