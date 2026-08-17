/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.supermod.village;

public enum TechLevel {
    PRIMITIVE,
    BASIC,
    ADVANCED,
    REDSTONE,
    INDUSTRIAL;


    public TechLevel next() {
        int i = this.ordinal() + 1;
        return i >= TechLevel.values().length ? this : TechLevel.values()[i];
    }
}

