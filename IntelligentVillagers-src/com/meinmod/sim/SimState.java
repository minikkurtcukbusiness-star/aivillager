/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.CropBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.meinmod.sim;

import com.meinmod.sim.City;
import com.meinmod.sim.NameGen;
import com.meinmod.sim.ResourceType;
import com.meinmod.sim.WorkerRole;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class SimState {
    public static long serverTick = 0L;
    private static final Random RNG = new Random();
    private static final Map<UUID, WorkerRole> roles = new HashMap<UUID, WorkerRole>();
    private static final Map<UUID, UUID> villagerCity = new HashMap<UUID, UUID>();
    private static final Map<UUID, City> cities = new HashMap<UUID, City>();
    private static final Map<UUID, EnumMap<ResourceType, Integer>> cityResources = new HashMap<UUID, EnumMap<ResourceType, Integer>>();
    private static final Map<UUID, Integer> villagerCooldown = new HashMap<UUID, Integer>();

    public static void tick(ServerLevel level) {
        ++serverTick;
    }

    public static void ensureVillagerRegistered(ServerLevel level, Villager v) {
        UUID id = v.m_20148_();
        roles.putIfAbsent(id, WorkerRole.random(RNG));
        villagerCooldown.putIfAbsent(id, 0);
        if (cities.isEmpty()) {
            City c = City.createAt(v.m_20183_());
            cities.put(c.id, c);
            cityResources.put(c.id, new EnumMap(ResourceType.class));
            SimState.sayToNearestPlayer(level, c.core, (Component)Component.m_237113_((String)("\ud83c\udfd9 Neue Stadt gegr\u00fcndet: " + c.name)));
        }
        villagerCity.putIfAbsent(id, SimState.cities.values().iterator().next().id);
        City city = SimState.getCityOf(id);
        WorkerRole r = SimState.getRoleOf(id);
        v.m_6593_((Component)Component.m_237113_((String)(r.icon + " " + NameGen.nameFor(id) + " \u00a77[" + city.name + "]")));
        v.m_20340_(true);
    }

    public static WorkerRole getRoleOf(UUID villagerId) {
        return roles.getOrDefault(villagerId, WorkerRole.BUILDER);
    }

    public static City getCityOf(UUID villagerId) {
        UUID cid = villagerCity.get(villagerId);
        return cities.get(cid);
    }

    public static void tryJoinNearbyCity(ServerLevel level, Villager v) {
    }

    public static BlockPos findMineTarget(ServerLevel level, BlockPos origin) {
        int r = 6;
        for (BlockPos p : BlockPos.m_121940_((BlockPos)origin.m_7918_(-r, -2, -r), (BlockPos)origin.m_7918_(r, 2, r))) {
            BlockState st = level.m_8055_(p);
            if (!st.m_60713_(Blocks.f_49997_) && !st.m_60713_(Blocks.f_49996_) && !st.m_60713_(Blocks.f_49995_) && !st.m_60713_(Blocks.f_50089_) && !st.m_60713_(Blocks.f_50069_) && !st.m_60713_(Blocks.f_152550_)) continue;
            return p.m_7949_();
        }
        return null;
    }

    public static boolean chance(double p) {
        return RNG.nextDouble() < p;
    }

    public static void lookAt(Villager v, BlockPos pos) {
        v.m_21563_().m_24946_((double)pos.m_123341_() + 0.5, (double)pos.m_123342_() + 0.5, (double)pos.m_123343_() + 0.5);
    }

    public static void swing(Villager v) {
        v.m_6674_(InteractionHand.MAIN_HAND);
    }

    public static ResourceType resourceFromBlock(BlockState st) {
        Block b = st.m_60734_();
        if (b == Blocks.f_49997_) {
            return ResourceType.COAL;
        }
        if (b == Blocks.f_49996_) {
            return ResourceType.IRON;
        }
        if (b == Blocks.f_49995_) {
            return ResourceType.GOLD;
        }
        if (b == Blocks.f_50089_) {
            return ResourceType.DIAMOND;
        }
        return ResourceType.STONE;
    }

    public static void addResource(UUID cityId, ResourceType type) {
        cityResources.putIfAbsent(cityId, new EnumMap(ResourceType.class));
        EnumMap<ResourceType, Integer> m = cityResources.get(cityId);
        m.put(type, m.getOrDefault((Object)type, 0) + 1);
    }

    public static void buildStep(ServerLevel level, Villager v, City city) {
        if (city.activeBuild == null) {
            return;
        }
        int cd = villagerCooldown.getOrDefault(v.m_20148_(), 0);
        if (cd > 0) {
            villagerCooldown.put(v.m_20148_(), cd - 1);
            return;
        }
        PathNavigation nav = v.m_21573_();
        nav.m_26519_((double)city.activeBuild.anchor.m_123341_(), (double)city.activeBuild.anchor.m_123342_(), (double)city.activeBuild.anchor.m_123343_(), 0.9);
        boolean placed = city.activeBuild.placeNext(level);
        villagerCooldown.put(v.m_20148_(), 10);
        if (!placed) {
            SimState.sayToNearestPlayer(level, city.core, (Component)Component.m_237113_((String)("\u2705 " + city.name + ": Bauprojekt fertig!")));
            city.activeBuild = null;
        }
    }

    public static void farmStep(ServerLevel level, Villager v, City city, BlockPos center, int radius) {
        for (BlockPos p : BlockPos.m_121940_((BlockPos)center.m_7918_(-radius, 0, -radius), (BlockPos)center.m_7918_(radius, 0, radius))) {
            BlockState st = level.m_8055_(p);
            BlockState above = level.m_8055_(p.m_7494_());
            if (st.m_60713_(Blocks.f_50493_) && SimState.chance(0.15)) {
                SimState.lookAt(v, p);
                SimState.swing(v);
                level.m_7731_(p, Blocks.f_50093_.m_49966_(), 3);
                return;
            }
            if (st.m_60713_(Blocks.f_50093_) && above.m_60795_() && SimState.chance(0.2)) {
                SimState.lookAt(v, p.m_7494_());
                SimState.swing(v);
                level.m_7731_(p.m_7494_(), Blocks.f_50092_.m_49966_(), 3);
                return;
            }
            if (!above.m_60713_(Blocks.f_50092_) || (Integer)above.m_61143_((Property)CropBlock.f_52244_) != 7 || !SimState.chance(0.35)) continue;
            SimState.lookAt(v, p.m_7494_());
            SimState.swing(v);
            level.m_46961_(p.m_7494_(), false);
            SimState.addResource(city.id, ResourceType.FOOD);
            return;
        }
    }

    public static void sayToNearestPlayer(ServerLevel level, BlockPos pos, Component msg) {
        List players = level.m_6907_();
        if (players.isEmpty()) {
            return;
        }
        ServerPlayer nearest = null;
        double best = Double.MAX_VALUE;
        for (ServerPlayer p : players) {
            double d = p.m_20275_((double)pos.m_123341_() + 0.5, (double)pos.m_123342_() + 0.5, (double)pos.m_123343_() + 0.5);
            if (!(d < best)) continue;
            best = d;
            nearest = p;
        }
        if (nearest != null) {
            nearest.m_213846_(msg);
        }
    }
}

