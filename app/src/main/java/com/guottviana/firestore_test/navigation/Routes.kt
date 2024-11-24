package com.guottviana.firestore_test.navigation

import com.guottviana.firestore_test.domain.model.Cupcake
import com.guottviana.firestore_test.domain.model.User
import kotlinx.serialization.Serializable

object Routes {
    var homeScreen = "home"
    var loginScreen = "login"
    var signupScreen = "signup"
    var addCupcakeScreen = "add"
    var cartScreen = "cart"
    var confirmationScreen = "confirm"
    var paymentScreen = "payment"
    var creditScreen = "credit"
    var debitScreen = "debit"
    var administration = "admin"
    var userScreen = "user"
    var addressScreen = "address"
    var orderScreen = "orders"

    @Serializable
    data class UserEditScreen(val user:User)

    @Serializable
    data class CupcakeScreen(val cupcake: Cupcake)



}