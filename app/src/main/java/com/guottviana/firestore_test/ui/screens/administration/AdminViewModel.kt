package com.guottviana.firestore_test.ui.screens.administration

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Cupcake
import com.guottviana.firestore_test.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.OutputStream

class AdminViewModel: ViewModel() {

    private val db = Firebase.firestore




    fun setColor(color:String){

        db.collection("colorOption")
            .document("color")
            .update("color", color)
            .addOnSuccessListener { documentReference ->
                Log.d("UPDATED", "Successfully edited color")
            }
            .addOnFailureListener { e ->
                Log.w("FAILED", "Error editing document", e)
            }
    }

    fun getUsers(): StateFlow<List<User>> {
        val userList = MutableStateFlow<List<User>>(emptyList())

        db.collection("userType")
            .addSnapshotListener { value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                if (value != null){
                    userList.value = value.toObjects()
                }
            }

        return userList

    }

    fun editUser(user: User, newType: Int, context: Context){

        var typeName = ""
        if (newType == 1){
            typeName = "customer"
        }else if (newType == 2){
            typeName = "employee"
        }else if (newType == 3){
            typeName = "administrator"
        }

        val data = hashMapOf(
            "userName" to user.userName,
            "email" to user.email,
            "type" to typeName,
            "typeId" to newType
        )

        db.collection("userType")
            .whereEqualTo("email", user.email)
            .get()
            .addOnSuccessListener {
                db.collection("userType").document(it.documents[0].id)
                    .set(data)
                    .addOnSuccessListener {
                        Log.d("USEREDIT", "User successfully edited")
                        Toast.makeText(context, "User successfully edited", Toast.LENGTH_SHORT).show()
                    }
            }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getCSV(context: Context) {

        db.collection("cupcakes")
            .get()
            .addOnSuccessListener {
                saveDocument(context, it.toObjects())
            }

    }


    private fun OutputStream.writeCsv(listOfData: List<Cupcake>) {
        //get data create directory

        val writer = bufferedWriter()
        writer.write(""""Flavor", "Price", "Sold"""")
        writer.newLine()
        listOfData.forEach {
            writer.write("\"${it.flavor}\", ${it.price}, ${it.sold}")
            writer.newLine()
        }
        writer.flush()
        writer.close()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveDocument(context : Context, cupcakeList: List<Cupcake>) {

        val collection =
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val dirDest = File(
            Environment.DIRECTORY_DOCUMENTS,
            context.getString(R.string.app_name)
        )
        val date = System.currentTimeMillis()
        val fileName = "export_$date.csv"


        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.RELATIVE_PATH,
                "$dirDest${File.separator}")
            put(MediaStore.Files.FileColumns.IS_PENDING, 1)


        }

        val uri = context.contentResolver.insert(collection, contentValues)



        uri?.let { it ->
            context.contentResolver.openOutputStream(it, "w").use { out ->
                out?.writeCsv(cupcakeList)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 0)

            context.contentResolver.update(it, contentValues, null, null)

            Toast.makeText(
                context,
                "$fileName ${R.string.created_on} $dirDest",
                Toast.LENGTH_LONG).show()
        }





}

}

