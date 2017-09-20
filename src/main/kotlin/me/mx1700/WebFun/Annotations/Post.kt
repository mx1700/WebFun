package me.mx1700.WebFun.Annotations

@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class Post(val path: String)