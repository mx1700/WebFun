package me.mx1700.WebFun.RouterMatcher

import me.mx1700.WebFun.RouteMatch
import java.util.regex.Pattern
import kotlin.coroutines.experimental.buildSequence

/**
 * path 匹配 url
 * /index
 * /index[/]
 * /index[/{id}]    "id" to "\\d+"
 * /home/{aaa}
 */
class PathMatcher<T>(private val routes: List<RouterItem<T>>) {
    private val regexMatcher: UrlRegexMatcher<T>

    init {
        regexMatcher = UrlRegexMatcher(routes.map {
            val (p, params) = buildRegex(it.path, it.rules)
            UrlRegexMatcher.RouterItem(p, params, it.info)
        })
    }

    data class RouterItem<T>(
            /**
             * path 规则
             * 只支持 {PARAM} 和 []
             * {} 表示参数，[] 表示可选内容
             *
             * 实例
             * /index           匹配 "/index"
             * /index[/]        匹配 "/index" 和 "/index/"
             * /users/{id}      匹配 "/users/PARAM"
             * /users[/{id}]    匹配 "/users" 和 "/users/PARAM"
             * /u/{id}  "id" to "\\d+"  匹配 /u/123
             */
            val path: String,
            val rules: Map<String, String>,
            val info: T
    )

    private fun buildRegex(url: String, rules: Map<String, String>): Pair<Pattern, List<String>> {
        val params = mutableListOf<String>()
        val urlRegex = StringBuffer()

        val pattern = "\\{(\\w+\\??)\\}";
        val r = Pattern.compile(pattern);
        val m = r.matcher(url.replace("[", "(").replace("]", ")?"))

        while (m.find()) {
            val param = m.group(1)
            params.add(param)
            // \ 和 $ 在 replace 时是特殊字符，需要转义
            val rule = (rules[param] ?: "[^/.]+").replace("\\", "\\\\").replace("$", "\\$")
            m.appendReplacement(urlRegex, "(?<$1>$rule)")
        }
        m.appendTail(urlRegex)
        return Pattern.compile("^$urlRegex$") to params
    }

    fun matches(url: String): Sequence<RouteMatch<T>> {
        return regexMatcher.matches(url)
    }
}

/**
 * 正则匹配 url
 */
private class UrlRegexMatcher<T>(private val regexRoutes: List<RouterItem<T>>) {

    data class RouterItem<T>(
            val pattern: Pattern,
            val paramNames: List<String>,
            val info: T
    )

    fun matches(url: String): Sequence<RouteMatch<T>> {
        return buildSequence {
            for ((p, paramNames, info) in regexRoutes) {
                val m = p.matcher (url);
                if (m.find()) {
                    yield(RouteMatch(paramNames.map { it to m.group(it) }.toMap(), info))
                }
            }
        }
    }
}