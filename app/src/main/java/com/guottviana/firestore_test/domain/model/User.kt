package com.guottviana.firestore_test.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(
    val userName: String = "",
    val email: String = "",
    val type: String = "",
    val typeId: Int = 0
) : Parcelable
