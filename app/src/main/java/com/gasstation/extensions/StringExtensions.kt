package com.gasstation.extensions

import java.math.RoundingMode
import java.text.DecimalFormat

fun String.numberFormat(): String {
    val df = DecimalFormat("#,###")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this.toInt())
}

fun Double.distanceFormat(): String {
    return "%.1f".format(this / 1000)
}