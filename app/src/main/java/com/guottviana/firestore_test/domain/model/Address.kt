package com.guottviana.firestore_test.domain.model

data class Address(
    val user: String = "",
    val street: String = "",
    val country: String = "",
    val city: String = "",
    val state: String = "",
    val number: Int = 0
){
    override fun toString(): String {
        return "$street $number, $city, $state, $country"
    }
}
