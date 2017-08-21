package me.mx1700.WebFun

import me.mx1700.WebFun.Annotations.Get

fun main(args: Array<String>) {
    val app = Application.getApp().run()
    app.testRoute("GET", "/", mapOf("name" to "hahaha"))
}

@Get(path = "/")
fun index(name: String = "Tom", id: Int, bean: TestBean): String {
    bean.hello()
    return "hello, $name; id: $id"
}




//inline fun timer(action: () -> Unit, count: Int) {
//    val startTime = System.currentTimeMillis()   //获取开始时间
//    for (i in 1..count) {
//        action()
//    }
//    val endTime = System.currentTimeMillis() //获取结束时间
//    System.out.println("$count 次运行时间： " + (endTime - startTime) + "ms")
//}