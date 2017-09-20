package me.mx1700.WebFun

import javax.servlet.http.HttpServletResponse

data class Response(
        val header: List<Pair<String, String>>,
        val body: String,
        val status: Int = HttpServletResponse.SC_OK
) {
    fun header(name: String) = header.firstOrNull { it.first.toLowerCase() == name.toLowerCase() }?.second
}