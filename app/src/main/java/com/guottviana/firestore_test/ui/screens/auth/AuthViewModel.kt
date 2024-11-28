package com.guottviana.firestore_test.ui.screens.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.guottviana.firestore_test.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel(){
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private var _user = MutableStateFlow<User?>(null)
    var user = _user.asStateFlow()

    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if (auth.currentUser == null){
            _authState.value = AuthState.Unauthenticated
            _user.value = null
        }else{
            _authState.value = AuthState.Authenticated

            getUserType()
        }
    }
    fun login(email : String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _authState.value =AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                    getUserType()
                }else{
                    _authState.value =AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }
    fun signup(email : String, password : String, userName: String){

        if (email.isEmpty() || password.isEmpty() || userName.isEmpty()){
            _authState.value =AuthState.Error("Email, password and user name can't be empty")
            return
        }

        val data = hashMapOf(
            "userName" to userName,
            "email" to email,
            "type" to "customer",
            "typeId" to 3
        )

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated

                    addFirstAddress(email)

                    db.collection("userType")
                        .add(data)
                        .addOnSuccessListener {
                            Log.d("USER CREATED", "Costumer added")
                            getUserType()
                        }.addOnFailureListener{ e ->
                            Log.w("FAILED", "Error adding document", e)
                        }


                }else{
                    _authState.value =AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
        _user.value = null
    }

    private fun getUserType(){
        db.collection("userType")
            .whereEqualTo("email",auth.currentUser?.email)
            .addSnapshotListener { value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null) {
                    _user.value = value.documents[0].toObject()
                    Log.d("USERTYPE", _user.value.toString())
                }
            }
    }

    fun addFirstAddress(email: String) {

        val data = hashMapOf(
            "user" to email,
            "street" to "",
            "country" to "",
            "city" to "",
            "state" to "",
            "number" to 0
        )

        db.collection("address")
            .add(data)
            .addOnSuccessListener {
                Log.d("FIRSTADDRESS", "Added first version of address")
            }

    }
}


sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()

}