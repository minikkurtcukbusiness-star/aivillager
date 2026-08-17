/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.DoubleArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.npc.Villager
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.RegisterCommandsEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$EntityInteract
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod
 */
package com.meinmod.supermod;

import com.meinmod.supermod.village.VillageData;
import com.meinmod.supermod.village.VillageManager;
import com.meinmod.supermod.village.VillagerRole;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(value="supermod")
public class SuperMod {
    public static double DEVELOPMENT_SPEED = 1.0;

    public SuperMod() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        VillageManager.init();
        System.out.println("[SuperMod] geladen (Forge 1.20.1)");
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register((LiteralArgumentBuilder)Commands.m_82127_((String)"villager_speed").then(Commands.m_82129_((String)"value", (ArgumentType)DoubleArgumentType.doubleArg((double)0.1, (double)10.0)).executes(ctx -> {
            DEVELOPMENT_SPEED = DoubleArgumentType.getDouble((CommandContext)ctx, (String)"value");
            ((CommandSourceStack)ctx.getSource()).m_288197_(() -> Component.m_237113_((String)("\u00a7a[SuperMod] Entwicklungsgeschwindigkeit: x" + DEVELOPMENT_SPEED)), true);
            return 1;
        })));
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();
        if (!(entity instanceof Villager)) {
            return;
        }
        Villager v = (Villager)entity;
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        VillageData data = VillageManager.getVillage(level2);
        VillagerRole role = VillagerRole.getRole(v);
        String task = data != null && data.getCurrentTaskName() != null ? data.getCurrentTaskName() : "keins";
        String prog = data != null ? data.getCurrentTaskProgressPct() + "%" : "?";
        String tech = data != null && data.getTechLevel() != null ? data.getTechLevel().name() : "?";
        String speed = String.valueOf(DEVELOPMENT_SPEED);
        event.getEntity().m_213846_((Component)Component.m_237113_((String)("\u00a7e[SuperMod] Rolle: \u00a7f" + String.valueOf((Object)role) + " \u00a77| \u00a7eTech: \u00a7f" + tech + " \u00a77| \u00a7eProjekt: \u00a7f" + task + " \u00a77(" + prog + ") \u00a77| \u00a7eSpeed: \u00a7fx" + speed)));
    }
}

