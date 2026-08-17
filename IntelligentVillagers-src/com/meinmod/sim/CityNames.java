/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.sim;

import java.util.Random;
import java.util.UUID;

public class CityNames {
    private static final String[] PREFIX = new String[]{"Neu", "Alt", "Gr\u00fcn", "Stein", "Eisen", "Gold", "Schatten", "Sonnen", "Wolken", "Frost"};
    private static final String[] ROOT = new String[]{"hain", "furt", "burg", "tal", "feld", "wald", "stadt", "mark", "heim", "kliff"};

    public static String randomCityName(UUID seed) {
        Random r = new Random(seed.getMostSignificantBits() ^ seed.getLeastSignificantBits());
        return PREFIX[r.nextInt(PREFIX.length)] + ROOT[r.nextInt(ROOT.length)];
    }
}

