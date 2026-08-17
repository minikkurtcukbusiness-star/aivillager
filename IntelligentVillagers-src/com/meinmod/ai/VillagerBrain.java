/*
 * Decompiled with CFR 0.152.
 */
package com.meinmod.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VillagerBrain {
    public final UUID villagerId;
    public String name;
    public String role;
    public String currentTask;
    public int intelligence = 1;
    public final List<String> memory = new ArrayList<String>();

    public VillagerBrain(UUID id, String name, String role) {
        this.villagerId = id;
        this.name = name;
        this.role = role;
        this.currentTask = "wichtiger Arbeit f\u00fcrs Dorf";
    }

    public void remember(String text) {
        if (this.memory.size() >= 12) {
            this.memory.remove(0);
        }
        this.memory.add(text);
    }
}

