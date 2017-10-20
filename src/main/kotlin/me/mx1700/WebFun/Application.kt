package me.mx1700.WebFun
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletResponse
import javax.xml.bind.TypeConstraintException
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.kotlinFunction

open class Application(routeClass: String) {

    private val router: Router = Router {
        scanClass(routeClass) +
                RouteConfig("GET", "/test2/{id}", ::test2.javaMethod!!, mapOf("id" to "\\d+"))
    }

    companion object {
        inline fun create(): Application {
            val className = currentClassName()
            return Application(className)
        }
    }

    fun handle(req: Request): Response {
        val matches = router.matches(req.method, req.path)
        val res = try {
            callAction(matches, req)
        } catch (err: Exception) {
            errorHandle(matches, req, err)
        }

        return when(res) {
            is Response -> res
            is String -> Response(res)
            else -> TODO()
        }
    }

    private fun callAction(matches: Sequence<RouteMatch<RouteConfig>>, req: Request): Any? {
        val routeMatch = matches.firstOrNull { it.info.method == req.method && !it.info.isErrorHandler } ?:
                throw ClassNotFoundException("aaaaaaaaaaaaa")

        val kFun = routeMatch.info.action.kotlinFunction!!
        val params = kFun.parameters.mapNotNull {
            //val type = it.type.javaType.typeName.split('.').last().decapitalize()
            val value = req.query(it.name!!)
            when {
                it.type.javaType.typeName == Request::class.java.name -> it to req
                !it.name.isNullOrBlank() &&
                        routeMatch.parameters.containsKey(it.name!!) &&
                        !routeMatch.parameters[it.name!!].isNullOrEmpty()
                            -> it to routeMatch.parameters[it.name!!]

                value != null -> it to value    //匹配成功
                it.isOptional -> null           //最后确认选填类型，可以不匹配
                else -> throw TypeConstraintException("路由参数 ${it.name} 未匹配")
            }
        }.toMap()

        return kFun.callBy(params)
    }

    private fun errorHandle(matches: Sequence<RouteMatch<RouteConfig>>, req: Request, err: Exception): Any? {
        val routeMatch = matches.firstOrNull {
            it.info.isErrorHandler && it.info.exception!!.isInstance(err)
        }
        if (routeMatch == null) {
            throw err
        }

        val kFun = routeMatch.info.action.kotlinFunction!!
        val params = kFun.parameters.mapNotNull {
            when {
                it.type.javaType.typeName == Request::class.java.name -> it to req
                it.type.javaType.typeName == err.javaClass.typeName -> it to err
                it.isOptional -> null           //最后确认选填类型，可以不匹配
                else -> throw TypeConstraintException("路由参数 ${it.name} 未匹配")
            }
        }.toMap()

        return kFun.callBy(params)
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

