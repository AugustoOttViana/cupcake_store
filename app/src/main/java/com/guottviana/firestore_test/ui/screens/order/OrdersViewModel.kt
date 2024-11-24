package com.guottviana.firestore_test.ui.screens.order

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.guottviana.firestore_test.domain.model.Cupcake
import com.guottviana.firestore_test.domain.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrdersViewModel: ViewModel() {

    private var _orders = MutableStateFlow<List<Order>>(emptyList())
    var orders = _orders.asStateFlow()


    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    init {
        getOrders()
    }

    private fun getOrders(){
        db.collection("order")
            .whereEqualTo("user", auth.currentUser?.email)
            .addSnapshotListener { value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null) {
                    _orders.value = value.toObjects()
                }
            }
    }
}