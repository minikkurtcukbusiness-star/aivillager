/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.meinmod.supermod.village.blueprint;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;

public class Blueprint {
    public final String id;
    public final int minTechOrdinal;
    public final List<Placement> blocks = new ArrayList<Placement>();

    public Blueprint(String id, int minTechOrdinal) {
        this.id = id;
        this.minTechOrdinal = minTechOrdinal;
    }

    public Blueprint add(int dx, int dy, int dz, BlockState state) {
        this.blocks.add(new Placement(dx, dy, dz, state));
        return this;
    }

    public record Placement(int dx, int dy, int dz, BlockState state) {
    }
}

