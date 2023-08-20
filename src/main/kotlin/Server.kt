import blocks.BlockManager
import commands.CommandManager
import events.EventManager
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.extras.MojangAuth
import net.minestom.server.extras.optifine.OptifineSupport
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.world.DimensionType

class Server {
    init {
        val minecraftServer = MinecraftServer.init()
        MinecraftServer.setChunkViewDistance(16)
        MinecraftServer.setBrandName("beans")

        BlockManager()

        instance = MinecraftServer.getInstanceManager().createInstanceContainer(DimensionType.OVERWORLD, AnvilLoader(saveLocation + "beans"))
        val border = instance.worldBorder
        border.setCenter(0f, 0f)
        border.setDiameter(202.0, 0)
        border.warningBlocks = 0

        CommandManager()
        EventManager()

        OptifineSupport.enable()

        MojangAuth.init();

        minecraftServer.start("0.0.0.0", 25565)
    }

    companion object {
        lateinit var instance: InstanceContainer
        var saveLocation = "F:\\MultiMC\\instances\\1.20.1\\.minecraft\\saves\\"
        val spawn = Pos(-99.0, 61.0, -99.0, -44f, 3f)
    }
}
