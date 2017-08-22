package me.mx1700.WebFun
import me.mx1700.WebFun.Annotations.Get
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.handler.gzip.GzipHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

fun main(args: Array<String>) {
    val app = Application.getApp().run()
//    app.testRoute("GET", "/", mapOf("name" to "hahaha"))
    val server = Server(8080)
    val handle = HttpHandler({ req, res -> })
    server.handler = handle
    server.start()
    server.dumpStdErr()
    server.join()
}

class HttpHandler(private val run: (request: HttpServletRequest,
                                    response: HttpServletResponse) -> Unit) : AbstractHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun handle(target: String,
                        baseRequest: Request,
                        request: HttpServletRequest,
                        response: HttpServletResponse) {
        val ctx = request.startAsync()
        response.contentType = "text/html; charset=utf-8"
        response.status = HttpServletResponse.SC_NOT_FOUND
        run(request, response)

        baseRequest.isHandled = true
    }
}

@Get(path = "/")
fun index(name: String): String {
    return "hello, $name"
}




//inline fun timer(action: () -> Unit, count: Int) {
//    val startTime = System.currentTimeMillis()   //获取开始时间
//    for (i in 1..count) {
//        action()
//    }
//    val endTime = System.currentTimeMillis() //获取结束时间
//    System.out.println("$count 次运行时间： " + (endTime - startTime) + "ms")
//}