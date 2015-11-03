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
package net.redstonelamp.cmd.defaults;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.Command;
import net.redstonelamp.cmd.CommandExecutor;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.plugin.exception.PluginException;

import java.io.IOException;

public class ReloadCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(label.equalsIgnoreCase("reload")){
            try{
                // TODO: Reload entire server and not just plugins
                RedstoneLamp.SERVER.getPluginSystem().disablePlugins();
                RedstoneLamp.SERVER.getPluginSystem().enablePlugins();
            }catch(PluginException | IOException e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
