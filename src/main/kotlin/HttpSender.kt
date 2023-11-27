package tk.mcsog

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.utils.info

object HttpSender : KotlinPlugin(
    JvmPluginDescription(
        id = "tk.mcsog.HttpSender",
        name = "HttpSender",
        version = "0.1.0",
    ) {

        author("f00001111")
    }
) {
    var server : ApplicationEngine? = null
    override fun onEnable() {
        logger.info { "Loading Config" }
        Config.reload()
        Data.reload()
        Command.register()
        logger.info { "Starting Server" }
        server = embeddedServer(Netty, host = Config.host, port = Config.port) {
            sender()
        }.start(wait = false)
        logger.info { "Server Started" }
    }

    private fun Application.sender() {
        install(ContentNegotiation) {
            json()
        }
        routing {
            post(Config.uri) {
                val text: Message = call.receive<Message>()
                val name: String? = Data.app[text.Secret]
                if (name == null) {
                    call.respondText("App not found")
                    return@post
                }
                when (text.Type){
                    "Group" -> {
                        val bot: Bot? = Bot.findInstance(text.Bot)
                        if (bot == null) {
                            call.respondText("Bot not found")
                            return@post
                        }
                        val group: Group? = bot.getGroup(text.Number)
                        if (group == null) {
                            call.respondText("Group not found")
                            return@post
                        }
                        group.sendMessage("["+Data.app[text.Secret]+"] "+text.Message)
                        call.respondText { "Success" }
                        return@post
                    }

                    "Friend" -> {
                        val bot: Bot? = Bot.findInstance(text.Bot)
                        if (bot == null) {
                            call.respondText("Bot not found")
                            return@post
                        }
                        val friend: Friend? = bot.getFriend(text.Number)
                        if (friend == null) {
                            call.respondText("Friend not found")
                            return@post
                        }
                        friend.sendMessage("["+Data.app[text.Secret]+"] "+text.Message)
                        call.respondText { "Success" }
                        return@post
                    }

                    else -> {
                        call.respondText("Type not found")
                        return@post
                    }
                }
            }
        }
    }

    override fun onDisable() {
        logger.info { "Stopping Server" }
        if (server != null) {
            server!!.stop(1000, 1000)
        }
        logger.info { "Server stopped" }
        Config.save()
        Data.save()
    }
}