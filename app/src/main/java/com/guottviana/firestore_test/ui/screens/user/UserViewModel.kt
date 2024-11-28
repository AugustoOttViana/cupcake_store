package com.guottviana.firestore_test.ui.screens.user

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Address
import com.guottviana.firestore_test.domain.model.Cupcake
import com.guottviana.firestore_test.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel: ViewModel() {

    private var _address = MutableStateFlow<Address?>(Address())
    var address = _address.asStateFlow()

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    init {
        getUserAddress()
    }

    private fun getUserAddress(){
        db.collection("address")
            .whereEqualTo("user", auth.currentUser?.email)
            .addSnapshotListener { value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null) {
                    _address.value = value.documents[0].toObject()

                }
            }
    }

    fun addAddress(
        country: String,
        street: String,
        city: String,
        state: String,
        number: Int,
        context: Context
    ) {

        val data = hashMapOf(
            "user" to auth.currentUser?.email,
            "street" to street,
            "country" to country,
            "city" to city,
            "state" to state,
            "number" to number
        )

        db.collection("address")
            .whereEqualTo("user", auth.currentUser?.email)
            .get()
            .addOnSuccessListener {
                if (it.size() > 0) {

                    db.collection("address")
                        .document(it.documents[0].id)
                        .set(data)
                        .addOnSuccessListener { documentReference ->
                            Log.d("UPDATED", "Successfully edited address")
                            Toast.makeText(context, R.string.address_added_toast, Toast.LENGTH_LONG).show()

                        }
                        .addOnFailureListener { e ->
                            Log.w("FAILED", "Error adding document", e)
                        }

                } else {

                    db.collection("address")
                        .add(data)

                }
            }
    }
}