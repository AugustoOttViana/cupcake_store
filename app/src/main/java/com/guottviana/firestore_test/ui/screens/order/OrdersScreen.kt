package com.guottviana.firestore_test.ui.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Order

@Composable
fun OrdersScreen(
    paddingValues: PaddingValues,
    viewModel: OrdersViewModel = viewModel()
){

    val orders by viewModel.orders.collectAsStateWithLifecycle()


    if (orders.isEmpty()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.no_orders))
        }
    } else {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            orders.forEach(){
                item {
                    OrderItem(order = it)
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.LightGray.copy(alpha = 0.4f)
            )
            .padding(8.dp)
    ) {
        Text(text = stringResource(id = R.string.products) + ": ${order.products}")
        Text(text = stringResource(id = R.string.address) + ": ${order.address}")
        Text(text = stringResource(id = R.string.price_label) + ": ${order.price}")
    }
}