package com.guottviana.firestore_test.domain.model

import android.media.Image
import android.os.Parcelable
import androidx.compose.ui.graphics.painter.Painter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Cupcake(
    val id: Int = 0,
    val flavor: String = "",
    val image: String = "",
    val description: String = "",
    val price : Double = 0.0,
    val amount: Int = 0,
    val sold: Int = 0
) : Parcelable
