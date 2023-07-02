package me.mrscopes.beans.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import me.mrscopes.beans.Beans
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bukkit.Bukkit
import java.util.*


class Mongo(url: String) {
    var mongoPlayers: HashMap<UUID, MongoPlayer>
    var playerCollection: MongoCollection<MongoPlayer>
    var database: MongoDatabase
    var client: MongoClient

    init {
        val pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build())
        val codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(url))
            .codecRegistry(codecRegistry)
            .build()

        client = MongoClients.create(clientSettings)
        database = client.getDatabase("beans")
        playerCollection = database.withCodecRegistry(pojoCodecRegistry).getCollection("players", MongoPlayer::class.java)
        mongoPlayers = HashMap()
    }

    fun playerFromDatabase(uuid: UUID): MongoPlayer {
        var mongoPlayer = MongoPlayer()
        mongoPlayer.uuid = uuid.toString()
        try {
            mongoPlayer = playerCollection.find(eq("uuid", uuid.toString())).first()!!
        } catch (e: Exception) {
            println(e)
        }
        return mongoPlayer
    }

    fun putPlayerInDatabase(mongoPlayer: MongoPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(Beans.instance, Runnable { playerCollection.insertOne(mongoPlayer) })
    }
}

data class MongoPlayer (
    var uuid: String = "",
    var money: Double = 100.0
)