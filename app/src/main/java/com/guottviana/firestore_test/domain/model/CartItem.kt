package com.guottviana.firestore_test.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val price: Double = 0.0,
    val image: String? = null,
    val quantity: Int = 0,
    val flavor: String = "",
    val user: String = ""
)