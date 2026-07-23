package me.mrscopes.beans.mine;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.mrscopes.beans.Beans;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

public class MineManager {
    private static final int MINE_COUNT = 100;
    private static final int BEACON_COUNT = 25;

    private final Beans plugin;
    private final Random random = new Random();
    private final List<SavedMine> mines = new ArrayList<>();
    private final Set<Location> activeBeacons = new HashSet<>();

    private final Location corner1;
    private final Location corner2;

    private Set<BlockVector3> currentGeneratedBeacons = new HashSet<>();
    private BukkitTask beaconSoundTask;
    private boolean generatingSnapshots = false;

    public MineManager(Beans plugin) {
        this.plugin = plugin;

        World world = Bukkit.getWorld("world");

        this.corner1 = new Location(world, -0.5, -49.5, -0.5);
        this.corner2 = new Location(world, -99.5, -0.5, -99.5);

        startBeaconSoundTask();
    }

    public void generateMineSnapshots() {
        generateMineSnapshots(null);
    }

    public void generateMineSnapshots(Runnable whenDone) {
        if (generatingSnapshots) {
            plugin.getLogger().warning("Mine snapshots are already generating.");
            return;
        }

        if (corner1.getWorld() == null) {
            plugin.getLogger().warning("Could not generate mines because world is null.");
            return;
        }

        generatingSnapshots = true;
        mines.clear();
        generateMineSnapshotStep(0, whenDone);
    }

    private void generateMineSnapshotStep(int index, Runnable whenDone) {
        if (index >= MINE_COUNT) {
            generatingSnapshots = false;
            plugin.getLogger().info("Generated " + mines.size() + " mine snapshots.");

            if (whenDone != null) {
                whenDone.run();
            }

            return;
        }

        generateMineBlocks();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                SavedMine snapshot = snapshotCurrentMine();
                mines.add(snapshot);
                plugin.getLogger().info("Saved mine snapshot " + mines.size() + "/" + MINE_COUNT);
            } catch (Exception exception) {
                plugin.getLogger().log(Level.SEVERE, "Failed to snapshot mine " + (index + 1) + ".", exception);
            }

            generateMineSnapshotStep(index + 1, whenDone);
        }, 1L);
    }

    public void restoreRandomMine() {
        if (mines.isEmpty()) {
            if (!generatingSnapshots) {
                generateMineSnapshots();
            }

            plugin.getLogger().warning("Mines were empty, generating snapshots now. Try reset again after generation finishes.");
            return;
        }

        SavedMine mine = mines.get(random.nextInt(mines.size()));
        restoreMine(mine);
    }

    private void generateMineBlocks() {
        try {
            World bukkitWorld = corner1.getWorld();

            if (bukkitWorld == null) {
                return;
            }

            com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(bukkitWorld);
            CuboidRegion region = createRegion(corner1, corner2, world);

            RandomPattern pattern = new RandomPattern();
            pattern.add(BlockTypes.STONE.getDefaultState(), 50);
            pattern.add(BlockTypes.COAL_ORE.getDefaultState(), 20);
            pattern.add(BlockTypes.IRON_ORE.getDefaultState(), 15);
            pattern.add(BlockTypes.DIAMOND_ORE.getDefaultState(), 10);
            pattern.add(BlockTypes.EMERALD_ORE.getDefaultState(), 4.9);
            pattern.add(BlockTypes.OBSIDIAN.getDefaultState(), 0.1);

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                editSession.setBlocks(region, pattern);
                currentGeneratedBeacons = placeExactBeacons(region, editSession, BEACON_COUNT);
                Operations.complete(editSession.commit());
            }
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Failed to generate mine blocks.", exception);
        }
    }

    private SavedMine snapshotCurrentMine() {
        World world = corner1.getWorld();

        if (world == null) {
            throw new IllegalStateException("Mine world is null.");
        }

        int minChunkX = Math.min(corner1.getChunk().getX(), corner2.getChunk().getX());
        int maxChunkX = Math.max(corner1.getChunk().getX(), corner2.getChunk().getX());
        int minChunkZ = Math.min(corner1.getChunk().getZ(), corner2.getChunk().getZ());
        int maxChunkZ = Math.max(corner1.getChunk().getZ(), corner2.getChunk().getZ());

        SavedMine savedMine = new SavedMine(world);

        for (BlockVector3 beacon : currentGeneratedBeacons) {
            savedMine.beacons.add(new Location(
                    world,
                    beacon.x() + 0.5,
                    beacon.y() + 0.5,
                    beacon.z() + 0.5
            ));
        }

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                Chunk chunk = world.getChunkAt(cx, cz);
                LevelChunk nmsChunk = getNmsChunk(chunk);

                savedMine.chunks.add(new SavedMineChunk(
                        cx,
                        cz,
                        copySections(nmsChunk.getSections())
                ));
            }
        }

        return savedMine;
    }

    private void restoreMine(SavedMine mine) {
        List<SavedMineChunk> refreshedChunks = new ArrayList<>(mine.chunks.size());

        for (SavedMineChunk savedChunk : mine.chunks) {
            Chunk chunk = mine.world.getChunkAt(savedChunk.x, savedChunk.z);
            LevelChunk nmsChunk = getNmsChunk(chunk);

            LevelChunkSection[] liveSections = nmsChunk.getSections();
            LevelChunkSection[] savedSections = savedChunk.sections;

            int length = Math.min(liveSections.length, savedSections.length);

            for (int i = 0; i < length; i++) {
                liveSections[i] = savedSections[i].copy();
            }

            refreshedChunks.add(savedChunk);
        }

        forceClientChunkReload(mine.world, refreshedChunks);

        activeBeacons.clear();
        activeBeacons.addAll(mine.beacons);
    }

    private void forceClientChunkReload(World world, List<SavedMineChunk> chunks) {
        ServerLevel level = ((CraftWorld) world).getHandle();

        for (Player player : world.getPlayers()) {
            CraftPlayer craftPlayer = (CraftPlayer) player;

            for (SavedMineChunk savedChunk : chunks) {
                ChunkPos pos = new ChunkPos(savedChunk.x, savedChunk.z);

                craftPlayer.getHandle().connection.send(
                        new ClientboundForgetLevelChunkPacket(pos)
                );
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline() || player.getWorld() != world) {
                    return;
                }

                CraftPlayer currentCraftPlayer = (CraftPlayer) player;

                for (SavedMineChunk savedChunk : chunks) {
                    LevelChunk nmsChunk = getNmsChunk(world.getChunkAt(savedChunk.x, savedChunk.z));

                    currentCraftPlayer.getHandle().connection.send(
                            new ClientboundLevelChunkWithLightPacket(nmsChunk, level.getLightEngine(), null, null)
                    );
                }
            }, 2L);
        }
    }

    private Set<BlockVector3> placeExactBeacons(CuboidRegion region, EditSession editSession, int amount) throws Exception {
        Set<BlockVector3> used = new HashSet<>();

        int minX = region.getMinimumPoint().x();
        int minY = region.getMinimumPoint().y();
        int minZ = region.getMinimumPoint().z();

        int width = region.getMaximumPoint().x() - minX + 1;
        int height = region.getMaximumPoint().y() - minY + 1;
        int length = region.getMaximumPoint().z() - minZ + 1;

        int attempts = 0;
        int maxAttempts = amount * 100;

        while (used.size() < amount && attempts < maxAttempts) {
            attempts++;

            int x = minX + random.nextInt(width);
            int y = minY + random.nextInt(height);
            int z = minZ + random.nextInt(length);

            BlockVector3 position = BlockVector3.at(x, y, z);

            if (!used.add(position)) {
                continue;
            }

            editSession.setBlock(position, BlockTypes.BEACON.getDefaultState());
        }

        if (used.size() < amount) {
            plugin.getLogger().warning("Only placed " + used.size() + "/" + amount + " beacons in mine.");
        }

        return used;
    }

    public void teleportPlayersToMineTop() {
        Bounds bounds = getBounds();
        double teleportY = bounds.maxY + 1.0;

        World world = corner1.getWorld();
        if (world == null) {
            return;
        }

        for (Player player : world.getPlayers()) {
            Location location = player.getLocation();

            if (location.getX() < bounds.minX || location.getX() > bounds.maxX + 1) continue;
            if (location.getY() < bounds.minY || location.getY() > bounds.maxY + 1) continue;
            if (location.getZ() < bounds.minZ || location.getZ() > bounds.maxZ + 1) continue;

            Location teleportLocation = location.clone();
            teleportLocation.setY(teleportY);

            player.teleport(teleportLocation);
        }
    }

    private void startBeaconSoundTask() {
        beaconSoundTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (activeBeacons.size() <= BEACON_COUNT / 3) {
                beaconSoundTask.cancel();
                beaconSoundTask = null;
                restoreRandomMine();
                return;
            }

            activeBeacons.removeIf(location -> {
                World world = location.getWorld();

                if (world == null) {
                    return true;
                }

                Location blockLocation = location.clone();
                blockLocation.setX(location.getBlockX());
                blockLocation.setY(location.getBlockY());
                blockLocation.setZ(location.getBlockZ());

                if (blockLocation.getBlock().getType() != Material.BEACON) {
                    return true;
                }

                world.playSound(
                        blockLocation.clone().add(0.5, 0.5, 0.5),
                        "minecraft:block.beacon.ambient",
                        SoundCategory.BLOCKS,
                        2.0f,
                        1.0f
                );

                return false;
            });
        }, 20L, 20L);
    }

    public void stopBeaconSoundTask() {
        if (beaconSoundTask != null) {
            beaconSoundTask.cancel();
            beaconSoundTask = null;
        }
    }

    private CuboidRegion createRegion(Location corner1, Location corner2, com.sk89q.worldedit.world.World world) {
        Bounds bounds = getBounds();

        BlockVector3 min = BlockVector3.at(bounds.minX, bounds.minY, bounds.minZ);
        BlockVector3 max = BlockVector3.at(bounds.maxX, bounds.maxY, bounds.maxZ);

        return new CuboidRegion(world, min, max);
    }

    private Bounds getBounds() {
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());

        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());

        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        return new Bounds(minX, maxX, minY, maxY, minZ, maxZ);
    }

    private LevelChunk getNmsChunk(Chunk chunk) {
        ChunkAccess access = ((CraftChunk) chunk).getHandle(ChunkStatus.FULL);
        return (LevelChunk) access;
    }

    private LevelChunkSection[] copySections(LevelChunkSection[] sections) {
        LevelChunkSection[] copy = new LevelChunkSection[sections.length];

        for (int i = 0; i < sections.length; i++) {
            copy[i] = sections[i].copy();
        }

        return copy;
    }

    public int getMineCount() {
        return mines.size();
    }

    private record Bounds(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {}

    private static class SavedMine {
        private final World world;
        private final List<SavedMineChunk> chunks = new ArrayList<>();
        private final List<Location> beacons = new ArrayList<>();

        private SavedMine(World world) {
            this.world = world;
        }
    }

    private record SavedMineChunk(int x, int z, LevelChunkSection[] sections) {}
}