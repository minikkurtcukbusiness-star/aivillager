/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.event.TickEvent$ServerTickEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 */
package com.meinmod.supermod.village;

import com.meinmod.supermod.village.VillageData;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VillageManager {
    private static final Map<ServerLevel, VillageData> villages = new HashMap<ServerLevel, VillageData>();

    public static void init() {
        MinecraftForge.EVENT_BUS.register((Object)new VillageManager());
    }

    public static VillageData getVillage(ServerLevel level) {
        return villages.get(level);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        event.getServer().m_129785_().forEach(level -> {
            VillageData data = villages.computeIfAbsent((ServerLevel)level, l -> new VillageData());
            data.tick((ServerLevel)level);
        });
    }
}

