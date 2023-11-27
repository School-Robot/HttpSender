package tk.mcsog

import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.isConsole
import java.util.UUID

object Command: CompositeCommand(
    HttpSender, "hsapp", description = "HTTP Sender应用管理器"
) {
    @Description("添加应用")
    @SubCommand("add")
    suspend fun addApp(context: CommandContext, name: String) {
        if (context.sender.isConsole()) {
            val sec: String? = Data.app_re[name]
            if (sec != null){
                context.sender.sendMessage("应用已存在")
                return
            }
            var uuid = UUID.randomUUID()
            while (Data.app[uuid.toString()] != null){
                uuid = UUID.randomUUID()
            }
            Data.app[uuid.toString()] = name
            Data.app_re[name] = uuid.toString()
            context.sender.sendMessage("应用已添加，Secret: $uuid")
            return
        }else{
            context.sender.sendMessage("请在控制台执行")
            return
        }
    }

    @Description("删除应用")
    @SubCommand("del")
    suspend fun delApp(context: CommandContext, name: String) {
        if (context.sender.isConsole()) {
            val sec: String? = Data.app_re[name]
            if (sec == null){
                context.sender.sendMessage("应用不存在")
                return
            }
            Data.app.remove(sec)
            Data.app_re.remove(name)
            context.sender.sendMessage("应用已删除")
            return
        }else{
            context.sender.sendMessage("请在控制台执行")
            return
        }
    }

    @Description("列出所有应用")
    @SubCommand("list")
    suspend fun listApp(context: CommandContext) {
        if (context.sender.isConsole()) {
            var res = "应用：\n"
            for ((k, v) in Data.app_re){
                res+= "名称：$k Secret：$v\n"
            }
            context.sender.sendMessage(res)
            return
        }else{
            context.sender.sendMessage("请在控制台执行")
            return
        }
    }
}