/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.ai;

import java.util.Random;
import java.util.UUID;

public class NameGenerator {
    private static final String[] FIRST = new String[]{"Eldrin", "Mara", "Torik", "Lysa", "Boran", "Kael", "Nira", "Odrin", "Selva", "Rurik", "Fenra", "Ivar", "Korin", "Alma", "Darek", "Siv", "Harka", "Tovin", "Elna", "Brann"};
    private static final String[] LAST = new String[]{"Steinhieb", "Feldh\u00fcter", "Eisenblick", "Goldhand", "Waldl\u00e4ufer", "Brotmeister", "Schmiedsohn", "Karrenbauer", "H\u00f6hlenkundig", "Wassertr\u00e4ger", "Aschenpfad", "Kornwacht"};

    public static String randomName(UUID id) {
        Random r = new Random(id.getMostSignificantBits() ^ id.getLeastSignificantBits());
        return FIRST[r.nextInt(FIRST.length)] + " " + LAST[r.nextInt(LAST.length)];
    }
}

