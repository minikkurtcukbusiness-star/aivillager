/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.Blocks
 */
package com.meinmod.build;

import com.meinmod.build.BuildPlan;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class Blueprints {
    public static BuildPlan townHall(BlockPos anchor) {
        int z;
        int x;
        BuildPlan p = new BuildPlan(anchor);
        for (x = 0; x < 7; ++x) {
            for (z = 0; z < 7; ++z) {
                p.add(x, 0, z, Blocks.f_50652_.m_49966_());
            }
        }
        for (int y = 1; y <= 3; ++y) {
            for (int i = 0; i < 7; ++i) {
                p.add(i, y, 0, Blocks.f_50705_.m_49966_());
                p.add(i, y, 6, Blocks.f_50705_.m_49966_());
                p.add(0, y, i, Blocks.f_50705_.m_49966_());
                p.add(6, y, i, Blocks.f_50705_.m_49966_());
            }
        }
        p.add(3, 2, 0, Blocks.f_50185_.m_49966_());
        p.add(3, 2, 6, Blocks.f_50185_.m_49966_());
        p.add(0, 2, 3, Blocks.f_50185_.m_49966_());
        p.add(6, 2, 3, Blocks.f_50185_.m_49966_());
        for (x = 0; x < 7; ++x) {
            for (z = 0; z < 7; ++z) {
                p.add(x, 4, z, Blocks.f_50405_.m_49966_());
            }
        }
        p.add(1, 2, 1, Blocks.f_50081_.m_49966_());
        p.add(5, 2, 1, Blocks.f_50081_.m_49966_());
        p.add(1, 2, 5, Blocks.f_50081_.m_49966_());
        p.add(5, 2, 5, Blocks.f_50081_.m_49966_());
        return p;
    }
}

