package me.mx1700.webfun.RouterMatcher

class ErrorMatcher<T>(private val routes: List<RouteItem<T>>) {

    data class RouteItem<T>(
            val clazz: Class<*>,
            val info: T
    )

    fun matches(err: Exception): Sequence<T> {
        return routes.asSequence().filter { it.clazz == err.javaClass }.map { it.info }
    }
}