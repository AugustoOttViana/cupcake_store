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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guottviana.firestore_test.domain.model.Order
import com.guottviana.firestore_test.ui.screens.auth.AuthViewModel
import com.guottviana.firestore_test.ui.screens.user.UserViewModel

@Composable
fun OrdersScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: OrdersViewModel = viewModel(),
    authViewModel: AuthViewModel
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
            Text(text = "No Orders")
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
        Text(text = "Products: ${order.products}")
        Text(text = "Address: ${order.address}")
        Text(text = "Total Amount: ${order.price}")
    }
}