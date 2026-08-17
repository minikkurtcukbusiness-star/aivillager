/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.npc.Villager
 */
package com.meinmod.ai;

import com.meinmod.ai.NameGenerator;
import com.meinmod.ai.RoleGenerator;
import com.meinmod.ai.VillagerBrain;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.entity.npc.Villager;

public class BrainRegistry {
    private static final Map<UUID, VillagerBrain> BRAINS = new HashMap<UUID, VillagerBrain>();

    public static VillagerBrain get(Villager villager) {
        return BRAINS.computeIfAbsent(villager.m_20148_(), id -> new VillagerBrain((UUID)id, NameGenerator.randomName(id), RoleGenerator.randomRole(id)));
    }
}

