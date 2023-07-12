package me.mrscopes.beans.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.mrscopes.beans.Beans
import me.mrscopes.beans.events.Events
import me.mrscopes.beans.mongo.listeners.ConnectListener
import me.mrscopes.beans.mongo.listeners.QuitListener
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bukkit.Bukkit
import java.util.*
import kotlin.collections.HashMap

class Mongo(val plugin: Beans, url: String) {
    val mongoPlayers: HashMap<UUID, MongoPlayer>
    val playerCollection: MongoCollection<MongoPlayer>
    val database: MongoDatabase
    val client: MongoClient

    init {
        val pojoCodecRegistry = fromRegistries(
            com.mongodb.MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        )

        val codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(url))
            .codecRegistry(codecRegistry)
            .build()

        client = MongoClients.create(clientSettings)
        database = client.getDatabase("beans")
        playerCollection = database.withCodecRegistry(pojoCodecRegistry).getCollection("players", MongoPlayer::class.java)
        mongoPlayers = HashMap()

        Events.registerEvents(listOf(ConnectListener(), QuitListener()))

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable {
            plugin.logger.info("Backing up all players...")
            mongoPlayers.values.forEach { putPlayerInDatabase(it) }
            plugin.logger.info("Backed up all players.")
        }, 0, 6000)
    }

    fun playerFromDatabase(uuid: UUID): MongoPlayer {
        var mongoPlayer = MongoPlayer()
        mongoPlayer.uuid = uuid.toString()
        try {
            mongoPlayer = playerCollection.find(eq("uuid", uuid.toString())).first()!!
        } catch (e: Exception) {
            plugin.logger.warning("Exception trying to find $uuid in mongo:\n$e")
        }
        return mongoPlayer
    }

    fun putPlayerInDatabase(mongoPlayer: MongoPlayer) {
        playerCollection.findOneAndReplace(eq("uuid", mongoPlayer.uuid), mongoPlayer, FindOneAndReplaceOptions().upsert(true))
    }

    fun putPlayerInDatabaseAsync(mongoPlayer: MongoPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            playerCollection.findOneAndReplace(eq("uuid", mongoPlayer.uuid), mongoPlayer, FindOneAndReplaceOptions().upsert(true))
        })
    }
}

data class MongoPlayer(
    var uuid: String = "",
    var money: Double = 100.0,
    var kills: Double = 0.0,
    var deaths: Double = 0.0
)