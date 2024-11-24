package com.guottviana.firestore_test.utils

import java.text.NumberFormat
import java.util.Currency

object CurrencyUtils {
    fun formatPrice(price: Double, currency: String = "BRL"): String {
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance(currency)
        return format.format(price)
    }
}