package com.guottviana.firestore_test.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.guottviana.firestore_test.R
import com.guottviana.firestore_test.domain.model.Cupcake
import com.guottviana.firestore_test.domain.model.User
import com.guottviana.firestore_test.ui.screens.addCupcake.AddCupcakeScreen
import com.guottviana.firestore_test.ui.screens.administration.AdminScreen
import com.guottviana.firestore_test.ui.screens.administration.UserEditScreen
import com.guottviana.firestore_test.ui.screens.auth.AuthState
import com.guottviana.firestore_test.ui.screens.auth.AuthViewModel
import com.guottviana.firestore_test.ui.screens.auth.LoginScreen
import com.guottviana.firestore_test.ui.screens.auth.SignupScreen
import com.guottviana.firestore_test.ui.screens.cart.CartScreen
import com.guottviana.firestore_test.ui.screens.cart.ConfirmationScreen
import com.guottviana.firestore_test.ui.screens.cart.PaymentScreen
import com.guottviana.firestore_test.ui.screens.cupcake.CupcakeScreen
import com.guottviana.firestore_test.ui.screens.cupcakes.CupcakeListScreen
import com.guottviana.firestore_test.ui.screens.order.OrdersScreen
import com.guottviana.firestore_test.ui.screens.paymentMethods.CreditScreen
import com.guottviana.firestore_test.ui.screens.paymentMethods.DebitScreen
import com.guottviana.firestore_test.ui.screens.user.AddressScreen
import com.guottviana.firestore_test.ui.screens.user.UserScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues
){

    val authState = authViewModel.authState.observeAsState()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            LoginScreen(modifier, navController, authViewModel)
        }

        composable("home"){
            CupcakeListScreen(modifier, navController, paddingValues = paddingValues)

        }

        composable("signup"){
            SignupScreen(modifier, navController, authViewModel)

        }

        composable("add"){
            AddCupcakeScreen(modifier, navController, paddingValues = paddingValues)

        }

        composable("user"){
            UserScreen(navController = navController, paddingValues = paddingValues, authViewModel = authViewModel)
        }

        composable("address"){
            AddressScreen(navController = navController, paddingValues = paddingValues, authViewModel = authViewModel)
        }

        composable("orders"){
            OrdersScreen(navController = navController, paddingValues = paddingValues, authViewModel = authViewModel)
        }

        composable("admin"){
            AdminScreen(modifier = modifier, navController = navController, paddingValues = paddingValues)
        }

        composable<Routes.CupcakeScreen>(
            typeMap = mapOf(typeOf<Cupcake>() to cupcakeNavType)
                ){
            val productRoute = it.toRoute<Routes.CupcakeScreen>()
            CupcakeScreen(modifier, navController, authViewModel, cupcake = productRoute.cupcake, paddingValues = paddingValues)

        }

        composable<Routes.UserEditScreen>(
            typeMap = mapOf(typeOf<User>() to userNavType)
        ){
            val productRoute = it.toRoute<Routes.UserEditScreen>()
            UserEditScreen( navController, authViewModel, user = productRoute.user, paddingValues = paddingValues)

        }

        composable("cart"){
            CartScreen(modifier = modifier, navController = navController, paddingValues = paddingValues)

        }

        composable("confirm"){
            ConfirmationScreen(modifier = modifier, navController = navController, paddingValues = paddingValues)
        }

        composable("payment"){
            PaymentScreen(modifier = modifier, navController = navController, paddingValues = paddingValues)
        }

        composable("credit"){
            CreditScreen(modifier = modifier, navController = navController, paddingValues = paddingValues)
        }

        composable("debit"){
            DebitScreen(modifier = modifier, navController = navController, paddingValues = paddingValues)
        }

    })

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(Routes.loginScreen)
            else -> Unit
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    authViewModel: AuthViewModel,
    viewModel: TopNavigationViewModel = viewModel(),
    navController: NavHostController,
    navToAdd: () -> Unit,
    navToAdministration: () -> Unit,
    navToCart: () -> Unit,
    navToUser: () -> Unit,
    navToOrder: () -> Unit,
    navToHome: () -> Unit){

    var expandedNav by remember {
        mutableStateOf(false)
    }

    var expandedSearch by remember {
        mutableStateOf(false)
    }

    var expandedUser by remember {
        mutableStateOf(false)
    }

    val color by viewModel.color.collectAsStateWithLifecycle()

    val userType by authViewModel.user.collectAsStateWithLifecycle()

    val cupcakes by viewModel.cupcakeList.collectAsStateWithLifecycle()
    val filteredCupcakes by viewModel.filteredProducts.collectAsStateWithLifecycle()


    CenterAlignedTopAppBar(
        colors = TopAppBarColors(color,color,color,Color.Black,color),
        title = { Text(
            modifier = Modifier.clickable { navToHome() },
            text = stringResource(id = R.string.app_name))
                },
        navigationIcon = {
            Image(
                modifier = Modifier
                    .clickable {
                        expandedNav = true
                    }
                    .size(30.dp),
                painter = painterResource(id = R.drawable.ic_down_arrow),
                contentDescription = ""
            )

            DropdownMenu(
                expanded = expandedNav,
                onDismissRequest = {
                    expandedNav = false
                }) {
                if (userType?.typeId!! > 1) {

                    DropdownMenuItem(text = {
                        Text(text = "Add cupcake")
                    }, onClick = navToAdd)

                    if (userType?.typeId!! > 2) {
                        DropdownMenuItem(text = {
                            Text(text = "Administration")
                        }, onClick = navToAdministration)
                    }
                }
            }

        },
        actions = {
            IconButton(onClick = {
                expandedSearch = true
            }) {
                Image(painter = painterResource(id = R.drawable.ic_search), contentDescription = "")
                CupcakesPopup(
                    showPopup = expandedSearch,
                    onDismiss = {
                        expandedSearch = false
                        viewModel.clearList()
                        },
                    navController = navController,
                    viewModel = viewModel
                )
            }

            IconButton(onClick = navToCart) {
                Image(painter = painterResource(id = R.drawable.ic_cart), contentDescription = "Credits: OpenClipart-Vectors")
            }

            IconButton(onClick = { expandedUser = true }) {
                Image(painter = painterResource(id = R.drawable.ic_avatar), contentDescription = "Credits: OpenClipart-Vectors")
                DropdownMenu(
                    expanded = expandedUser,
                    onDismissRequest = {
                        expandedUser = false
                    }) {

                    DropdownMenuItem(text = {
                        Text("Profile")
                    }, onClick = {
                        navToUser()
                    } )

                    DropdownMenuItem(text = {
                        Text("Orders")
                    }, onClick = {
                        navToOrder()
                    } )

                    DropdownMenuItem(text = {
                        Text("Sign out")
                    }, onClick = {
                        authViewModel.signout()
                    } )

                }
            }

        }
    )
}

@Composable
fun CupcakesPopup(
    showPopup: Boolean,
    viewModel: TopNavigationViewModel,
    onDismiss: () -> Unit,
    navController: NavHostController
){

    viewModel.getList()

    var filterString by remember {
        mutableStateOf("")
    }

    val cupcakes by viewModel.cupcakeList.collectAsStateWithLifecycle()
    val filteredCupcakes by viewModel.filteredProducts.collectAsStateWithLifecycle()


    if (showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .zIndex(10F),
            contentAlignment = Alignment.Center,
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                    focusable = true
                ),
                onDismissRequest = { onDismiss() }
            ) {
                Box(
                    Modifier
                        .width(300F.dp)
                        .height(500F.dp)
                        .background(Color.DarkGray)
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
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
                            placeholder = {
                                Text(
                                    text = "Search for products",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Red
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (filterString == "" || filterString.isEmpty()) {
                            CupcakeList(cupcakes = cupcakes, navController = navController)
                        }else{
                            CupcakeList(cupcakes = filteredCupcakes, navController = navController)
                        }
                    }



                }
            }
        }
    }
}

@Composable
fun CupcakeList(cupcakes: List<Cupcake>, navController: NavHostController){
    LazyColumn {
        cupcakes.forEach {
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(width = 132.dp, height = 144.dp)
                        .clickable {
                            navController.navigate(
                                Routes.CupcakeScreen(
                                    it
                                )
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            AsyncImage(
                                model = it.image,
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))

                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text(
                                text = it.flavor,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}