package com.guottviana.firestore_test.ui.screens.addCupcake

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.utils.GetCustomContents

@Composable
fun AddCupcakeScreen(
    navController: NavController,
    viewModel: AddCupcakeViewModel = viewModel(),
    paddingValues: PaddingValues,
){
    var flavor by remember {
        mutableStateOf("")
    }

    var stock by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var file by remember {
        mutableStateOf<DocumentFile?>(null)
    }

    val context = LocalContext.current


    Box(
        modifier = Modifier.padding(paddingValues),
        contentAlignment = Alignment.TopStart
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.4f))
                .padding(8.dp)
                .clickable { navController.popBackStack() }
        )
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        OutlinedTextField(
            value = flavor,
            onValueChange = { it ->

            flavor = it
        },
            label = {
                Text(text = stringResource(id = R.string.flavor))
            })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = stock,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = { it ->
                if(it.toIntOrNull() != null) {
                    stock = it
                }else if (it == ""){
                    stock = ""
                }
                            },
            label = {
                Text(text = stringResource(id = R.string.stock_label))
            })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = price,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = { it ->
                if(it.toDoubleOrNull() != null) {
                    price = it
                }else if (it == ""){
                    price = ""
                }
            },
            label = {
                Text(text = stringResource(id = R.string.price_label))
            })

        Spacer(modifier = Modifier.height(16.dp))

        val photoPicker = rememberLauncherForActivityResult(
            contract = GetCustomContents(isMultiple = false),
            onResult = { uris ->

                if (uris.isNotEmpty()) {
                    file = DocumentFile.fromSingleUri(context, uris[0])
                }

            })

        Box() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    ShowFileChooser{
                        photoPicker.launch("image/*")
                    }

                    if (file != null){
                        Text(
                            modifier = Modifier.size(20.dp),
                            text = "\u2713"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
            }
        }
        

        OutlinedTextField(
            value = description,
            onValueChange = { it ->

                description = it
            },
            label = {
                Text(text = stringResource(id = R.string.description))
            })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (stock != "" && flavor != "" && price != "" && description != "" && file != null) {
                viewModel.addCupcake(
                    stock = stock,
                    price = price,
                    flavor = flavor.trimEnd(),
                    description = description.trimEnd(),
                    file = file
                )
                stock = ""
                price = ""
                flavor = ""
                description = ""
                file = null

                Toast.makeText(context, R.string.cupcake_added_toast, Toast.LENGTH_LONG).show()


            }else{
                Toast.makeText(context, R.string.cupcake_added_toast_warning, Toast.LENGTH_LONG).show()
            }

        }) {
            Text(text = stringResource(id = R.string.confirm))
        }


    }

}

@Composable
fun ShowFileChooser(
    photoPicker: () -> Unit){
    Button(
        onClick = { photoPicker() }) {
        Text(text = stringResource(id = R.string.imageSelect))
    }
}