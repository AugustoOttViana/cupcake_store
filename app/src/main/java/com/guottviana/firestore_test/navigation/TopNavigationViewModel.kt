package com.guottviana.firestore_test.navigation

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.guottviana.firestore_test.domain.model.CartItem
import com.guottviana.firestore_test.domain.model.Cupcake
import com.guottviana.firestore_test.ui.screens.auth.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TopNavigationViewModel: ViewModel() {

    private val db = Firebase.firestore

    private val _filteredProducts = MutableStateFlow<List<Cupcake>>(emptyList())
    var filteredProducts = _filteredProducts.asStateFlow()

    private var _cupcakes = MutableStateFlow<List<Cupcake>>(emptyList())
    var cupcakeList = _cupcakes.asStateFlow()

    private var _color = MutableStateFlow(Color.White)
    var color = _color.asStateFlow()


    init {
        getColor()
    }
    private fun getColor() {

        var colorOption: String

        db.collection("colorOption" )
            .document("color")
            .addSnapshotListener{value, error ->

                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null){
                    colorOption = value.get("color").toString()
                    Log.d("COLOR", value.get("color").toString())

                    when (colorOption) {
                        "Red" -> {
                            _color.value = Color.Red
                        }
                        "Blue" -> {
                            _color.value = Color.Blue
                        }
                        else -> {
                            _color.value = Color.White
                        }
                    }
                }
            }

    }

    fun getList(){
        db.collection("cupcakes")
            .get()
            .addOnSuccessListener {
                _cupcakes.value = it.toObjects()
                Log.d("TESTE", cupcakeList.value.size.toString())
            }
    }

    fun clearList(){
        _cupcakes.value = emptyList()
    }

    fun searchProducts(text: String) {
        _filteredProducts.value = cupcakeList.value.filter {
            it.flavor
                .contains(
                    text,
                    ignoreCase = true
                ) || it.description
                .contains(
                    text,
                    ignoreCase = true
                )
        }
    }

}