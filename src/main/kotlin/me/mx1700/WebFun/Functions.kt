package me.mx1700.WebFun

infix fun <T> T?.or(v: T): T {
    return if (this == null) v else this
}