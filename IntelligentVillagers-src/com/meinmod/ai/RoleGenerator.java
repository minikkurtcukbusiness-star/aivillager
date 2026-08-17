/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.ai;

import java.util.Random;
import java.util.UUID;

public class RoleGenerator {
    private static final String[] ROLES = new String[]{"Miner", "Farmer", "Builder", "Scout"};

    public static String randomRole(UUID id) {
        Random r = new Random(id.getMostSignificantBits() ^ id.getLeastSignificantBits() ^ 0xFFFFFFFFFFFFFFFFL);
        return ROLES[r.nextInt(ROLES.length)];
    }
}

