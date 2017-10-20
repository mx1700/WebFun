package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.ErrorHandler
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Route
import java.lang.Exception

fun main(args: Array<String>) {
    val app = Application.create()
    app.run()
}

@Route("/")
fun index(name: String = "Tom", req: Request): String {
    throw Exception("aaa")
    return "hello, $name, method: ${req.method}"
}

@Get("/test")
fun test(): String {
    return "test111"
}

@Get("/test1[/{id}]", rules = arrayOf("id:\\d+"))
fun test1(id: String = "ididid"): String {
    return "test1:${id}"
}

fun test2(id: String): String {
    return "test2:$id"
}

@ErrorHandler
fun error(err: Exception): String {
    return "Error! " + err.message
}