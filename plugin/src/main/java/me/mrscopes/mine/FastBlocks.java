package me.mrscopes.mine;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FastBlocks {

    private FastBlocks() {}

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final int FLAGS = 18;

    private static final Map<Integer, SphereOffsets> SPHERE_CACHE = new ConcurrentHashMap<>();

    public record MaterialAmount(Material material, int amount) {}

    private record SphereOffsets(short[] x, short[] y, short[] z, int size) {}

    public static ArrayList<MaterialAmount> nuke(Location center, double radius) {
        if (center == null || center.getWorld() == null) {
            return new ArrayList<>();
        }

        ServerLevel level = ((CraftWorld) center.getWorld()).getHandle();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        final int cx = center.getBlockX();
        final int cy = center.getBlockY();
        final int cz = center.getBlockZ();

        final int minY = level.getMinY();
        final int maxY = level.getMaxY();

        int stone = 0;
        int coalOre = 0;
        int ironOre = 0;
        int diamondOre = 0;
        int emeraldOre = 0;

        SphereOffsets offsets = sphereOffsets(radius);

        short[] xs = offsets.x();
        short[] ys = offsets.y();
        short[] zs = offsets.z();

        for (int i = 0; i < offsets.size(); i++) {
            int y = cy + ys[i];

            if (y < minY || y >= maxY) {
                continue;
            }

            pos.set(
                    cx + xs[i],
                    y,
                    cz + zs[i]
            );

            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.STONE)) {
                stone++;
            } else if (state.is(Blocks.COAL_ORE)) {
                coalOre++;
            } else if (state.is(Blocks.IRON_ORE)) {
                ironOre++;
            } else if (state.is(Blocks.DIAMOND_ORE)) {
                diamondOre++;
            } else if (state.is(Blocks.EMERALD_ORE)) {
                emeraldOre++;
            } else if (!state.is(Blocks.OBSIDIAN)) {
                continue;
            }

            level.setBlock(pos, AIR, FLAGS);
        }

        ArrayList<MaterialAmount> result = new ArrayList<>(5);

        if (stone != 0)
            result.add(new MaterialAmount(Material.STONE, stone));

        if (coalOre != 0)
            result.add(new MaterialAmount(Material.COAL_ORE, coalOre));

        if (ironOre != 0)
            result.add(new MaterialAmount(Material.IRON_ORE, ironOre));

        if (diamondOre != 0)
            result.add(new MaterialAmount(Material.DIAMOND_ORE, diamondOre));

        if (emeraldOre != 0)
            result.add(new MaterialAmount(Material.EMERALD_ORE, emeraldOre));

        return result;
    }

    public static void warmupRadius(double radius) {
        sphereOffsets(radius);
    }

    private static SphereOffsets sphereOffsets(double radius) {
        int key = (int) Math.round(radius * 10);

        return SPHERE_CACHE.computeIfAbsent(key, k -> {
            int max = (int) Math.ceil(radius);
            double radiusSquared = radius * radius;

            int capacity = (max * 2 + 1);
            capacity *= capacity * capacity;

            short[] xs = new short[capacity];
            short[] ys = new short[capacity];
            short[] zs = new short[capacity];

            int size = 0;

            for (int x = -max; x <= max; x++) {
                int xx = x * x;

                for (int y = -max; y <= max; y++) {
                    int xxyy = xx + y * y;

                    for (int z = -max; z <= max; z++) {
                        if (xxyy + z * z <= radiusSquared) {
                            xs[size] = (short) x;
                            ys[size] = (short) y;
                            zs[size] = (short) z;
                            size++;
                        }
                    }
                }
            }

            short[] finalX = new short[size];
            short[] finalY = new short[size];
            short[] finalZ = new short[size];

            System.arraycopy(xs, 0, finalX, 0, size);
            System.arraycopy(ys, 0, finalY, 0, size);
            System.arraycopy(zs, 0, finalZ, 0, size);

            return new SphereOffsets(finalX, finalY, finalZ, size);
        });
    }
}