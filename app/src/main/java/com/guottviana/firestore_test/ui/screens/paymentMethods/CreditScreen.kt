package com.guottviana.firestore_test.ui.screens.paymentMethods

import android.util.Log
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
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Address
import com.guottviana.firestore_test.navigation.Routes
import com.guottviana.firestore_test.ui.screens.cart.CartViewModel

@Composable
fun CreditScreen(
    navController: NavController,
    viewModel: CartViewModel = viewModel(),
    paddingValues: PaddingValues
){

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

    val address by viewModel.address.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = owner,
            label = { Text(text = stringResource(id = R.string.card_owner)) },
            onValueChange = {
                owner = it
            })

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = cardNumber,
            label = { Text(text = stringResource(id = R.string.number)) },
            onValueChange = {
                if(it.toLongOrNull() != null && it.length <= 16) {
                    cardNumber = it.trim()
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
                label = { Text(text = stringResource(id = R.string.expiration_date)) },
                placeholder = { Text(text = stringResource(id = R.string.expiration_value))},
                onValueChange = {
                    if(it.replace("/","").toIntOrNull() != null && it.length <= 5) {
                        expiration = it

                    }else if (it == ""){
                        expiration = ""
                    }
                })

            Spacer(modifier = Modifier.width(10.dp))

            TextField(
                modifier = Modifier
                    .size(width = 130.dp, height = 56.dp),
                value = cvv,
                label = { Text(text = stringResource(id = R.string.cvv)) },
                onValueChange = {
                    if(it.toIntOrNull() != null && it.length <= 3) {
                        cvv = it
                    }else if (it == ""){
                        cvv = ""
                    }
                })
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (cvv.length != 3 ||
                (expiration.length != 5 || !expiration.contains('/')) ||
                cardNumber.length != 16 ||
                owner == "" ) {

                Log.d("cvv", (cvv.length != 3).toString())
                Log.d("expiration", (expiration.length != 5 || !expiration.contains('/')).toString())
                Log.d("TESTE", (cardNumber.length != 16).toString())
                Log.d("TESTE", (owner == "").toString())
                Toast.makeText(context, R.string.card_payment_error_toast, Toast.LENGTH_LONG).show()
            }else {
                cvv = ""
                expiration = ""
                cardNumber = ""
                owner = ""

                viewModel.createOrder(viewModel.cartItemList.value, address as Address, context)

                navController.navigate(Routes.orderScreen)
            }
        }) {
            Text(text = stringResource(id =R.string.sendButton))
        }

    }
}