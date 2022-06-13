package com.iw.gizlysoz.Extension

fun Int.pow(x: Int): Int = (2..x).fold(this) { r, _ -> r * this }