package net.redstonelamp.network.npd;

import java.util.List;

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
public class ProtocolNameInstruction extends Instruction{
    @Override
    public String[] getNames(){
        return new String[]{
                "Name",
                "ProtocolName",
        };
    }

    @Override
    public void run(NPDParser parser, List<String> args) throws NPDException{
        checkArgs(args, 1);
    }
}
