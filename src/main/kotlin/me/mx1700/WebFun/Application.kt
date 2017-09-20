package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Post
import me.mx1700.WebFun.Annotations.Route
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import javax.servlet.http.HttpServletResponse
import javax.xml.bind.TypeConstraintException
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.kotlinFunction

open class Application(routeClass: String) {

    var routes: List<RouteInfo>

    companion object {
        inline fun getApp(): Application {
            val className = Thread.currentThread().stackTrace[1].className;
            return Application(className)
        }
    }

    init {
        routes = scanRoutes(routeClass)
        routes.forEach { println(it) }
    }

    fun handle(req: Request): Response {
        val action = routes.asSequence().filter { it.method == req.method && it.path == req.path }
                .firstOrNull()?.action ?: return Response(listOf(), "Not found.", HttpServletResponse.SC_NOT_FOUND)
        val kFun = action.kotlinFunction!!
        val params = kFun.parameters.mapNotNull {
            //val type = it.type.javaType.typeName.split('.').last().decapitalize()
            val value = req.query(it.name!!)
            when {
                it.type.javaType.typeName == Request::class.java.name -> it to req
                value != null -> it to value    //匹配成功
                it.isOptional -> null           //最后确认选填类型，可以不匹配
                else -> throw TypeConstraintException("路由参数 ${it.name} 未匹配")
            }
        }.toMap()
        val res = kFun.callBy(params)
        return when(res) {
            is Response -> res
            is String -> Response(listOf(), res)
            else -> TODO()
        }
    }

    fun testRoute(method: String, path: String, query: List<Pair<String, String>>) {
        val res = handle(Request(path, method, "", listOf(), query, listOf(), listOf(), null))
        println(res)
    }

    private fun scanRoutes(className: String): List<RouteInfo> {
        val clazz = Class.forName(className)
        val routes = clazz.methods.filter { Modifier.isStatic(it.modifiers) }.fold(mutableListOf<RouteInfo>()) { r, action ->
            action.annotations.forEach {
                when (it) {
                    is Get -> r.add(RouteInfo("GET", it.path, action))
                    is Post -> r.add(RouteInfo("POST", it.path, action))
                    is Route -> it.methods.map { m -> r.add(RouteInfo(m, it.path, action)) }
//                    else -> null
                }
            }
            r
        }
        return routes
    }
}

data class RouteInfo(val method: String,val path: String,val action: Method)