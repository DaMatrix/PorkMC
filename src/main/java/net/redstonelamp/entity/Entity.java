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
package net.redstonelamp.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.redstonelamp.Player;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.metadata.MetadataDictionary;

/**
 * Represents an Entity.
 *
 * @author RedstoneLamp Team
 */
public abstract class Entity{
    @Getter @Setter(AccessLevel.PROTECTED) private int entityID;
    @Getter @Setter private int maxHealth;
    @Getter @Setter private int health;
    @Getter @Setter private Position position;
    @Getter @Setter private String name;
    @Getter @Setter(AccessLevel.PROTECTED) private MetadataDictionary metadata;
    //private EntityMetadata metadata;
    @Getter private boolean initialized = false;
    private EntityMotion motion;
    @Getter @Setter(AccessLevel.PROTECTED) private EntityManager entityManager;

    public Entity(EntityManager entityManager, Position position){
        this.position = position;
        this.entityManager = entityManager;
    }

    protected void initEntity(){
        initialized = true;
        setEntityID(entityManager.getNextEntityID());
        if(position != null && position.getLevel() != null){
            position.getLevel().getEntityManager().addEntity(this);
        }
    }

    protected void destroyEntity(){
        initialized = false;
        if(position != null && position.getLevel() != null){
            position.getLevel().getEntityManager().removeEntity(this);
        }
    }

    /**
     * INTERNAL METHOD!
     * <br>
     * Called when an entity is to be ticked, or checked. This method is overrided in subclasses to perform
     * actions such as movement for mob AI.
     *
     * @param tick The current tick.
     */
    public void doTick(long tick){

    }

    /**
     * Spawns this entity to a player. This method may be overridden in child classes.
     *
     * @param player The Player this entity will spawn to
     */
    public void spawnTo(Player player){
        //TODO: Send AddEntityResponse or Request?
    }

    /**
     * De-spawns this entity (removes) from a player. This method may be overriden in child classes.
     *
     * @param player The Player this entity will spawn to
     */
    public void despawnFrom(Player player){
        //TODO: Send RemoveEntityResponse or Request?
    }
}
