package me.mx1700.WebFun.Annotations

//TODO:尚未使用
@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class Route(val path: String, val methods: Array<String> = arrayOf("GET", "POST"))

@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class Get(val path: String = "")

@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class Post(val path: String)