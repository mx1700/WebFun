package me.mx1700.WebFun

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler
import java.io.IOException
import javax.servlet.MultipartConfigElement
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Part

class ServletHandler(
        private val run: (request: me.mx1700.WebFun.Request) -> Response
) : AbstractHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun handle(target: String,
                        baseRequest: Request,
                        request: HttpServletRequest,
                        response: HttpServletResponse) {

        val isMultipart = request.contentType.startsWith("multipart/form-data");
        if (isMultipart) {
            request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MultipartConfigElement(
                    System.getProperty("java.io.tmpdir")))
        }
        response.status = HttpServletResponse.SC_NOT_FOUND

        val req = me.mx1700.WebFun.Request(
                request.pathInfo,
                request.method,
                request.remoteHost,
                request.headerNames.asSequence().map { it to request.getHeader(it) }.toList(),
                request.parameterNames.asSequence().map { it to request.getParameter(it) }.toList(),
                request.parameterNames.asSequence().map { it to request.getParameter(it) }.toList(),
                if(isMultipart) request.parts.toList() else listOf<Part>(),
                null   //TODO:未完成
        )
        val resp = run(req)
        response.contentType = resp.header("contentType") ?: "text/html; charset=utf-8"
        response.status = resp.status
        response.writer.print(resp.body)
        baseRequest.isHandled = true
    }
}

fun Application.run(port: Int = 8080) {
    val server = Server(port)
    val handle = ServletHandler(this::handle)
    server.handler = handle
    server.start()
    server.dumpStdErr()
    server.join()
}