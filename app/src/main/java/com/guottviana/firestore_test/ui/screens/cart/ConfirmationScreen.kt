package com.guottviana.firestore_test.ui.screens.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.navigation.Routes
import com.guottviana.firestore_test.utils.CurrencyUtils

@Composable
fun ConfirmationScreen(
    navController: NavController,
    viewModel: CartViewModel = viewModel(),
    paddingValues: PaddingValues
){
    val cartItems by viewModel.cartItemList.collectAsStateWithLifecycle()

    Column(
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.price_label) + ":",
                modifier = Modifier.weight(1f),
                fontSize = 32.sp
            )

            Text(
                text = CurrencyUtils.formatPrice(viewModel.getTotalPrice(cartItems)),
                fontSize = 32.sp
            )

        }

        Button(onClick = {
            navController.navigate(Routes.paymentScreen)
        }) {
            Text(text = stringResource(id = R.string.confirm))
        }

    }
}