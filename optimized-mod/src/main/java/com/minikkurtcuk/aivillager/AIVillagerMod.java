package com.minikkurtcuk.aivillager;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(AIVillagerMod.MODID)
public class AIVillagerMod {
    public static final String MODID = "aivillager";
    public static final VillageSimulationOptimized SIM = new VillageSimulationOptimized();

    public AIVillagerMod() {
        MinecraftForge.EVENT_BUS.register(SIM);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("iv")
            .then(Commands.literal("speed")
                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.25, 10.0))
                    .executes(ctx -> {
                        SIM.speed = DoubleArgumentType.getDouble(ctx, "value");
                        ctx.getSource().sendSuccess(() -> Component.literal("§aAI Village speed: x" + SIM.speed), true);
                        return 1;
                    })))
            .then(Commands.literal("pause").executes(ctx -> {
                SIM.paused = !SIM.paused;
                ctx.getSource().sendSuccess(() -> Component.literal("§eAI Village " + (SIM.paused ? "paused" : "resumed")), true);
                return 1;
            }))
            .then(Commands.literal("status").executes(ctx -> {
                ctx.getSource().sendSuccess(() -> Component.literal(SIM.status()), false);
                return 1;
            })));
    }

    public static ServerLevel serverLevel(Level level) {
        return level instanceof ServerLevel serverLevel ? serverLevel : null;
    }
}
