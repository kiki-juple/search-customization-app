package com.kiki.searchcustomization.util

import java.text.NumberFormat
import java.util.*

fun Double.toRupiah(): String {
    val price = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    price.maximumFractionDigits = 0
    return price.format(this)
}
