package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.Get
import me.mx1700.WebFun.Annotations.Post
import me.mx1700.WebFun.Annotations.Route
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletResponse
import javax.xml.bind.TypeConstraintException
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.kotlinFunction

open class Application(routeClass: String) {

    private val router: Router = Router { scanClass(routeClass) }

    companion object {
        inline fun getApp(): Application {
            val className = currentClassName()
            return Application(className)
        }
    }

    fun handle(req: Request): Response {
        val match = router.matches(req.method, req.path).firstOrNull()
                ?: return Response("Not found.", listOf(), HttpServletResponse.SC_NOT_FOUND)

        val a = match.info.action
        val aaa = match.info.action.kotlinFunction
        val kFun = match.info.action.kotlinFunction!!
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
            is String -> Response(res)
            else -> TODO()
        }
    }

    fun testRoute(method: String, path: String, query: List<Pair<String, String>>) {
        val res = handle(Request(path, method, "", listOf(), query, listOf(), listOf(), object :ServletInputStream(){
            override fun isReady(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isFinished(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun read(): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun setReadListener(readListener: ReadListener?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }))
        println(res)
    }
}

