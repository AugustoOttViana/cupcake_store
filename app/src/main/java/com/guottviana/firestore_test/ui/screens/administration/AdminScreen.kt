package com.guottviana.firestore_test.ui.screens.administration

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.navigation.Routes

@Composable
fun AdminScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: AdminViewModel = viewModel()
){

    var configMenu by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        LazyRow(
            modifier
                .fillMaxWidth()
        ) {
            item {
                Button(onClick = {
                    configMenu = "color"
                }) {
                    Text(text = stringResource(id = R.string.color_options_button))
                }
            }

            item {
                Button(onClick = {
                    configMenu = "users"
                }) {
                    Text(text = stringResource(id = R.string.user_options_button))
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                item {
                    Button(onClick = {
                        configMenu = "export"
                    }) {
                        Text(text = stringResource(id = R.string.export_button))
                    }
                }
            }
        }

        if (configMenu == "color"){
            ColorEdit(viewModel)
        }else if (configMenu == "users"){
            UserEdit(viewModel, navController)
        }else if (configMenu == "export") {
            ExportData(viewModel)
        }
    }
}

@Composable
fun ColorEdit(viewModel: AdminViewModel){
    var option by remember {
        mutableStateOf("select a color")
    }

    var expandedNav by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextButton(onClick = {
            expandedNav = true
        }){
            Text(text = option)
            DropdownMenu(
                expanded = expandedNav,
                onDismissRequest = {
                    expandedNav = false
                }) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.red_option))
                    },
                    onClick = {
                        option = "Red"
                    })
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.blue_option))
                    },
                    onClick = {
                        option = "Blue"
                    })
            }
        }

        Button(onClick = {
            if (option != "select a color"){
                viewModel.setColor(option)
                option = "select a color"
            }
        }) {
            Text(text = stringResource(id = R.string.sendButton))
        }
    }
}

@Composable
fun UserEdit(
    viewModel: AdminViewModel,
    navController: NavController,
    ){
    val users by viewModel.getUsers().collectAsStateWithLifecycle()

    Column {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            users.forEach {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Routes.UserEditScreen(it))
                            },
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(text = it.userName)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = it.type)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExportData(viewModel: AdminViewModel){

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Button(onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                viewModel.getCSV(context = context)
            }
        }) {
            Text(text = stringResource(id = R.string.export_button))
        }

    }

}



