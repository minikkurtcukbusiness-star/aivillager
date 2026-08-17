/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.sim;

import java.util.Random;

public enum WorkerRole {
    MINER("\u26cf"),
    FARMER("\ud83c\udf3e"),
    BUILDER("\ud83c\udfd7");

    public final String icon;

    private WorkerRole(String icon) {
        this.icon = icon;
    }

    public static WorkerRole random(Random r) {
        WorkerRole[] v = WorkerRole.values();
        return v[r.nextInt(v.length)];
    }
}

