/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.ai;

import com.meinmod.ai.LocalDialogueAI;
import com.meinmod.ai.VillagerBrain;

public class AIManager {
    public static String generateDialogue(VillagerBrain brain, String cityName, String leaderName) {
        String base = LocalDialogueAI.respond(brain, cityName, leaderName);
        return base;
    }
}

