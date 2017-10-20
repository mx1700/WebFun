package me.mx1700.WebFun

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler
import java.io.IOException
import java.io.InputStream
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

        val isMultipart = request.contentType != null && request.contentType.startsWith("multipart/form-data");
        if (isMultipart) {
            request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MultipartConfigElement(
                    System.getProperty("java.io.tmpdir")))
        }

        val req = me.mx1700.WebFun.Request(
                request.pathInfo,
                request.method,
                request.remoteHost,
                request.headerNames.asSequence().map { it to request.getHeader(it) }.toList(),
                request.parameterNames.asSequence().map { it to request.getParameter(it) }.toList(),
                request.parameterNames.asSequence().map { it to request.getParameter(it) }.toList(),
                if(isMultipart) request.parts.toList() else listOf<Part>(),
                request.inputStream
        )

        val resp = run(req)
        resp.header.forEach({ (name, value) -> response.addHeader(name, value) })
        response.status = resp.status
        val value = resp.body.value;
        when (value) {
            is String -> response.writer.print(value)
            is InputStream -> value.copyTo(response.outputStream)
        }

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