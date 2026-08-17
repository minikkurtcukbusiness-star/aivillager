/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.npc.Villager
 */
package com.meinmod.supermod.village;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.world.entity.npc.Villager;

public enum VillagerRole {
    BUILDER,
    MINER,
    FARMER;

    private static final Map<Integer, VillagerRole> roles;
    private static final Random rnd;

    public static void assignIfMissing(Villager v) {
        roles.computeIfAbsent(v.m_19879_(), id -> VillagerRole.values()[rnd.nextInt(VillagerRole.values().length)]);
    }

    public static VillagerRole getRole(Villager v) {
        return roles.getOrDefault(v.m_19879_(), BUILDER);
    }

    static {
        roles = new HashMap<Integer, VillagerRole>();
        rnd = new Random();
    }
}

