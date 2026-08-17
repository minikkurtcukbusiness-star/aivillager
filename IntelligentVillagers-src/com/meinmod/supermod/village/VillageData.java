/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.phys.AABB
 */
package com.meinmod.supermod.village;

import com.meinmod.supermod.SuperMod;
import com.meinmod.supermod.village.TechLevel;
import com.meinmod.supermod.village.VillagerRole;
import com.meinmod.supermod.village.blueprint.Blueprint;
import com.meinmod.supermod.village.blueprint.BlueprintRegistry;
import com.meinmod.supermod.village.blueprint.BuildTask;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;

public class VillageData {
    private static final AABB WORLD_BOX = new AABB(-3.0E7, -64.0, -3.0E7, 3.0E7, 320.0, 3.0E7);
    private TechLevel techLevel = TechLevel.PRIMITIVE;
    private double techProgress = 0.0;
    private BlockPos center = null;
    private final Queue<BuildTask> queue = new ArrayDeque<BuildTask>();
    private BuildTask current = null;
    private int buildTick = 0;
    private int buildSlot = 0;

    public String getCurrentTaskName() {
        return this.current != null ? this.current.name() : null;
    }

    public int getCurrentTaskProgressPct() {
        return this.current != null ? this.current.progressPct() : 0;
    }

    public TechLevel getTechLevel() {
        return this.techLevel;
    }

    public void tick(ServerLevel level) {
        List villagers = level.m_45976_(Villager.class, WORLD_BOX);
        if (villagers.isEmpty()) {
            return;
        }
        if (this.center == null) {
            this.center = ((Villager)villagers.get(0)).m_20183_();
            System.out.println("[SuperMod] Dorfzentrum gesetzt: " + String.valueOf(this.center));
        }
        int builders = 0;
        int miners = 0;
        int farmers = 0;
        for (Villager v : villagers) {
            VillagerRole.assignIfMissing(v);
            VillagerRole role = VillagerRole.getRole(v);
            if (role == VillagerRole.BUILDER) {
                ++builders;
            }
            if (role == VillagerRole.MINER) {
                ++miners;
            }
            if (role != VillagerRole.FARMER) continue;
            ++farmers;
        }
        double research = (0.6 + (double)farmers * 0.35) * SuperMod.DEVELOPMENT_SPEED;
        this.techProgress += research;
        if (this.techProgress >= 100.0) {
            this.techProgress = 0.0;
            this.techLevel = this.techLevel.next();
            System.out.println("[SuperMod] Tech-Level UP: " + String.valueOf((Object)this.techLevel));
        }
        if (this.queue.size() < 2 && this.current == null) {
            this.planSomeProjects(level);
        } else if (this.queue.size() < 2 && this.buildTick % 200 == 0) {
            this.planSomeProjects(level);
        }
        if (this.current == null) {
            this.current = this.queue.poll();
        }
        if (this.current == null) {
            return;
        }
        int blocksPerStep = (int)Math.max(1.0, Math.floor((1.0 + (double)builders * 0.6 + (double)miners * 0.35) * SuperMod.DEVELOPMENT_SPEED));
        ++this.buildTick;
        int interval = (int)Math.max(2.0, Math.floor(12.0 / Math.max(1.0, SuperMod.DEVELOPMENT_SPEED)));
        if (this.buildTick % interval != 0) {
            return;
        }
        for (int i = 0; i < blocksPerStep && !this.current.done(); ++i) {
            Blueprint.Placement placement = this.current.blueprint.blocks.get(this.current.index);
            BlockPos pos = this.current.origin.m_7918_(placement.dx(), placement.dy(), placement.dz());
            if (level.m_8055_(pos).m_60795_()) {
                level.m_7731_(pos, placement.state(), 3);
            }
            ++this.current.index;
        }
        if (this.current.done()) {
            System.out.println("[SuperMod] Projekt abgeschlossen: " + this.current.name());
            this.current = null;
        }
    }

    private void planSomeProjects(ServerLevel level) {
        int techOrd = this.techLevel.ordinal();
        for (int p = 0; p < 2; ++p) {
            Blueprint blueprint = BlueprintRegistry.pickForTech(techOrd);
            BlockPos origin = this.nextBuildOrigin();
            this.queue.add(new BuildTask(blueprint, origin));
        }
    }

    private BlockPos nextBuildOrigin() {
        int slotSize = 28;
        int ring = 1 + this.buildSlot / 8;
        int idx = this.buildSlot % 8;
        int dx = 0;
        int dz = 0;
        switch (idx) {
            case 0: {
                dx = 0;
                dz = -ring;
                break;
            }
            case 1: {
                dx = ring;
                dz = -ring;
                break;
            }
            case 2: {
                dx = ring;
                dz = 0;
                break;
            }
            case 3: {
                dx = ring;
                dz = ring;
                break;
            }
            case 4: {
                dx = 0;
                dz = ring;
                break;
            }
            case 5: {
                dx = -ring;
                dz = ring;
                break;
            }
            case 6: {
                dx = -ring;
                dz = 0;
                break;
            }
            case 7: {
                dx = -ring;
                dz = -ring;
            }
        }
        ++this.buildSlot;
        return this.center.m_7918_(dx * slotSize, 0, dz * slotSize);
    }
}

