package com.guottviana.firestore_test.ui.screens.cupcake

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Comment
import com.guottviana.firestore_test.domain.model.Cupcake
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CupcakeViewModel: ViewModel() {

    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    fun getComments(cupcake: Cupcake): StateFlow<List<Comment>>{
        val commentList = MutableStateFlow<List<Comment>>(emptyList())
        db.collection("comments")
            .whereEqualTo("cupcakeFlavor", cupcake.flavor)
            .addSnapshotListener { value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null){
                    commentList.value = value.toObjects()
                }

            }
        return commentList
    }

    fun addComment(text: String, flavor: String, context: Context, userName: String){

        val data = hashMapOf(
            "userName" to userName,
            "cupcakeFlavor" to flavor,
            "date" to Timestamp.now(),
            "text" to text,
            "user" to auth.currentUser?.email
        )

        db.collection("comments")
            .add(data)
            .addOnSuccessListener {

                Log.d("COMMENT", "Comment added successfully")
                Toast.makeText(context, R.string.comment_added, Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {

                Log.w("FAILED", "Error adding comment", it)

            }
    }

    fun addToCart(cupcake: Cupcake){

        val data = hashMapOf(
            "flavor" to cupcake.flavor,
            "image" to cupcake.image,
            "price" to cupcake.price,
            "quantity" to 1,
            "user" to auth.currentUser?.email
        )

        db.collection("cartItem")
            .whereEqualTo("flavor", cupcake.flavor)
            .whereEqualTo("user", auth.currentUser?.email)
            .get()
            .addOnSuccessListener{it ->

                if (it.size() > 0){
                    db.collection("cartItem")
                        .document(it.documents[0].id)
                        .update("quantity", (it.documents[0].data?.get("quantity") as Long) + 1)
                        .addOnSuccessListener { documentReference ->
                            Log.d("UPDATED", "Successfully added to the cart")
                        }
                        .addOnFailureListener { e ->
                            Log.w("FAILED", "Error adding document", e)
                        }
                }else{
                    db.collection("cartItem")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            Log.d("CREATED", "DocumentSnapshot written with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("FAILED", "Error adding document", e)
                        }
                }
            }
            .addOnFailureListener {
                Log.d("NOTFOUND", "Error finding document", it)
            }


    }

    fun deleteCupcake(cupcake: Cupcake){
        db.collection("cupcakes")
            .whereEqualTo("flavor", cupcake.flavor)
            .get()
            .addOnSuccessListener { it ->
                db.collection("cupcakes")
                    .document(it.documents[0].id)
                    .delete()
                    .addOnSuccessListener { Log.d("DELETED", "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w("DELETED ", "Error deleting document", e) }
            }

    }
}