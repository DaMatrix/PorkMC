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

import net.redstonelamp.network.npd.NPDException;
import net.redstonelamp.network.npd.NPDParser;

import java.util.List;

public abstract class Instruction{
    public abstract String[] getNames();
    public abstract void run(NPDParser parser, List<String> args) throws NPDException;

    protected void checkArgs(List<String> args, int min) throws NPDException{
        if(args.size() < min){
            throw new NPDException.MalformedParametersException(min, args.size());
        }
    }

    protected int parseInt(String num) throws NPDException{
        if(num.equals("0")){
            return 0;
        }
        int radix = 10;
        if(num.charAt(0) == '0'){
            if(num.charAt(1) == 'x'){
                radix = 16;
            }else if(num.charAt(1) == 'b'){
                radix = 2;
            }else{
                radix = 8;
            }
        }else{
            int index = num.indexOf('_');
            if(index != -1){
                String radixStr = num.substring(index + 1);
                num = num.substring(0, index);
                try{
                    radix = Integer.parseInt(radixStr);
                }catch(NumberFormatException e){
                    throw new NPDException.NumberFormatException(radixStr);
                }
            }
        }
        try{
            return Integer.parseInt(num, radix);
        }catch(NumberFormatException e){
            throw new NPDException.NumberFormatException(num);
        }
    }
}
