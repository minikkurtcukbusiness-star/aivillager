/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.sim;

import java.util.UUID;

public class NameGen {
    private static final String[] FIRST = new String[]{"Alric", "Borin", "Cedrik", "Doran", "Elric", "Fenn", "Garrik", "Haldor", "Ismar", "Jorin", "Kerrin", "Loric", "Marek", "Nestor", "Orin", "Perrin", "Quint", "Roderic", "Sorin", "Tarin"};
    private static final String[] LAST = new String[]{"Steinhieb", "Feldh\u00fcter", "Eisenblick", "Goldhand", "Waldl\u00e4ufer", "Brotmeister", "Schmiedsohn", "Karrenbauer", "H\u00f6hlenkundiger", "Wassertr\u00e4ger"};

    public static String nameFor(UUID id) {
        long x = id.getMostSignificantBits() ^ id.getLeastSignificantBits();
        int a = (int)(Math.abs(x) % (long)FIRST.length);
        int b = (int)(Math.abs(x / 97L) % (long)LAST.length);
        return FIRST[a] + " " + LAST[b];
    }
}

