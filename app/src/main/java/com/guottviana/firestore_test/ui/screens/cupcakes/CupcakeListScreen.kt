package com.guottviana.firestore_test.ui.screens.cupcakes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Cupcake
import com.guottviana.firestore_test.navigation.Routes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.navOptions
import com.guottviana.firestore_test.ui.screens.auth.AuthState
import com.guottviana.firestore_test.ui.screens.auth.AuthViewModel

@Composable
fun CupcakeListScreen(
    navController: NavController,
    viewModel: CupcakeListViewModel = viewModel(),
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel
){
    var filterString by remember {
        mutableStateOf("")
    }

    var showSearch by remember {
        mutableStateOf(false)
    }

    val authState = authViewModel.authState.observeAsState()

    val cupcakes by viewModel.cupcakeList.collectAsStateWithLifecycle()
    val filteredCupcakes by viewModel.filteredProducts.collectAsStateWithLifecycle()

    navController.visibleEntries

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(
                route = Routes.loginScreen,
                navOptions = navOptions {
                    popUpTo(navController.graph.id){
                        inclusive = true
                    }
                }
            )
            else -> Unit
        }
    }

    Column(modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Column(horizontalAlignment = Alignment.Start) {
                AnimatedVisibility(visible = showSearch) {
                    TextField(
                        value = filterString,
                        onValueChange = {
                            filterString = it
                            viewModel.searchProducts(filterString)
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f)
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.search_placeholder),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Image(
                    modifier = Modifier.clickable {
                        showSearch = true
                    },
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = ""
                )
            }

        }



        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (filterString == "" || filterString.isEmpty()) {
                items(cupcakes) { cupcake ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    ) {
                        CupcakeItem(
                            cupcake = cupcake,
                            navController
                        )

                    }
                }
            }else{
                items(filteredCupcakes) { cupcake ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    ) {
                        CupcakeItem(
                            cupcake = cupcake,
                            navController
                        )

                    }
                }
            }
        }
    }


}

@Composable 
fun CupcakeItem(cupcake: Cupcake, navController: NavController){
    Card(modifier = Modifier
        .padding(horizontal = 8.dp)
        .size(width = 132.dp, height = 144.dp)
        .clickable { navController.navigate(Routes.CupcakeScreen(cupcake)) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.CenterHorizontally)) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                AsyncImage(model = cupcake.image, contentDescription = "")
            }
            Spacer(modifier = Modifier.size(8.dp))

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = cupcake.flavor,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
    
}

