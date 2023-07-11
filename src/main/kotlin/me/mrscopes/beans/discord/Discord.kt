package me.mrscopes.beans.discord

import me.mrscopes.beans.Beans
import me.mrscopes.beans.discord.listeners.ChatListener
import me.mrscopes.beans.events.Events
import me.mrscopes.beans.utilities.Utilities.broadcastToAdmins
import me.mrscopes.beans.utilities.Utilities.broadcastToStaff
import me.mrscopes.beans.utilities.color
import me.mrscopes.beans.utilities.plainText
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

class Discord(plugin: Beans) : ListenerAdapter() {
    val client: JDA
    var beans: Beans
    val serverChat: TextChannel
    val staffChat: TextChannel
    val adminChat: TextChannel

    init {
        val tempClient = JDABuilder.createDefault(plugin.config.getString("discord token")).addEventListeners(this).build()
        tempClient.awaitReady()
        client = tempClient
        beans = plugin

        serverChat = channelFromConfig("server chat id")!!
        staffChat = channelFromConfig("staff chat id")!!
        adminChat = channelFromConfig("admin chat id")!!

        Events.registerEvent(ChatListener())
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return
        if (event.channelType != ChannelType.TEXT) return
        val message = event.message
        if (message.contentRaw.isEmpty()) return
        val content = message.contentStripped
        val finalContent = if (content.length >= 70) content.substring(0, 70) + "..." else content
        val displayName = displayNameFromMember(message.member!!).plainText()
        val channel = message.channel
        if (channel == serverChat) {
            Bukkit.broadcast("&b[Discord] &7$displayName&8: &f$finalContent".color())
        } else if (channel == staffChat) {
            broadcastToStaff("&4&l! &6[Staff Chat] &e$displayName&8: &f$finalContent".color())
        } else if (channel == adminChat) {
            broadcastToAdmins("&4&l! &4[Admin Chat] &c$displayName&8: &f$finalContent".color())
        }
    }

    private fun displayNameFromMember(member: Member): Component {
        val highestRole = member.roles[0]
        return if (highestRole != null) MiniMessage.miniMessage().deserialize("<#${Integer.toHexString(highestRole.color!!.rgb).substring(2)}>${highestRole.name} <white>${member.user.asTag}")
               else Component.text(member.user.asTag)
    }

    private fun channelFromConfig(value: String): TextChannel? {
        val channel = client.getTextChannelById(beans.config.getString(value)!!)
        if (channel == null) {
            beans.logger.severe("Cannot get channel for '$value'")
        }
        return channel
    }
}
