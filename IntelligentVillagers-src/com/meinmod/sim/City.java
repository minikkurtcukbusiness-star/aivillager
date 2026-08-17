/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 */
package com.meinmod.sim;

import com.meinmod.build.BuildPlan;
import com.meinmod.sim.CityNames;
import java.util.UUID;
import net.minecraft.core.BlockPos;

public class City {
    public final UUID id;
    public final String name;
    public final BlockPos core;
    public BuildPlan activeBuild;

    private City(UUID id, String name, BlockPos core) {
        this.id = id;
        this.name = name;
        this.core = core.m_7949_();
    }

    public static City createAt(BlockPos core) {
        UUID id = UUID.randomUUID();
        String name = CityNames.randomCityName(id);
        return new City(id, name, core);
    }
}

