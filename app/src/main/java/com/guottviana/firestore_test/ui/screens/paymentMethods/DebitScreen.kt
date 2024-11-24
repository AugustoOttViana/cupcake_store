package com.guottviana.firestore_test.ui.screens.paymentMethods

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Address
import com.guottviana.firestore_test.navigation.Routes
import com.guottviana.firestore_test.ui.screens.cart.CartViewModel

@Composable
fun DebitScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CartViewModel = viewModel(),
    paddingValues: PaddingValues,
){

    val address by viewModel.address.collectAsStateWithLifecycle()

    var owner by remember {
        mutableStateOf("")
    }

    var cardNumber by remember {
        mutableStateOf("")
    }

    var expiration by remember {
        mutableStateOf("")
    }

    var cvv by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = owner,
            label = { Text(text = "Card owner") },
            onValueChange = {
                owner = it
            })

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = cardNumber,
            label = { Text(text = "Number") },
            onValueChange = {
                if(it.toIntOrNull() != null) {
                    cardNumber = it
                }else if (it == ""){
                    cardNumber = ""
                }
            })

        Spacer(modifier = Modifier.height(20.dp))

        Row(
        ) {
            TextField(
                modifier = Modifier
                    .size(width = 130.dp, height = 56.dp),
                value = expiration,
                label = { Text(text = "Expiration") },
                onValueChange = {
                    expiration = it
                })

            Spacer(modifier = Modifier.width(10.dp))

            TextField(
                modifier = Modifier
                    .size(width = 130.dp, height = 56.dp),
                value = cvv,
                label = { Text(text = "CVV") },
                onValueChange = {
                    cvv = it
                })
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            cvv = ""
            expiration = ""
            cardNumber = ""
            owner = ""

            viewModel.createOrder(viewModel.cartItemList.value, address as Address, context)

            navController.navigate(Routes.orderScreen)
        }) {
            Text(text = stringResource(id = R.string.sendButton))
        }

    }
}