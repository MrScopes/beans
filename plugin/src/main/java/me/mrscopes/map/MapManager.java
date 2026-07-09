package me.mrscopes.map;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.mrscopes.MrScopes;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftChunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class MapManager {
    private final MrScopes plugin;
    private final File mapsFolder;
    private final Map<String, SavedMap> savedMaps = new HashMap<>();

    public MapManager(MrScopes plugin) {
        this.plugin = plugin;
        this.mapsFolder = new File(plugin.getDataFolder(), "maps");

        if (!this.mapsFolder.exists() && !this.mapsFolder.mkdirs()) {
            throw new IllegalStateException("Failed to create maps folder: " + mapsFolder.getAbsolutePath());
        }
    }

    public void saveMap(String name, Location corner1, Location corner2) {
        saveMapToRam(name, corner1, corner2);
        saveMapToSchematic(name, corner1, corner2);
    }

    public void restoreMap(String name, Location corner1, Location corner2) {
        String key = normalize(name);

        if (savedMaps.containsKey(key)) {
            restoreMapFromRam(name);
            return;
        }

        pasteMapFromSchematic(name, corner1);
        saveMapToRam(name, corner1, corner2);
    }

    public void saveMapToRam(String name, Location corner1, Location corner2) {
        World world = corner1.getWorld();

        if (world == null || corner2.getWorld() == null) {
            return;
        }

        if (!world.equals(corner2.getWorld())) {
            return;
        }

        int minChunkX = Math.min(corner1.getChunk().getX(), corner2.getChunk().getX());
        int maxChunkX = Math.max(corner1.getChunk().getX(), corner2.getChunk().getX());
        int minChunkZ = Math.min(corner1.getChunk().getZ(), corner2.getChunk().getZ());
        int maxChunkZ = Math.max(corner1.getChunk().getZ(), corner2.getChunk().getZ());

        SavedMap savedMap = new SavedMap(world);

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                Chunk chunk = world.getChunkAt(cx, cz);
                LevelChunk nmsChunk = getNmsChunk(chunk);

                savedMap.chunks.put(
                        new ChunkCoord(cx, cz),
                        copySections(nmsChunk.getSections())
                );
            }
        }

        savedMaps.put(normalize(name), savedMap);
    }

    public void restoreMapFromRam(String name) {
        SavedMap savedMap = savedMaps.get(normalize(name));

        if (savedMap == null) {
            return;
        }

        World world = savedMap.world;

        for (Map.Entry<ChunkCoord, LevelChunkSection[]> entry : savedMap.chunks.entrySet()) {
            ChunkCoord coord = entry.getKey();
            LevelChunkSection[] savedSections = entry.getValue();

            Chunk chunk = world.getChunkAt(coord.x, coord.z);
            LevelChunk nmsChunk = getNmsChunk(chunk);
            LevelChunkSection[] liveSections = nmsChunk.getSections();

            int length = Math.min(liveSections.length, savedSections.length);

            for (int i = 0; i < length; i++) {
                liveSections[i] = savedSections[i].copy();
            }

            world.refreshChunk(coord.x, coord.z);
        }
    }

    public void saveMapToSchematic(String name, Location corner1, Location corner2) {
        try {
            World bukkitWorld = corner1.getWorld();

            if (bukkitWorld == null || corner2.getWorld() == null) {
                return;
            }

            if (!bukkitWorld.equals(corner2.getWorld())) {
                return;
            }

            var world = BukkitAdapter.adapt(bukkitWorld);
            CuboidRegion region = createRegion(corner1, corner2, world);
            Clipboard clipboard = new BlockArrayClipboard(region);

            ForwardExtentCopy copy = new ForwardExtentCopy(
                    world,
                    region,
                    clipboard,
                    region.getMinimumPoint()
            );

            copy.setCopyingEntities(false);

            Operations.complete(copy);

            File file = getSchematicFile(name);

            try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_V3_SCHEMATIC.getWriter(new FileOutputStream(file))) {
                writer.write(clipboard);
            }
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save schematic: " + normalize(name), exception);
        }
    }

    public void pasteMapFromSchematic(String name, Location pasteOrigin) {
        try {
            World bukkitWorld = pasteOrigin.getWorld();

            File file = getSchematicFile(name);

            if (!file.exists()) {
                return;
            }

            Clipboard clipboard;

            try (ClipboardReader reader = Objects.requireNonNull(ClipboardFormats.findByFile(file)).getReader(new FileInputStream(file))) {
                clipboard = reader.read();
            }

            var world = BukkitAdapter.adapt(bukkitWorld);

            try (var editSession = WorldEdit.getInstance().newEditSession(world)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(
                                pasteOrigin.getBlockX(),
                                pasteOrigin.getBlockY(),
                                pasteOrigin.getBlockZ()
                        ))
                        .ignoreAirBlocks(false)
                        .copyEntities(false)
                        .build();

                Operations.complete(operation);
            }
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Unable to paste map from schematic: " + normalize(name), exception);
        }
    }

    public boolean hasMapInRam(String name) {
        return savedMaps.containsKey(normalize(name));
    }

    public boolean hasMapSchematic(String name) {
        return getSchematicFile(name).exists();
    }

    public void deleteMapFromRam(String name) {
        savedMaps.remove(normalize(name));
    }

    public File getSchematicFile(String name) {
        return new File(mapsFolder, normalize(name) + ".schem");
    }

    private CuboidRegion createRegion(Location corner1, Location corner2, com.sk89q.worldedit.world.World world) {
        BlockVector3 min = BlockVector3.at(
                Math.min(corner1.getBlockX(), corner2.getBlockX()),
                Math.min(corner1.getBlockY(), corner2.getBlockY()),
                Math.min(corner1.getBlockZ(), corner2.getBlockZ())
        );

        BlockVector3 max = BlockVector3.at(
                Math.max(corner1.getBlockX(), corner2.getBlockX()),
                Math.max(corner1.getBlockY(), corner2.getBlockY()),
                Math.max(corner1.getBlockZ(), corner2.getBlockZ())
        );

        return new CuboidRegion(world, min, max);
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

    private String normalize(String name) {
        return name.toLowerCase();
    }

    private record ChunkCoord(int x, int z) {
    }

    private static class SavedMap {
        private final World world;
        private final Map<ChunkCoord, LevelChunkSection[]> chunks = new HashMap<>();

        private SavedMap(World world) {
            this.world = world;
        }
    }
}