package com.minikkurtcuk.aivillager;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class VillageSimulation {
    public double speed = 1.0;
    public boolean paused = false;
    private final Map<ServerLevel, State> states = new WeakHashMap<>();

    private static final class State {
        BlockPos center;
        List<Villager> villagers = List.of();
        int scanTick, actionTick, buildIndex;
        int tech;
        double progress;
        final List<BlockPos> buildQueue = new ArrayList<>();
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || paused) return;
        MinecraftServer server = e.getServer();
        for (ServerLevel level : server.getAllLevels()) {
            State s = states.computeIfAbsent(level, x -> new State());
            s.scanTick++;
            s.actionTick++;
            if (s.center == null || s.scanTick >= 40) refresh(level, s);
            if (s.villagers.isEmpty()) continue;
            int interval = Math.max(2, (int)(10 / Math.max(.1, speed)));
            if (s.actionTick % interval != 0) continue;
            simulateBatch(level, s);
        }
    }

    private void refresh(ServerLevel level, State s) {
        s.scanTick = 0;
        AABB search = s.center == null ? new AABB(-3.0E7, -64, -3.0E7, 3.0E7, 320, 3.0E7) : new AABB(s.center).inflate(128, 64, 128);
        List<Villager> found = level.getEntitiesOfClass(Villager.class, search);
        if (s.center == null && !found.isEmpty()) s.center = found.get(0).blockPosition();
        if (s.center != null) {
            AABB local = new AABB(s.center).inflate(128, 64, 128);
            found = level.getEntitiesOfClass(Villager.class, local);
        }
        s.villagers = found;
        for (Villager v : found) assignRole(v, found.size());
        if (s.buildQueue.isEmpty() && s.center != null) makeBlueprint(s);
    }

    private void assignRole(Villager v, int count) {
        var tag = v.getPersistentData();
        if (tag.contains("IVRole")) return;
        String role;
        int n = Math.floorMod(v.getUUID().hashCode(), 100);
        if (n < 8) role = "LEADER"; else if (n < 28) role = "FARMER"; else if (n < 43) role = "MINER"; else if (n < 68) role = "BUILDER"; else role = "SCOUT";
        tag.putString("IVRole", role);
    }

    private void simulateBatch(ServerLevel level, State s) {
        int builders = 0, farmers = 0;
        int budget = Math.min(12, Math.max(3, s.villagers.size() / 5));
        int start = s.actionTick % Math.max(1, s.villagers.size());
        for (int i = 0; i < budget; i++) {
            Villager v = s.villagers.get((start + i) % s.villagers.size());
            String role = v.getPersistentData().getString("IVRole");
            switch (role) {
                case "BUILDER" -> builders++;
                case "FARMER" -> { farmers++; farmerAction(level, v); }
                case "MINER" -> minerAction(level, v);
                case "SCOUT" -> scoutAction(v, s.center);
                default -> { }
            }
        }
        s.progress += (0.45 + farmers * 0.08 + builders * 0.12) * speed;
        if (s.progress >= 100) { s.progress = 0; s.tech = Math.min(5, s.tech + 1); }
        buildBatch(level, s, Math.max(1, Math.min(5, builders + 1)));
    }

    private void farmerAction(ServerLevel level, Villager v) {
        BlockPos p = v.blockPosition();
        for (int dx = -3; dx <= 3; dx++) for (int dz = -3; dz <= 3; dz++) {
            BlockPos crop = p.offset(dx, 0, dz);
            if (level.getBlockState(crop).is(Blocks.FARMLAND) && level.isEmptyBlock(crop.above())) { level.setBlock(crop.above(), Blocks.WHEAT.defaultBlockState(), 3); return; }
        }
    }

    private void minerAction(ServerLevel level, Villager v) {
        BlockPos p = v.blockPosition();
        for (int y = 1; y <= 3; y++) {
            BlockPos q = p.below(y);
            BlockState st = level.getBlockState(q);
            if (st.is(Blocks.COAL_ORE) || st.is(Blocks.IRON_ORE) || st.is(Blocks.COPPER_ORE)) { level.destroyBlock(q, false); return; }
        }
    }

    private void scoutAction(Villager v, BlockPos center) {
        if (center == null || v.getRandom().nextInt(4) != 0) return;
        double a = v.getRandom().nextDouble() * Math.PI * 2;
        v.getNavigation().moveTo(center.getX() + Math.cos(a) * 35, center.getY(), center.getZ() + Math.sin(a) * 35, 0.8);
    }

    private void makeBlueprint(State s) {
        BlockPos c = s.center;
        int ring = 1 + s.buildIndex++ % 6;
        int ox = (ring % 2 == 0 ? ring : -ring) * 14;
        int oz = ((ring + 1) % 2 == 0 ? ring : -ring) * 14;
        for (int x = -3; x <= 3; x++) for (int z = -3; z <= 3; z++) { s.buildQueue.add(c.offset(ox+x, 0, oz+z)); s.buildQueue.add(c.offset(ox+x, 1, oz+z)); }
        for (int x = -4; x <= 4; x++) for (int y = 0; y <= 3; y++) { s.buildQueue.add(c.offset(ox+x, y, oz-4)); s.buildQueue.add(c.offset(ox+x, y, oz+4)); }
        for (int z = -3; z <= 3; z++) for (int y = 0; y <= 3; y++) { s.buildQueue.add(c.offset(ox-4, y, oz+z)); s.buildQueue.add(c.offset(ox+4, y, oz+z)); }
        for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) s.buildQueue.add(c.offset(ox+x, 4, oz+z));
    }

    private void buildBatch(ServerLevel level, State s, int amount) {
        if (s.buildQueue.isEmpty()) makeBlueprint(s);
        for (int i = 0; i < amount && !s.buildQueue.isEmpty(); i++) {
            BlockPos p = s.buildQueue.remove(0);
            if (level.isEmptyBlock(p)) level.setBlock(p, p.getY() == s.center.getY() ? Blocks.OAK_PLANKS.defaultBlockState() : Blocks.OAK_PLANKS.defaultBlockState(), 3);
        }
    }

    public String status() {
        int total = 0; StringBuilder out = new StringBuilder("§a[AI Village] ");
        for (State s : states.values()) total += s.villagers.size();
        return out.append("Villagers=").append(total).append(" Speed=x").append(speed).append(" Paused=").append(paused).toString();
    }
}
