package com.minikkurtcuk.aivillager;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

/**
 * Low-overhead autonomous village simulation.
 *
 * Design goals for low-end machines:
 * - never scan the whole world
 * - never process every villager every tick
 * - cap entity work and block work per server tick
 * - only touch chunks around an active player
 */
public final class VillageSimulationOptimized {
    public double speed = 1.0;
    public boolean paused;

    private static final int RADIUS = 128;
    private static final int SCAN_INTERVAL = 100;
    private static final int MAX_VILLAGERS = 48;
    private static final int MAX_ACTIONS_PER_STEP = 6;
    private static final int MAX_BLOCKS_PER_TICK = 3;
    private static final Map<ServerLevel, State> STATES = new WeakHashMap<>();

    private static final class State {
        BlockPos center;
        List<Villager> villagers = List.of();
        int tick;
        int cursor;
        int building;
        int tech;
        double progress;
        final ArrayDeque<BlockPos> queue = new ArrayDeque<>();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || paused) return;

        MinecraftServer server = event.getServer();
        for (ServerLevel level : server.getAllLevels()) {
            State state = STATES.computeIfAbsent(level, ignored -> new State());
            state.tick++;

            // No player = no active simulation. This prevents background worlds from consuming CPU.
            if (level.players().isEmpty()) continue;

            if (state.center == null || state.tick % SCAN_INTERVAL == 0) refresh(level, state);
            if (state.villagers.isEmpty()) continue;

            int interval = Math.max(5, (int) Math.round(15.0 / Math.max(0.25, speed)));
            if (state.tick % interval != 0) continue;

            int budget = Math.min(MAX_ACTIONS_PER_STEP,
                    Math.max(1, (int) Math.ceil(state.villagers.size() / 10.0)));

            for (int i = 0; i < budget; i++) {
                Villager villager = state.villagers.get(state.cursor++ % state.villagers.size());
                if (!villager.isAlive() || villager.isRemoved()) continue;
                roleAction(level, state, villager);
            }

            state.progress += (0.25 + budget * 0.05) * speed;
            if (state.progress >= 100.0) {
                state.progress -= 100.0;
                state.tech = Math.min(5, state.tech + 1);
            }

            build(level, state, Math.min(MAX_BLOCKS_PER_TICK, 1 + state.tech / 3));
        }
    }

    private void refresh(ServerLevel level, State state) {
        ServerPlayer player = level.players().get(0);
        if (state.center == null) state.center = player.blockPosition();

        AABB box = new AABB(state.center).inflate(RADIUS, 48, RADIUS);
        List<Villager> found = level.getEntitiesOfClass(Villager.class, box,
                villager -> villager.isAlive() && !villager.isRemoved());

        if (found.size() > MAX_VILLAGERS) {
            found = new ArrayList<>(found.subList(0, MAX_VILLAGERS));
        }

        state.villagers = found;
        for (Villager villager : found) assignRole(villager);
        if (state.queue.isEmpty()) blueprint(state);
    }

    private void assignRole(Villager villager) {
        var data = villager.getPersistentData();
        if (data.contains("IVRole")) return;

        int n = Math.floorMod(villager.getUUID().hashCode(), 100);
        data.putString("IVRole",
                n < 8 ? "LEADER" :
                n < 35 ? "FARMER" :
                n < 55 ? "MINER" :
                n < 82 ? "BUILDER" : "SCOUT");
    }

    private void roleAction(ServerLevel level, State state, Villager villager) {
        String role = villager.getPersistentData().getString("IVRole");
        switch (role) {
            case "FARMER" -> farm(level, villager);
            case "MINER" -> mine(level, villager);
            case "BUILDER" -> state.building++;
            case "SCOUT" -> scout(villager, state.center);
            default -> { }
        }
    }

    private void farm(ServerLevel level, Villager villager) {
        BlockPos p = villager.blockPosition();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                BlockPos soil = p.offset(dx, 0, dz);
                if (!level.getBlockState(soil).is(Blocks.FARMLAND)) continue;

                BlockPos crop = soil.above();
                BlockState current = level.getBlockState(crop);
                if (current.is(Blocks.WHEAT)) {
                    var age = current.getValue(net.minecraft.world.level.block.CropBlock.AGE);
                    if (age >= 7) {
                        level.destroyBlock(crop, true);
                        level.setBlock(crop, Blocks.WHEAT.defaultBlockState(), 3);
                    }
                    return;
                }

                if (level.isEmptyBlock(crop)) {
                    level.setBlock(crop, Blocks.WHEAT.defaultBlockState(), 3);
                    return;
                }
            }
        }
    }

    private void mine(ServerLevel level, Villager villager) {
        BlockPos p = villager.blockPosition();
        for (int y = 1; y <= 2; y++) {
            BlockPos q = p.below(y);
            BlockState block = level.getBlockState(q);
            if (block.is(Blocks.COAL_ORE) || block.is(Blocks.IRON_ORE) || block.is(Blocks.COPPER_ORE)) {
                level.destroyBlock(q, true);
                return;
            }
        }
    }

    private void scout(Villager villager, BlockPos center) {
        if (center == null || villager.getRandom().nextInt(8) != 0) return;
        double angle = villager.getRandom().nextDouble() * Math.PI * 2.0;
        double x = center.getX() + Math.cos(angle) * 24.0;
        double z = center.getZ() + Math.sin(angle) * 24.0;
        villager.getNavigation().moveTo(x, center.getY(), z, 0.7);
    }

    private void blueprint(State state) {
        BlockPos c = state.center;
        int ring = 1 + state.building % 6;
        int ox = (ring % 2 == 0 ? ring : -ring) * 12;
        int oz = ((ring + 1) % 2 == 0 ? ring : -ring) * 12;

        // Small staged houses. The queue is deliberately tiny and incremental.
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) state.queue.add(c.offset(ox + x, 0, oz + z));
        }
        for (int x = -4; x <= 4; x++) {
            for (int y = 0; y <= 3; y++) {
                state.queue.add(c.offset(ox + x, y, oz - 4));
                state.queue.add(c.offset(ox + x, y, oz + 4));
            }
        }
        for (int z = -3; z <= 3; z++) {
            for (int y = 0; y <= 3; y++) {
                state.queue.add(c.offset(ox - 4, y, oz + z));
                state.queue.add(c.offset(ox + 4, y, oz + z));
            }
        }
    }

    private void build(ServerLevel level, State state, int amount) {
        if (state.queue.isEmpty()) blueprint(state);

        for (int i = 0; i < amount && !state.queue.isEmpty(); i++) {
            BlockPos pos = state.queue.removeFirst();
            if (level.isLoaded(pos) && level.isEmptyBlock(pos)) {
                level.setBlock(pos, Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
        }
    }

    public String status() {
        int total = 0;
        for (State state : STATES.values()) total += state.villagers.size();
        return "§a[AI Village] Villagers=" + total + " Speed=x" + speed +
                " Tech=" + maxTech() + " Mode=" + (paused ? "PAUSED" : "RUNNING");
    }

    private int maxTech() {
        int result = 0;
        for (State state : STATES.values()) result = Math.max(result, state.tech);
        return result;
    }
}
