package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Post
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import org.springframework.core.LocalVariableTableParameterNameDiscoverer
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

@Configuration
@ComponentScan
open class Application {

    lateinit var routes: List<RouteInfo>

    companion object {
        val context = AnnotationConfigApplicationContext(Application::class.java)
        init {
            context.beanFactory.registerSingleton("test", "hello123123")
        }

        fun getApp(): Application {
            return context.getBean(Application::class.java)
        }
    }

    inline fun run(): Application {
        //context.beanDefinitionNames.forEach { println(it) }
        //println(context.getBean("test"))
        val className = Thread.currentThread().stackTrace[1].className;
        routes = scanRoutes(className)
        routes.forEach { println(it) }
        return this
    }

    fun testRoute(method: String, path: String, query: Map<String, Any>) {
        val action = routes.asSequence().filter { it.method == method && it.path == path }.first().action
        val kFun = action.kotlinFunction!!
        val params = kFun.parameters.map {
            val type = it.type.javaType.typeName.split('.').last().decapitalize()
            when {
                context.containsBean(type) -> it to context.getBean(type)
                query.containsKey(it.name) -> it to query[it.name]
                else -> null
            }
        }.filterNotNull().toMap()

        //TODO:基础类型转换，数组支持
//        params.forEach({k, v -> println("${k.name}: $v")})
//        println("----------------")
        val r = kFun.callBy(params)
        println(r)
    }

    fun scanRoutes(className: String): List<RouteInfo> {
        val routes = arrayListOf<RouteInfo>()
        val clazz = Class.forName(className)
        clazz.methods.filter { Modifier.isStatic(it.modifiers) }.map {
            val method = it
//            method.parameters.forEach { println(method.name + ":" + it.name) }
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

data class RouteInfo(val method: String,val path: String,val action: Method)