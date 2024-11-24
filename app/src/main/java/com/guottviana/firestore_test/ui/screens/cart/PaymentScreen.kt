package com.guottviana.firestore_test.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.navigation.Routes

@Composable
fun PaymentScreen(
    navController: NavController,
    paddingValues: PaddingValues
){
    Column(
        modifier = Modifier.padding(paddingValues).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
            navController.navigate(Routes.creditScreen)
        }) {
            Text(text = stringResource(id = R.string.credit_button))
        }

        Button(onClick = {
            navController.navigate(Routes.debitScreen)
        }) {
            Text(text = stringResource(id = R.string.debit_button))
        }
    }
}