package me.mx1700.WebFun

import javax.servlet.http.HttpServletResponse

data class Response(
        val body: String,
        val header: List<Pair<String, String>> = listOf("content-type" to "text/html; charset=utf-8", "PoweredBy" to "WebFun"),
        val status: Int = HttpServletResponse.SC_OK
) {
    fun header(name: String) = header.firstOrNull { it.first.toLowerCase() == name.toLowerCase() }?.second
}