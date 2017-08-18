package me.mx1700.WebFun

import me.mx1700.WebFun.Annotations.Get

fun main(args: Array<String>) {
    Application().run()
    ///Scanners.testRoute("GET", "/")
}

@Get(path = "/")
fun index(name: String = "hello"): String {
    return "hello world"
}