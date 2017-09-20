package me.mx1700.WebFun

import javax.servlet.http.Part

data class Request(
        val path: String,
        val method: String,
        val host: String,
        val header: List<Pair<String, String>>,
        val query: List<Pair<String, String>>,
        val form: List<Pair<String, String>>,
        val part: List<Part>,
        val body: String?
) {
    fun header(name: String) = header.firstOrNull { it.first.toLowerCase() == name.toLowerCase() }?.second
    fun query(name: String) = query.firstOrNull { it.first.toLowerCase() == name.toLowerCase() }?.second
    fun form(name: String) = form.firstOrNull { it.first.toLowerCase() == name.toLowerCase() }?.second
}