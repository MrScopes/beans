import commands.CommandManager
import events.EventManager
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.extras.optifine.OptifineSupport
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.world.DimensionType


class Server {
    init {
        println("Minecraft server starting...")
        val minecraftServer = MinecraftServer.init()
        println("Server initalized.")

        hub = loadWorld("beans")

        println("World initialized.")

        CommandManager()
        println("Commands initialized.")

        EventManager()
        println("Events initialized.")

        OptifineSupport.enable()

        MojangAuth.init();
        println("Mojang Authorization initialized.")

        minecraftServer.start("0.0.0.0", 25565)

        println("Server started.")
    }

    fun loadWorld(name: String): InstanceContainer {
        return MinecraftServer.getInstanceManager().createInstanceContainer(DimensionType.OVERWORLD, AnvilLoader(saveLocation + name))
    }

    companion object {
        lateinit var hub: InstanceContainer
        var saveLocation = "F:\\MultiMC\\instances\\1.20.1\\.minecraft\\saves\\"
    }
}
