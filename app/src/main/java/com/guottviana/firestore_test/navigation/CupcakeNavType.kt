package com.guottviana.firestore_test.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.guottviana.firestore_test.domain.model.Cupcake
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val cupcakeNavType = object : NavType<Cupcake>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Cupcake? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Cupcake::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }


    override fun parseValue(value: String): Cupcake = Json.decodeFromString(value)

    override fun put(bundle: Bundle, key: String, value: Cupcake) = bundle.putParcelable(key, value)


    override fun serializeAsValue(value: Cupcake): String = Uri.encode(Json.encodeToString<Cupcake>(value))

    override val name: String = Cupcake::class.java.name
}