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

import lombok.Getter;
import net.redstonelamp.item.Items;

/**
 * Wool block implementation.
 *
 * @author RedstoneLamp Team
 */
public class Wool extends Block {
    public static final int ID = Items.WOOL;
    @Getter private Color type;

    public Wool(int id, short meta, int count) {
        super(id, meta, count);
        this.type = Color.WHITE;
    }

    public Wool(Color type, int count) {
        super(ID, (short) type.getMetaId(), count);
    }

    public static enum Color {
        WHITE(0),
        ORANGE(1),
        MAGENTA(2),
        LIGHT_BLUE(3),
        YELLOW(4),
        LIME(5),
        PINK(6),
        GRAY(7),
        LIGHT_GRAY(8),
        CYAN(9),
        PURPLE(10),
        BLUE(11),
        BROWN(12),
        GREEN(13),
        RED(14),
        BLACK(15);

        @Getter private int metaId;

        Color(int metaId) {
            this.metaId = metaId;
        }
    }
}
