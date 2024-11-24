package com.guottviana.firestore_test.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.navigation.Routes
import com.guottviana.firestore_test.ui.screens.auth.AuthViewModel

@Composable
fun UserScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: UserViewModel = viewModel(),
    authViewModel: AuthViewModel
){
    val user by authViewModel.user.collectAsStateWithLifecycle()

    val address by viewModel.address.collectAsStateWithLifecycle()

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (address != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .clickable { navController.navigate(Routes.addressScreen) },
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = address.toString())
                }
                Spacer(modifier = Modifier.height(48.dp))
            }

            UserAttributeRow(
                attributeName = "User name",
                attributeValue = user?.userName.toString()
            )

            Spacer(modifier = Modifier.height(24.dp))

            UserAttributeRow(
                attributeName = "User email",
                attributeValue = user?.email.toString()
            )

            Spacer(modifier = Modifier.height(24.dp))

            UserAttributeRow(
                attributeName = "User type",
                attributeValue = user?.type.toString()
            )
        }
    }
}

@Composable
fun UserAttributeRow(attributeName: String, attributeValue: String){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(text = attributeName)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = attributeValue)
        }
    }
}