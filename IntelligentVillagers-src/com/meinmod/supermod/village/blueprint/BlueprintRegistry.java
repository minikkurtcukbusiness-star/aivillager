/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.meinmod.supermod.village.blueprint;

import com.meinmod.supermod.village.blueprint.Blueprint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlueprintRegistry {
    private static final List<Blueprint> ALL = new ArrayList<Blueprint>();
    private static final Random RND = new Random();

    public static Blueprint pickForTech(int techOrdinal) {
        ArrayList<Blueprint> candidates = new ArrayList<Blueprint>();
        for (Blueprint b : ALL) {
            if (b.minTechOrdinal > techOrdinal) continue;
            candidates.add(b);
        }
        if (candidates.isEmpty()) {
            return ALL.get(0);
        }
        return (Blueprint)candidates.get(RND.nextInt(candidates.size()));
    }

    private static Blueprint house_large() {
        int z;
        int x;
        Blueprint b = new Blueprint("house_large", 1);
        BlockState floor = Blocks.f_50705_.m_49966_();
        BlockState wall = Blocks.f_50741_.m_49966_();
        BlockState log = Blocks.f_50000_.m_49966_();
        BlockState glass = Blocks.f_50185_.m_49966_();
        BlockState roof = Blocks.f_50373_.m_49966_();
        BlockState torch = Blocks.f_50081_.m_49966_();
        for (x = 0; x < 9; ++x) {
            for (z = 0; z < 7; ++z) {
                b.add(x, 0, z, floor);
            }
        }
        for (int y = 1; y <= 4; ++y) {
            b.add(0, y, 0, log);
            b.add(8, y, 0, log);
            b.add(0, y, 6, log);
            b.add(8, y, 6, log);
            for (int x2 = 1; x2 < 8; ++x2) {
                b.add(x2, y, 0, wall);
                b.add(x2, y, 6, wall);
            }
            for (z = 1; z < 6; ++z) {
                b.add(0, y, z, wall);
                b.add(8, y, z, wall);
            }
        }
        b.add(2, 2, 0, glass).add(3, 2, 0, glass).add(5, 2, 0, glass).add(6, 2, 0, glass);
        b.add(2, 2, 6, glass).add(3, 2, 6, glass).add(5, 2, 6, glass).add(6, 2, 6, glass);
        b.add(0, 2, 2, glass).add(0, 2, 4, glass);
        b.add(8, 2, 2, glass).add(8, 2, 4, glass);
        b.add(4, 1, 3, torch);
        b.add(4, 3, 3, torch);
        for (x = 0; x < 9; ++x) {
            b.add(x, 5, 0, roof);
            b.add(x, 5, 6, roof);
        }
        for (int z2 = 0; z2 < 7; ++z2) {
            b.add(0, 5, z2, roof);
            b.add(8, 5, z2, roof);
        }
        for (x = 1; x < 8; ++x) {
            b.add(x, 6, 1, roof);
            b.add(x, 6, 5, roof);
        }
        for (x = 2; x < 7; ++x) {
            b.add(x, 7, 3, Blocks.f_50745_.m_49966_());
        }
        return b;
    }

    private static Blueprint warehouse() {
        int z;
        int x;
        Blueprint b = new Blueprint("warehouse", 1);
        BlockState floor = Blocks.f_50222_.m_49966_();
        BlockState wall = Blocks.f_50076_.m_49966_();
        BlockState pillar = Blocks.f_50010_.m_49966_();
        BlockState roof = Blocks.f_50405_.m_49966_();
        BlockState chest = Blocks.f_50087_.m_49966_();
        BlockState lantern = Blocks.f_50681_.m_49966_();
        for (x = 0; x < 11; ++x) {
            for (z = 0; z < 9; ++z) {
                b.add(x, 0, z, floor);
            }
        }
        for (int y = 1; y <= 5; ++y) {
            b.add(0, y, 0, pillar);
            b.add(10, y, 0, pillar);
            b.add(0, y, 8, pillar);
            b.add(10, y, 8, pillar);
            for (int x2 = 1; x2 < 10; ++x2) {
                b.add(x2, y, 0, wall);
                b.add(x2, y, 8, wall);
            }
            for (z = 1; z < 8; ++z) {
                b.add(0, y, z, wall);
                b.add(10, y, z, wall);
            }
        }
        for (x = 2; x <= 8; x += 2) {
            b.add(x, 1, 2, chest);
            b.add(x, 1, 6, chest);
        }
        for (x = 0; x < 11; ++x) {
            for (z = 0; z < 9; ++z) {
                b.add(x, 6, z, roof);
            }
        }
        b.add(5, 5, 4, lantern);
        return b;
    }

    private static Blueprint tower_watch() {
        int y;
        int z;
        int x;
        Blueprint b = new Blueprint("watch_tower", 2);
        BlockState base = Blocks.f_50222_.m_49966_();
        BlockState wall = Blocks.f_50652_.m_49966_();
        BlockState stair = Blocks.f_50194_.m_49966_();
        BlockState fence = Blocks.f_50132_.m_49966_();
        BlockState torch = Blocks.f_50139_.m_49966_();
        for (x = 0; x < 7; ++x) {
            for (z = 0; z < 7; ++z) {
                b.add(x, 0, z, base);
            }
        }
        for (y = 1; y <= 10; ++y) {
            for (int x2 = 1; x2 <= 5; ++x2) {
                b.add(x2, y, 1, wall);
                b.add(x2, y, 5, wall);
            }
            for (z = 2; z <= 4; ++z) {
                b.add(1, y, z, wall);
                b.add(5, y, z, wall);
            }
        }
        for (y = 1; y <= 9; ++y) {
            int step = y % 4;
            if (step == 1) {
                b.add(2, y, 2, stair);
            }
            if (step == 2) {
                b.add(3, y, 2, stair);
            }
            if (step == 3) {
                b.add(3, y, 3, stair);
            }
            if (step != 0) continue;
            b.add(2, y, 3, stair);
        }
        for (x = 0; x < 7; ++x) {
            for (z = 0; z < 7; ++z) {
                b.add(x, 11, z, Blocks.f_50705_.m_49966_());
            }
        }
        for (x = 0; x < 7; ++x) {
            b.add(x, 12, 0, fence);
            b.add(x, 12, 6, fence);
        }
        for (int z2 = 0; z2 < 7; ++z2) {
            b.add(0, 12, z2, fence);
            b.add(6, 12, z2, fence);
        }
        b.add(3, 12, 3, torch);
        return b;
    }

    private static Blueprint town_hall() {
        int z;
        int x;
        Blueprint b = new Blueprint("town_hall", 2);
        BlockState floor = Blocks.f_50387_.m_49966_();
        BlockState wall = Blocks.f_50470_.m_49966_();
        BlockState pillar = Blocks.f_50283_.m_49966_();
        BlockState glass = Blocks.f_50058_.m_49966_();
        BlockState roof = Blocks.f_50452_.m_49966_();
        BlockState lantern = Blocks.f_50681_.m_49966_();
        for (x = 0; x < 15; ++x) {
            for (z = 0; z < 11; ++z) {
                b.add(x, 0, z, floor);
            }
        }
        for (int y = 1; y <= 7; ++y) {
            b.add(0, y, 0, pillar);
            b.add(14, y, 0, pillar);
            b.add(0, y, 10, pillar);
            b.add(14, y, 10, pillar);
            for (int x2 = 1; x2 < 14; ++x2) {
                b.add(x2, y, 0, wall);
                b.add(x2, y, 10, wall);
            }
            for (z = 1; z < 10; ++z) {
                b.add(0, y, z, wall);
                b.add(14, y, z, wall);
            }
        }
        for (x = 4; x <= 10; ++x) {
            for (int y = 2; y <= 5; ++y) {
                b.add(x, y, 0, glass);
            }
        }
        for (x = 0; x < 15; ++x) {
            for (z = 0; z < 11; ++z) {
                b.add(x, 8, z, roof);
            }
        }
        b.add(7, 6, 5, lantern);
        b.add(4, 6, 3, lantern);
        b.add(10, 6, 3, lantern);
        for (x = 6; x <= 8; ++x) {
            for (z = 8; z <= 9; ++z) {
                b.add(x, 1, z, Blocks.f_50074_.m_49966_());
            }
        }
        return b;
    }

    private static Blueprint windmill() {
        int i;
        int y;
        int z;
        Blueprint b = new Blueprint("windmill", 2);
        BlockState stone = Blocks.f_50069_.m_49966_();
        BlockState plank = Blocks.f_50742_.m_49966_();
        BlockState log = Blocks.f_50006_.m_49966_();
        BlockState wool = Blocks.f_50041_.m_49966_();
        for (int x = 0; x < 9; ++x) {
            for (z = 0; z < 9; ++z) {
                b.add(x, 0, z, stone);
            }
        }
        for (y = 1; y <= 12; ++y) {
            for (int x = 2; x <= 6; ++x) {
                b.add(x, y, 2, plank);
                b.add(x, y, 6, plank);
            }
            for (z = 3; z <= 5; ++z) {
                b.add(2, y, z, plank);
                b.add(6, y, z, plank);
            }
            b.add(4, y, 4, Blocks.f_50016_.m_49966_());
        }
        for (y = 8; y <= 10; ++y) {
            b.add(4, y, 1, log);
        }
        for (i = 0; i < 5; ++i) {
            b.add(4, 9, 1 - i, wool);
        }
        for (i = 0; i < 5; ++i) {
            b.add(4 + i, 9, 1, wool);
        }
        for (i = 0; i < 5; ++i) {
            b.add(4 - i, 9, 1, wool);
        }
        for (i = 0; i < 5; ++i) {
            b.add(4, 9, 1 + i, wool);
        }
        return b;
    }

    private static Blueprint mine_entrance() {
        int x;
        Blueprint b = new Blueprint("mine_entrance", 1);
        BlockState stone = Blocks.f_50652_.m_49966_();
        BlockState log = Blocks.f_49999_.m_49966_();
        BlockState fence = Blocks.f_50132_.m_49966_();
        BlockState torch = Blocks.f_50081_.m_49966_();
        for (x = 0; x < 9; ++x) {
            for (int z = 0; z < 7; ++z) {
                b.add(x, 0, z, stone);
            }
        }
        for (int y = 1; y <= 4; ++y) {
            b.add(2, y, 1, log);
            b.add(6, y, 1, log);
        }
        for (x = 2; x <= 6; ++x) {
            b.add(x, 5, 1, log);
        }
        for (x = 0; x < 9; ++x) {
            b.add(x, 1, 0, fence);
            b.add(x, 1, 6, fence);
        }
        b.add(4, 1, 3, torch);
        b.add(4, 2, 3, torch);
        return b;
    }

    private static Blueprint wall_gate_redstone() {
        int x;
        int y;
        Blueprint b = new Blueprint("redstone_gate", 3);
        BlockState brick = Blocks.f_152589_.m_49966_();
        BlockState lamp = Blocks.f_50261_.m_49966_();
        BlockState block = Blocks.f_50330_.m_49966_();
        BlockState iron = Blocks.f_50183_.m_49966_();
        int width = 17;
        int gateX1 = 7;
        int gateX2 = 9;
        for (y = 0; y < 4; ++y) {
            for (x = 0; x < width; ++x) {
                boolean gate;
                boolean bl = gate = x >= gateX1 && x <= gateX2;
                if (gate && y <= 2) {
                    b.add(x, y, 0, Blocks.f_50016_.m_49966_());
                    continue;
                }
                b.add(x, y, 0, brick);
            }
        }
        for (y = 0; y < 3; ++y) {
            for (x = gateX1; x <= gateX2; ++x) {
                b.add(x, y, 0, iron);
            }
        }
        b.add(2, 2, 0, lamp).add(2, 1, 1, block);
        b.add(14, 2, 0, lamp).add(14, 1, 1, block);
        return b;
    }

    private static Blueprint greenhouse() {
        int z;
        int x;
        Blueprint b = new Blueprint("greenhouse", 2);
        BlockState floor = Blocks.f_152544_.m_49966_();
        BlockState glass = Blocks.f_50058_.m_49966_();
        BlockState frame = Blocks.f_50472_.m_49966_();
        BlockState water = Blocks.f_49990_.m_49966_();
        for (x = 0; x < 13; ++x) {
            for (z = 0; z < 9; ++z) {
                b.add(x, 0, z, floor);
            }
        }
        for (int y = 1; y <= 5; ++y) {
            b.add(0, y, 0, frame);
            b.add(12, y, 0, frame);
            b.add(0, y, 8, frame);
            b.add(12, y, 8, frame);
            for (int x2 = 1; x2 < 12; ++x2) {
                b.add(x2, y, 0, glass);
                b.add(x2, y, 8, glass);
            }
            for (z = 1; z < 8; ++z) {
                b.add(0, y, z, glass);
                b.add(12, y, z, glass);
            }
        }
        for (x = 0; x < 13; ++x) {
            for (z = 0; z < 9; ++z) {
                b.add(x, 6, z, glass);
            }
        }
        for (int z2 = 2; z2 <= 6; ++z2) {
            b.add(6, 1, z2, water);
        }
        return b;
    }

    private static Blueprint industrial_workshop() {
        int z;
        int x;
        Blueprint b = new Blueprint("industrial_workshop", 4);
        BlockState floor = Blocks.f_152559_.m_49966_();
        BlockState wall = Blocks.f_152555_.m_49966_();
        BlockState metal = Blocks.f_50075_.m_49966_();
        BlockState glass = Blocks.f_152498_.m_49966_();
        BlockState lamp = Blocks.f_50386_.m_49966_();
        for (x = 0; x < 19; ++x) {
            for (z = 0; z < 13; ++z) {
                b.add(x, 0, z, floor);
            }
        }
        for (int y = 1; y <= 8; ++y) {
            for (int x2 = 0; x2 < 19; ++x2) {
                b.add(x2, y, 0, wall);
                b.add(x2, y, 12, wall);
            }
            for (z = 0; z < 13; ++z) {
                b.add(0, y, z, wall);
                b.add(18, y, z, wall);
            }
        }
        for (x = 2; x <= 16; ++x) {
            b.add(x, 4, 0, glass);
            b.add(x, 4, 12, glass);
        }
        for (x = 4; x <= 14; x += 2) {
            b.add(x, 1, 5, metal);
            b.add(x, 2, 5, metal);
            b.add(x, 1, 7, metal);
        }
        b.add(9, 7, 6, lamp);
        b.add(5, 7, 3, lamp);
        b.add(13, 7, 9, lamp);
        return b;
    }

    static {
        ALL.add(BlueprintRegistry.tower_watch());
        ALL.add(BlueprintRegistry.town_hall());
        ALL.add(BlueprintRegistry.warehouse());
        ALL.add(BlueprintRegistry.house_large());
        ALL.add(BlueprintRegistry.windmill());
        ALL.add(BlueprintRegistry.mine_entrance());
        ALL.add(BlueprintRegistry.wall_gate_redstone());
        ALL.add(BlueprintRegistry.greenhouse());
        ALL.add(BlueprintRegistry.industrial_workshop());
    }
}

