package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Route

fun main(args: Array<String>) {
    val app = Application.getApp()
    app.run()
}

@Route(path = "/")
fun index(name: String = "Tom", req: Request): String {
    return "hello, $name, method: ${req.method}"
}

