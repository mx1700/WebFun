package me.mx1700.webfun
import me.mx1700.webfun.Annotations.ErrorHandler
import me.mx1700.webfun.Annotations.Get
import me.mx1700.webfun.Annotations.Route
import java.lang.Exception

fun main(args: Array<String>) {
    val app = Application.create(currentClassName())
    app.run(8080)
}

@Route("/")
fun index(name: String = "Tom", req: Request): String {
    if (name == "") throw Exception("name 不能为空")
    return "hello, $name, method: ${req.method}"
}

@Get("/test[/{id}]", rules = arrayOf("id:\\d+"))
fun test(id: String?): String {
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