package me.mx1700.WebFun

import me.mx1700.WebFun.Annotations.ErrorHandler
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Post
import me.mx1700.WebFun.Annotations.Route
import me.mx1700.WebFun.RouterMatcher.PathMatcher
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.kotlinFunction

class Router(private val routes: List<RouteConfig>) {

    constructor(init: () -> List<RouteConfig>): this(init())
//    private val routes = init(this)
    private val matcher: PathMatcher<RouteConfig> = PathMatcher(routes.map {
        PathMatcher.RouterItem(it.path, it.rules, it)
    })

    fun matches(method: String, path: String): Sequence<RouteMatch<RouteConfig>> {
        return matcher.matches(path).filter { it.info.method == method }
    }

    val errorRouters by lazy { routes.filter { it.isErrorHandler } }
}

data class RouteConfig(
        val method: String,
        val path: String,
        val action: Method,
        val rules: Map<String, String>,
        val exception: Class<*>? = null
) {
    constructor(exception: Class<*>, action: Method): this("", "", action, mapOf(), exception)
    val isErrorHandler = exception != null
}

data class RouteMatch<T>(
        val parameters: Map<String, String>,
        val info: T
)

fun scanClass(className: String): List<RouteConfig> {
    val clazz = Class.forName(className)
    return getClassFunctions(clazz)
            .fold(mutableListOf()) { r, action ->
                action.annotations.forEach {
                    when (it) {
                        is Get -> r.add(RouteConfig("GET", it.path, action, getRulesMap(it.rules)))
                        is Post -> r.add(RouteConfig("POST", it.path, action, getRulesMap(it.rules)))
                        is Route -> it.methods.map { m -> r.add(RouteConfig(m, it.path, action, getRulesMap(it.rules))) }
                        is ErrorHandler -> r.add(RouteConfig(getErrorHandlerExceptionClass(action), action))
                    }
                }
                r
            }
}

private fun getErrorHandlerExceptionClass(action: Method): Class<*> {
    val params = action.kotlinFunction!!.parameters
    if (params.count() == 0) {
        return Exception::class.java
    }
    val className = action.kotlinFunction!!.parameters[0].type.javaType.typeName
    val clazz = Class.forName(className)
    if (Exception::class.java.isAssignableFrom(clazz)) {
        return clazz
    }
    throw Exception("ErrorHandler 第一个参数必须是 Exception 的子类")
}

private fun getClassFunctions(clazz: Class<*>) =
        clazz.methods.filter { Modifier.isStatic(it.modifiers) && !it.name.contains('$') }

private fun getRulesMap(arr: Array<String>): Map<String, String> {
    return arr.map { it.split(':', limit = 2) }.map { it[0] to it[1] }.toMap()
}
