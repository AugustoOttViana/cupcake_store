package com.guottviana.firestore_test.ui.screens.addCupcake

import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import java.net.URL

class AddCupcakeViewModel: ViewModel() {

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val db = Firebase.firestore

    fun addCupcake(
        flavor: String,
        stock: String,
        price: String,
        description: String,
        file: DocumentFile?
    ){

        val fileUri = file?.uri
        val riversRef = storageRef.child("images/${file?.name}")

        val uploadTask = riversRef.putFile(fileUri as Uri)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->

            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                Log.d("DOWNLOAD", it.toString())

                val newCupcake = hashMapOf(
                    "flavor" to flavor,
                    "amount" to stock.toInt(),
                    "price" to price.toDouble(),
                    "description" to description,
                    "image" to it.toString(),
                    "sold" to 0
                )

                db.collection("cupcakes")
                    .add(newCupcake)
                    .addOnSuccessListener {
                        Log.d("document", "CREATED")
                    }
            }

        }


    }
}