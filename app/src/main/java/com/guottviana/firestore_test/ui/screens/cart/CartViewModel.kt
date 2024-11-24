package com.guottviana.firestore_test.ui.screens.cart

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.room.util.query
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.guottviana.firestore_test.domain.model.Address
import com.guottviana.firestore_test.domain.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel: ViewModel() {

    private var _cartItemList = MutableStateFlow<List<CartItem>>(emptyList())
    var cartItemList = _cartItemList.asStateFlow()

    private var _address = MutableStateFlow<Address?>(null)
    var address = _address.asStateFlow()

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val db = Firebase.firestore

    init {
        getCartItemList()
        getAddress()
    }

    private fun getAddress(){

        db.collection("address" )
            .whereEqualTo("user", auth.currentUser?.email)
            .addSnapshotListener { value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null){
                    _address.value = value.documents[0].toObject()
                }
            }
    }

    private fun getCartItemList(){

        db.collection("cartItem" )
            .whereEqualTo("user", auth.currentUser?.email)
            .addSnapshotListener() {value, error ->

                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null){
                    _cartItemList.value = value.toObjects()
                }
            }
    }

    fun incrementQuantity(cartItem: CartItem) {

        db.collection("cartItem")
            .whereEqualTo("flavor", cartItem.flavor)
            .limit(1)
            .get()
            .addOnSuccessListener {

                Log.d("BUSCA", it.documents[0].id)

                db.collection("cartItem")
                    .document(it.documents[0].id)
                    .update("quantity", cartItem.quantity + 1)
                    .addOnSuccessListener {
                        Log.d("document", "UPDATED")
                    }
            }
    }

    fun decrementQuantity(cartItem: CartItem) {

        if (cartItem.quantity > 1) {
            db.collection("cartItem")
                .whereEqualTo("flavor", cartItem.flavor)
                .limit(1)
                .get()
                .addOnSuccessListener {

                    Log.d("BUSCA", it.documents[0].id)

                    db.collection("cartItem")
                        .document(it.documents[0].id)
                        .update("quantity", cartItem.quantity - 1)
                        .addOnSuccessListener {
                            Log.d("document", "UPDATED")
                        }
                }
        }
    }

    fun removeItem(cartItem: CartItem) {
        db.collection("cartItem")
            .whereEqualTo("flavor", cartItem.flavor)
            .get()
            .addOnSuccessListener { it ->
                db.collection("cartItem")
                    .document(it.documents[0].id)
                    .delete()
                    .addOnSuccessListener { Log.d("DELETED", "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w("DELETED ", "Error deleting document", e) }
            }

    }

    fun getTotalPrice(cartItems: List<CartItem>): Double{
        var totalPrice = 0.0

        cartItems.forEach {
            totalPrice += it.price.times(it.quantity)
        }

        return totalPrice
    }

    fun getCartProducts(cartItems: List<CartItem>): String{
        var products = ""

        cartItems.forEach {
            products += ", " + it.flavor
        }

        return products.replaceFirst(", ", "")
    }

    fun createOrder(cartItems: List<CartItem>, address: Address, context: Context){
        val data = hashMapOf(
            "user" to auth.currentUser?.email,
            "address" to address.toString(),
            "price" to getTotalPrice(cartItems),
            "products" to getCartProducts(cartItems)
        )

        db.collection("order")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Order created", Toast.LENGTH_SHORT).show()

                cartItems.forEach {
                    db.collection("cupcakes")
                        .whereEqualTo("flavor", it.flavor)
                        .get()
                        .addOnSuccessListener { value ->
                            db.collection("cupcakes")
                                .document(value.documents[0].id)
                                .update("sold", (value.documents[0].data?.get("sold") as Long) + it.quantity)
                                .addOnSuccessListener {
                                    Log.d("document", "Cupcakes sold increased")
                                }
                        }
                }

               cartItems.forEach {
                   removeItem(it)
               }
            }
    }

}