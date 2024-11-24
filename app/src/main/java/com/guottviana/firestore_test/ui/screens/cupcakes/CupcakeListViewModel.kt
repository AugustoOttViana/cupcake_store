package com.guottviana.firestore_test.ui.screens.cupcakes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.guottviana.firestore_test.domain.model.Cupcake
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CupcakeListViewModel: ViewModel() {

    private var _cupcakeList = MutableStateFlow<List<Cupcake>>(emptyList())
    var cupcakeList = _cupcakeList.asStateFlow()

    private val _filteredProducts = MutableStateFlow<List<Cupcake>>(emptyList())
    var filteredProducts = _filteredProducts.asStateFlow()



//    private var _image = MutableStateFlow<ImageBitmap?>(null)
//    var image = _image.asStateFlow()

    private val db = Firebase.firestore

    init {
        getCupcakeList()
    }

    private fun getCupcakeList(){

        db.collection("cupcakes")
            //addSnapshotListener atualiza a lista caso a mesma seja alterada na base de dados
            .addSnapshotListener {value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null){
                    _cupcakeList.value = value.toObjects()

                }
            }
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