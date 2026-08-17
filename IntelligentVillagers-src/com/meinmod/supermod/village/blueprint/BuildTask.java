/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 */
package com.meinmod.supermod.village.blueprint;

import com.meinmod.supermod.village.blueprint.Blueprint;
import net.minecraft.core.BlockPos;

public class BuildTask {
    public final Blueprint blueprint;
    public final BlockPos origin;
    public int index = 0;

    public BuildTask(Blueprint blueprint, BlockPos origin) {
        this.blueprint = blueprint;
        this.origin = origin;
    }

    public int total() {
        return this.blueprint.blocks.size();
    }

    public boolean done() {
        return this.index >= this.total();
    }

    public String name() {
        return this.blueprint.id;
    }

    public int progressPct() {
        if (this.total() <= 0) {
            return 100;
        }
        return (int)Math.floor((double)this.index * 100.0 / (double)this.total());
    }
}

