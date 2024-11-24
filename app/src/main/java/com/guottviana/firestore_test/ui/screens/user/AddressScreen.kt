package com.guottviana.firestore_test.ui.screens.user

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guottviana.firestore_test.R

@Composable
fun AddressScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: UserViewModel = viewModel()
){
    val address by viewModel.address.collectAsStateWithLifecycle()

    var street by remember {
        mutableStateOf(address?.street.toString())
    }

    var country by remember {
        mutableStateOf(address?.country.toString())
    }

    var city by remember {
        mutableStateOf(address?.city.toString())
    }

    var state by remember {
        mutableStateOf(address?.state.toString())
    }

    var number by remember {
        mutableStateOf(address?.number.toString())
    }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
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
                .align(Alignment.TopStart)
                .clickable { navController.popBackStack() }
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(value = country, onValueChange = { it ->
                country = it
            },
                label = {
                    if (address?.country.toString() != "") {
                        Text(text = address?.country.toString())
                    }else {
                        Text(text = stringResource(id = R.string.country))
                    }
                }
            )

            OutlinedTextField(value = state, onValueChange = { it ->
                state = it
            },
                label = {
                    if (address?.state.toString() != "") {
                        Text(text = address?.state.toString())
                    }else {
                        Text(text = stringResource(id = R.string.state))
                    }
                }
            )

            OutlinedTextField(value = city, onValueChange = { it ->
                city = it
            },
                label = {
                    if (address?.city.toString() != "") {
                        Text(text = address?.city.toString())
                    }else{
                        Text(text = stringResource(id = R.string.city))
                    }
                }
            )

            OutlinedTextField(value = street, onValueChange = { it ->
                street = it
            },
                label = {
                    if (address?.street.toString() != "") {
                        Text(text = address?.street.toString())
                    }else {
                        Text(text = stringResource(id = R.string.street))
                    }
                }
            )

            OutlinedTextField(value = number, onValueChange = { it ->
                if(it.toIntOrNull() != null) {
                    number = it
                }else if (it == "" || number == "0"){
                    number = ""
                }

            },
                label = {
                    if (address?.number.toString() != "" && address?.number.toString() != "0") {
                        Text(text = address?.number.toString())
                    }else{
                        Text(text = stringResource(id = R.string.number))
                    }
                }
            )

            Button(
                onClick = {

                    if (country == "" || street == "" || state == "" || city == "" || number == "" || number == "0") {
                        Toast.makeText(context, R.string.address_added_toast_warning, Toast.LENGTH_LONG).show()
                    }else {
                        viewModel.addAddress(
                            country = country,
                            street = street,
                            state = state,
                            city = city,
                            number = number.toInt()
                        )
                    }
                }) {
                Text(text = stringResource(id = R.string.sendButton))
            }
        }
    }
}