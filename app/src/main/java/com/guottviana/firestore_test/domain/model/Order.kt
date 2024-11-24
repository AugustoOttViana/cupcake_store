package com.guottviana.firestore_test.domain.model

data class Order(
    val user: String = "",
    val address: String = "",
    val price: Double = 0.0,
    val products: String = ""
)
