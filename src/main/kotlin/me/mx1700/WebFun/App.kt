package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Route
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.kotlinFunction

fun main(args: Array<String>) {
//    val m = PathMatcher<String>(
//            listOf(
//                    PathMatcher.RouterItem("/", mapOf(), "index"),
//                    PathMatcher.RouterItem("/home/{id}", mapOf("id" to "\\d+"), "home")
//            )
//    )
//    m.matches("/home/").forEach { println(it) }

    val app = Application.getApp()
    app.run()

//    val clazz = Class.forName(currentClassName())
//    clazz.declaredMethods.forEach { println(it.kotlinFunction) }
}

@Route("/")
fun index(name: String = "Tom", req: Request): String {
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