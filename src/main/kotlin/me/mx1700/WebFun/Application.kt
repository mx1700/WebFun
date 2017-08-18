package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Post
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import org.springframework.core.LocalVariableTableParameterNameDiscoverer
import org.springframework.context.annotation.AnnotationConfigApplicationContext

@Configuration
@ComponentScan
open class Application {

    lateinit var routes: List<RouteInfo>
    val parameterNameDiscoverer = LocalVariableTableParameterNameDiscoverer()

    companion object {
        var context = AnnotationConfigApplicationContext(Application::class.java)
    }

    inline fun run() {
        val className = Thread.currentThread().stackTrace[1].className;
        routes = getRoutes(className)
        routes.forEach { println(it) }
    }

    fun testRoute(method: String, path: String) {
        val methodInfo = routes.asSequence().filter { it.method == method && it.path == path }.first().funInfo
        parameterNameDiscoverer.getParameterNames(methodInfo).forEach { println(it) }
    }

    fun getRoutes(className: String): List<RouteInfo> {
        println(className)
        val routes = arrayListOf<RouteInfo>()
        val clazz = Class.forName(className)
        clazz.methods.filter { Modifier.isStatic(it.modifiers) }.map {
            val method = it
            method.parameters.forEach { println(method.name + ":" + it.name) }
            routes.addAll(it.annotations.map {
                when (it) {
                    is Get -> RouteInfo("GET", it.path, method)
                    is Post -> RouteInfo("POST", it.path, method)
                    else -> null
                }
            }.filterNotNull())
        }
        return routes
    }
}

data class RouteInfo(val method: String,val path: String,val funInfo: Method)