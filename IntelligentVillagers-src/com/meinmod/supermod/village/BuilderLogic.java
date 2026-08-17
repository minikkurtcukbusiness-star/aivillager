/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.level.block.Blocks
 */
package com.meinmod.supermod.village;

import com.meinmod.supermod.village.TechLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Blocks;

public class BuilderLogic {
    public static void build(Villager v, ServerLevel level, TechLevel tech) {
        BlockPos base = v.m_20183_().m_7918_(2, 0, 2);
        if (!level.m_8055_(base).m_60795_()) {
            return;
        }
        level.m_7731_(base, Blocks.f_50652_.m_49966_(), 3);
        level.m_7731_(base.m_7494_(), Blocks.f_50705_.m_49966_(), 3);
        v.m_20340_(true);
        v.m_6593_((Component)Component.m_237113_((String)("\u00a76Baumeister (" + String.valueOf((Object)tech) + ")")));
    }
}

