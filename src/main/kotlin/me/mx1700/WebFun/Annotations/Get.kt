package me.mx1700.WebFun.Annotations

@Target(AnnotationTarget.FUNCTION)
@Repeatable
annotation class Get(val path: String = "")