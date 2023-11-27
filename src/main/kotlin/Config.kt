package tk.mcsog

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("Config"){
    var host by value("0.0.0.0")
    var port by value(8001)
    var uri by value("/sender")
}