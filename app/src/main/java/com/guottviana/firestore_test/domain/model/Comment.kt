package com.guottviana.firestore_test.domain.model

import com.google.firebase.Timestamp

data class Comment(
    val date: Timestamp? = null,
    val text: String = "",
    val userName: String = "",
    val user: String = "",
    val cupcakeFlavor: String = ""
)
