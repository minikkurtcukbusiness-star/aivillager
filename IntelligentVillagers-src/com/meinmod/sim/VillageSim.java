/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.CropBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.meinmod.sim;

import com.meinmod.ai.BrainRegistry;
import com.meinmod.ai.VillagerBrain;
import com.meinmod.build.Blueprints;
import com.meinmod.sim.CitySim;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class VillageSim {
    private static final Map<String, CitySim> CITIES = new HashMap<String, CitySim>();
    private static final Map<UUID, String> VILLAGER_CITY = new HashMap<UUID, String>();
    private static final Random RNG = new Random();

    public static String getCityNameOf(UUID villagerId) {
        return VILLAGER_CITY.getOrDefault(villagerId, "Wanderer");
    }

    public static String getLeaderNameOfCity(String cityName) {
        CitySim c = CITIES.get(cityName);
        if (c == null || c.leader == null) {
            return "";
        }
        return "";
    }

    public static void tick(ServerLevel level) {
        ArrayList<Villager> villagers = new ArrayList<Villager>();
        for (Entity e : level.m_142646_().m_142273_()) {
            if (!(e instanceof Villager)) continue;
            Villager v = (Villager)e;
            villagers.add(v);
        }
        if (villagers.isEmpty()) {
            return;
        }
        VillageSim.ensureCities(level, villagers);
        VillageSim.electMayors(villagers);
        for (Villager v : villagers) {
            CitySim city;
            VillagerBrain b = BrainRegistry.get(v);
            String cityName = VILLAGER_CITY.get(v.m_20148_());
            CitySim citySim = city = cityName == null ? null : CITIES.get(cityName);
            if (city == null) continue;
            v.m_6593_((Component)Component.m_237113_((String)("\u00a7e" + b.name + " \u00a77[" + city.name + "] \u00a7f" + VillageSim.roleIcon(b.role))));
            v.m_20340_(true);
            if (b.role.equals("Miner")) {
                b.currentTask = "Rohstoffe aus der Mine";
                VillageSim.doMining(level, v, city);
                continue;
            }
            if (b.role.equals("Farmer")) {
                b.currentTask = "Nahrung f\u00fcr die Vorr\u00e4te";
                VillageSim.doFarming(level, v, city);
                continue;
            }
            if (b.role.equals("Builder")) {
                b.currentTask = "Bauprojekte f\u00fcr die Stadt";
                VillageSim.doBuilding(level, v, city);
                continue;
            }
            b.currentTask = "Erkundung und Sicherheit";
            VillageSim.doScout(level, v, city);
        }
    }

    private static void ensureCities(ServerLevel level, List<Villager> villagers) {
        if (CITIES.isEmpty()) {
            Villager v = villagers.get(0);
            String name = VillageSim.randomCityName(v.m_20148_());
            CitySim c = new CitySim(name, v.m_20183_());
            CITIES.put(name, c);
        }
        for (Villager v : villagers) {
            String bestCity = null;
            double best = Double.MAX_VALUE;
            for (CitySim c : CITIES.values()) {
                double d = c.core.m_123331_((Vec3i)v.m_20183_());
                if (!(d < best)) continue;
                best = d;
                bestCity = c.name;
            }
            if (bestCity != null && best > 1200.0) {
                CitySim c;
                String name = VillageSim.randomCityName(v.m_20148_());
                c = new CitySim(name, v.m_20183_());
                CITIES.put(name, c);
                bestCity = name;
            }
            if (bestCity == null) continue;
            VILLAGER_CITY.put(v.m_20148_(), bestCity);
        }
    }

    private static void electMayors(List<Villager> villagers) {
        HashMap<String, UUID> first = new HashMap<String, UUID>();
        for (Villager villager : villagers) {
            String city = VILLAGER_CITY.get(villager.m_20148_());
            if (city == null) continue;
            first.putIfAbsent(city, villager.m_20148_());
        }
        for (Map.Entry entry : first.entrySet()) {
            CitySim c = CITIES.get(entry.getKey());
            if (c == null || c.leader != null) continue;
            c.leader = (UUID)entry.getValue();
        }
    }

    private static void doMining(ServerLevel level, Villager v, CitySim city) {
        BlockState st;
        BlockPos target = VillageSim.findMineTarget(level, v.m_20183_(), 7);
        if (target == null) {
            return;
        }
        v.m_21563_().m_24946_((double)target.m_123341_() + 0.5, (double)target.m_123342_() + 0.5, (double)target.m_123343_() + 0.5);
        v.m_6674_(InteractionHand.MAIN_HAND);
        if (RNG.nextDouble() < 0.25 && ((st = level.m_8055_(target)).m_60713_(Blocks.f_49997_) || st.m_60713_(Blocks.f_49996_) || st.m_60713_(Blocks.f_49995_) || st.m_60713_(Blocks.f_50089_) || st.m_60713_(Blocks.f_50069_) || st.m_60713_(Blocks.f_152550_))) {
            level.m_46961_(target, false);
            city.addRes(VillageSim.resKey(st.m_60734_().m_49954_().getString()), 1);
        }
    }

    private static void doFarming(ServerLevel level, Villager v, CitySim city) {
        BlockPos center = city.core.m_7918_(8, 0, 0);
        int r = 4;
        for (BlockPos p : BlockPos.m_121940_((BlockPos)center.m_7918_(-r, 0, -r), (BlockPos)center.m_7918_(r, 0, r))) {
            int age;
            BlockState below = level.m_8055_(p);
            BlockState above = level.m_8055_(p.m_7494_());
            if (below.m_60713_(Blocks.f_50493_) && RNG.nextDouble() < 0.12) {
                v.m_21563_().m_24946_((double)p.m_123341_() + 0.5, (double)p.m_123342_() + 0.5, (double)p.m_123343_() + 0.5);
                v.m_6674_(InteractionHand.MAIN_HAND);
                level.m_7731_(p, Blocks.f_50093_.m_49966_(), 3);
                return;
            }
            if (below.m_60713_(Blocks.f_50093_) && above.m_60795_() && RNG.nextDouble() < 0.18) {
                v.m_21563_().m_24946_((double)p.m_123341_() + 0.5, (double)p.m_123342_() + 1.2, (double)p.m_123343_() + 0.5);
                v.m_6674_(InteractionHand.MAIN_HAND);
                level.m_7731_(p.m_7494_(), Blocks.f_50092_.m_49966_(), 3);
                return;
            }
            if (!above.m_60713_(Blocks.f_50092_) || (age = ((Integer)above.m_61143_((Property)CropBlock.f_52244_)).intValue()) < 7 || !(RNG.nextDouble() < 0.25)) continue;
            v.m_21563_().m_24946_((double)p.m_123341_() + 0.5, (double)p.m_123342_() + 1.2, (double)p.m_123343_() + 0.5);
            v.m_6674_(InteractionHand.MAIN_HAND);
            level.m_46961_(p.m_7494_(), false);
            city.addRes("FOOD", 1);
            return;
        }
    }

    private static void doBuilding(ServerLevel level, Villager v, CitySim city) {
        if (city.activeBuild == null) {
            city.activeBuild = Blueprints.townHall(city.core.m_7918_(-8, 0, 0));
        }
        boolean placed = city.activeBuild.placeNext(level);
        v.m_6674_(InteractionHand.MAIN_HAND);
        if (!placed) {
            city.activeBuild = null;
            city.techLevel = Math.min(10, city.techLevel + 1);
        }
    }

    private static void doScout(ServerLevel level, Villager v, CitySim city) {
        BlockPos p = city.core.m_7918_(RNG.nextInt(21) - 10, 0, RNG.nextInt(21) - 10);
        v.m_21573_().m_26519_((double)p.m_123341_(), (double)p.m_123342_(), (double)p.m_123343_(), 0.9);
    }

    private static BlockPos findMineTarget(ServerLevel level, BlockPos origin, int radius) {
        for (BlockPos p : BlockPos.m_121940_((BlockPos)origin.m_7918_(-radius, -2, -radius), (BlockPos)origin.m_7918_(radius, 2, radius))) {
            BlockState st = level.m_8055_(p);
            if (!st.m_60713_(Blocks.f_49997_) && !st.m_60713_(Blocks.f_49996_) && !st.m_60713_(Blocks.f_49995_) && !st.m_60713_(Blocks.f_50089_) && !st.m_60713_(Blocks.f_50069_) && !st.m_60713_(Blocks.f_152550_)) continue;
            return p.m_7949_();
        }
        return null;
    }

    private static String randomCityName(UUID id) {
        String[] a = new String[]{"Neu", "Alt", "Stein", "Eisen", "Gold", "Schatten", "Sonnen", "Frost", "Wolken", "Gr\u00fcn"};
        String[] b = new String[]{"hain", "furt", "burg", "tal", "feld", "wald", "stadt", "mark", "heim", "kliff"};
        Random r = new Random(id.getMostSignificantBits() ^ id.getLeastSignificantBits());
        return a[r.nextInt(a.length)] + b[r.nextInt(b.length)];
    }

    private static String roleIcon(String role) {
        return switch (role) {
            case "Miner" -> "\u26cf";
            case "Farmer" -> "\ud83c\udf3e";
            case "Builder" -> "\ud83c\udfd7";
            default -> "\ud83d\udc41";
        };
    }

    private static String resKey(String blockName) {
        String s = blockName.toUpperCase(Locale.ROOT);
        if (s.contains("DIAMOND")) {
            return "DIAMOND";
        }
        if (s.contains("GOLD")) {
            return "GOLD";
        }
        if (s.contains("IRON")) {
            return "IRON";
        }
        if (s.contains("COAL")) {
            return "COAL";
        }
        return "STONE";
    }
}

