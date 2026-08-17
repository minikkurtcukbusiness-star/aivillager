/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.meinmod.build;

import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class BuildPlan {
    public final BlockPos anchor;
    private final Deque<Placement> queue = new ArrayDeque<Placement>();

    public BuildPlan(BlockPos anchor) {
        this.anchor = anchor.m_7949_();
    }

    public BuildPlan add(int dx, int dy, int dz, BlockState state) {
        this.queue.addLast(new Placement(this.anchor.m_7918_(dx, dy, dz), state));
        return this;
    }

    public boolean placeNext(ServerLevel level) {
        while (!this.queue.isEmpty()) {
            Placement p = this.queue.removeFirst();
            if (p.pos.m_123342_() <= level.m_141937_() || !level.m_46859_(p.pos)) continue;
            level.m_7731_(p.pos, p.state, 3);
            return true;
        }
        return false;
    }

    private static class Placement {
        final BlockPos pos;
        final BlockState state;

        Placement(BlockPos pos, BlockState state) {
            this.pos = pos.m_7949_();
            this.state = state;
        }
    }
}

