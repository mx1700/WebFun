package me.mx1700.WebFun

import java.io.InputStream
import javax.servlet.http.HttpServletResponse

val defaultHeader = listOf("content-type" to "text/html; charset=utf-8", "PoweredBy" to "WebFun")

data class Response(
        val body: Body<*>,
        val header: List<Pair<String, String>> = defaultHeader,
        val status: Int = HttpServletResponse.SC_OK
) {
    constructor(body: String,
                header: List<Pair<String, String>> = defaultHeader,
                status: Int = HttpServletResponse.SC_OK): this(StringBody(body), header, status)

    constructor(body: InputStream,
                header: List<Pair<String, String>> = defaultHeader,
                status: Int = HttpServletResponse.SC_OK): this(StreamBody(body), header, status)

    fun header(name: String) = header.firstOrNull { it.first.toLowerCase() == name.toLowerCase() }?.second

    interface Body<T> {
        val value: T
    }

    data class StringBody(override val value: String): Body<String>
    data class StreamBody(override val value: InputStream): Body<InputStream>
}