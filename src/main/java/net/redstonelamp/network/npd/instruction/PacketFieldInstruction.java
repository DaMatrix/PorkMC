package net.redstonelamp.network.npd.instruction;

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

import net.redstonelamp.network.npd.*;

import java.nio.ByteOrder;
import java.util.List;

public class PacketFieldInstruction extends Instruction{
    @Override
    public String[] getNames(){
        return new String[]{
                "PacketField"
        };
    }
    @Override
    public void run(NPDParser parser, List<String> args) throws NPDException{
        checkArgs(args, 2);
        if(args.get(0).equalsIgnoreCase("skip")){
            PacketField field = new PacketField(PacketFieldType.builder()
                    .baseType(PacketFieldTypeBase.SKIP)
                    .skipSize(parseInt(args.get(1)))
                    .build(), "");
            parser.packet().getFields().add(field);
        }else{
            String typeName = args.get(0);
            boolean isArray = false;
            ByteOrder endianness = ByteOrder.LITTLE_ENDIAN;
            if(typeName.endsWith("[]")){
                isArray = true;
                typeName = typeName.substring(0, typeName.length() - 2);
            }
            if(typeName.charAt(0) == '~'){
                endianness = ByteOrder.BIG_ENDIAN;
                typeName = typeName.substring(1);
            }
            PacketFieldTypeBase base = PacketFieldTypeBase.match(typeName);
            if(base == null){
                throw new NPDException("Unknown packet field type " + args.get(0));
            }
            PacketFieldType type = PacketFieldType.builder()
                    .baseType(base)
                    .array(isArray)
                    .endianness(endianness)
                    .build();
            PacketField field = new PacketField(type, args.get(1));
            parser.packet().getFields().add(field);
        }
    }
}
