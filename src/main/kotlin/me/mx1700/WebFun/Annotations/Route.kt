package me.mx1700.WebFun.Annotations


@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class Route(val path: String,
                       val methods: Array<String> = arrayOf("GET", "POST"),
                       val rules: Array<String> = arrayOf())

@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class Get(val path: String = "", val rules: Array<String> = arrayOf())

@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class Post(val path: String, val rules: Array<String> = arrayOf())

@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class ErrorHandler