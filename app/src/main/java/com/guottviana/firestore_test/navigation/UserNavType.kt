package com.guottviana.firestore_test.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.guottviana.firestore_test.domain.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val userNavType = object : NavType<User>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): User? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, User::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }


    override fun parseValue(value: String): User = Json.decodeFromString(value)

    override fun put(bundle: Bundle, key: String, value: User) = bundle.putParcelable(key, value)


    override fun serializeAsValue(value: User): String = Uri.encode(Json.encodeToString<User>(value))

    override val name: String = User::class.java.name
}