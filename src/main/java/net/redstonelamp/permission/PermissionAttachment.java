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
/*
 * This file is part of RedstoneLamp
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
package net.redstonelamp.permission;

import lombok.Getter;
import net.redstonelamp.Player;
import net.redstonelamp.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a permission attachment hooked by a plugin
 *
 * @author RedstoneLamp team
 */
public class PermissionAttachment{

    private final HashMap<String, Boolean> permissions = new HashMap<>();
    @Getter private final Player player;
    @Getter private final Plugin plugin;

    public PermissionAttachment(Player player, Plugin plugin){
        this.player = player;
        this.plugin = plugin;
    }

    public void setPermission(String permission, boolean value){
        // setPermission(permission, value); // TODO Infinite recursion! Who added this? Fix this!
    }

    public void setPermission(Permission permission, boolean value){
        setPermission(permission.toString(), value);
    }

    public void unsetPermission(String permission){
        permissions.remove(permission);
    }

    public void unsetPermission(Permission permission){
        permissions.remove(permission.toString());
    }

    public Permission[] getPermissions(){
        ArrayList<Permission> perms = new ArrayList<>();
        return (Permission[]) permissions.keySet().toArray();
    }

    public boolean isPermissionRegistered(String permission){
        return permissions.containsKey(permission);
    }

    public boolean isPermissionRegistered(Permission permission){
        return isPermissionRegistered(permission.toString());
    }

    public boolean hasPermission(String permission){
        if(permissions.get(permission) == null){
            return false;
        }
        System.out.println(permissions.get(permission).booleanValue());
        return permissions.get(permission);
    }

    public boolean hasPermission(Permission permission){
        return hasPermission(permission.toString());
    }
}
