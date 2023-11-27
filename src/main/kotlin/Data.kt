package tk.mcsog

import net.mamoe.mirai.console.data.AutoSavePluginData

object Data : AutoSavePluginData("Data") {
    var app: MutableMap<String, String> = mutableMapOf()
    var app_re: MutableMap<String, String> = mutableMapOf()
}