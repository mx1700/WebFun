package me.mx1700.WebFun

inline fun timer(action: () -> Unit, count: Int) {
    val startTime = System.currentTimeMillis()   //获取开始时间
    for (i in 1..count) {
        action()
    }
    val endTime = System.currentTimeMillis() //获取结束时间
    System.out.println("$count 次运行时间： " + (endTime - startTime) + "ms")
}

inline fun currentClassName() = Thread.currentThread().stackTrace[1].className;