package me.mrscopes.beans.regions

import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion

class Regions {
    companion object {
        val spawnRegion = CuboidRegion(BlockVector3.at(-86, 20, -86), BlockVector3.at(-101, 255, -101))
        val mineRegion = CuboidRegion(BlockVector3.at(20, 20, -20), BlockVector3.at(-20, 50, 20))
    }
}