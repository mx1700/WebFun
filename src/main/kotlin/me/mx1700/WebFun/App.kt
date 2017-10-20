package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.ErrorHandler
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Route
import java.lang.Exception

fun main(args: Array<String>) {
    val app = Application.create(currentClassName())
    app.run(8080)
}

@Route("/")
fun index(name: String = "Tom", req: Request): String {
    throw Exception("aaa")
    return "hello, $name, method: ${req.method}"
}

@Get("/test1[/{id}]", rules = arrayOf("id:\\d+"))
fun test1(id: String = "my id"): String {
    return "test1:$id"
}

@ErrorHandler
fun err(err: RouteNotFindException): String {
    return "404"
}

@ErrorHandler
fun error(err: Exception): String {
    return "Error! " + err.message
}