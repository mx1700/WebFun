package me.mx1700.WebFun

infix fun <T> T?.or(v: T): T {
    return if (this == null) v else this
}

inline fun timer(action: () -> Unit, count: Int) {
    val startTime = System.currentTimeMillis()   //获取开始时间
    for (i in 1..count) {
        action()
    }
    val endTime = System.currentTimeMillis() //获取结束时间
    System.out.println("$count 次运行时间： " + (endTime - startTime) + "ms")
}