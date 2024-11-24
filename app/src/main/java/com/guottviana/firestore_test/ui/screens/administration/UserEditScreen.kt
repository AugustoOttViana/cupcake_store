package com.guottviana.firestore_test.ui.screens.administration

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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavController
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.User
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UserEditScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel(),
    user: User,
    paddingValues: PaddingValues
){

    var expandedType by remember {
        mutableStateOf(false)
    }

    var type by remember {
        mutableIntStateOf(user.typeId)
    }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

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

            Text(text = user.userName)

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = user.email)

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(onClick = {
                expandedType = true
            }) {
                Text(text = type.toString())
                DropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = {
                        expandedType = false
                    }) {

                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.customer_option))
                        },
                        onClick = {
                            type = 1
                        })

                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.employee_option))
                        },
                        onClick = {
                            type = 2
                        })

                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.admin_option))
                        },
                        onClick = {
                            type = 3
                        })
                }
            }

            Button(onClick = {
                viewModel.editUser(user, type, context)
            }) {
                Text(text = stringResource(id = R.string.sendButton))
            }

        }
    }
}