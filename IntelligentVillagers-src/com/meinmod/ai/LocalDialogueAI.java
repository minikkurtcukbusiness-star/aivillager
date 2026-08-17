/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.ai;

import com.meinmod.ai.VillagerBrain;
import java.util.Random;

public class LocalDialogueAI {
    private static final String[] VIBE = new String[]{"Alles l\u00e4uft ruhig.", "Wir haben noch viel zu tun.", "Heute ist ein guter Tag.", "Irgendwas liegt in der Luft."};
    private static final String[] PERSONAL = new String[]{"Ich vertraue dir noch nicht ganz, aber ich beobachte.", "Du wirkst n\u00fctzlich. Vielleicht k\u00f6nnen wir zusammenarbeiten.", "Wenn du uns in Ruhe l\u00e4sst, bauen wir etwas Gro\u00dfes.", "Manche hier reden\u2026 du w\u00fcrdest staunen."};

    public static String respond(VillagerBrain b, String cityName, String leaderName) {
        Random r = new Random();
        String intro = "Ich bin \u00a7e" + b.name + "\u00a7f, " + b.role + " aus \u00a7a" + cityName + "\u00a7f. ";
        String lead = leaderName == null || leaderName.isBlank() ? "Wir suchen noch nach einer F\u00fchrung. " : "Unser Anf\u00fchrer ist \u00a7b" + leaderName + "\u00a7f. ";
        String task = "Gerade k\u00fcmmere ich mich um \u00a76" + b.currentTask + "\u00a7f. ";
        String vibe = VIBE[r.nextInt(VIBE.length)] + " ";
        Object mem = b.memory.isEmpty() ? "Ich habe dich hier noch nicht oft gesehen. " : "Ich erinnere mich: \u00a77\"" + b.memory.get(Math.max(0, b.memory.size() - 1)) + "\"\u00a7f. ";
        String personal = PERSONAL[r.nextInt(PERSONAL.length)];
        return intro + lead + task + vibe + (String)mem + personal;
    }
}

