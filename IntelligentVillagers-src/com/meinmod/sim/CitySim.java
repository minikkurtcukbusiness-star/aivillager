/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 */
package com.meinmod.sim;

import com.meinmod.build.BuildPlan;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;

public class CitySim {
    public final String name;
    public BlockPos core;
    public UUID leader;
    public int techLevel = 1;
    public BuildPlan activeBuild;
    public final Map<String, Integer> resources = new HashMap<String, Integer>();

    public CitySim(String name, BlockPos core) {
        this.name = name;
        this.core = core.m_7949_();
    }

    public void addRes(String key, int amount) {
        this.resources.put(key, this.resources.getOrDefault(key, 0) + amount);
    }
}

