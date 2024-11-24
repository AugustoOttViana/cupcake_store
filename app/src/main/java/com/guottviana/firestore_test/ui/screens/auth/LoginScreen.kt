package com.guottviana.firestore_test.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.guottviana.firestore_test.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guottviana.firestore_test.navigation.Routes

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate(Routes.homeScreen)
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.login_cupcake), contentDescription = "Login Image",
            modifier = Modifier.size(200.dp))
        Text(text = stringResource(id = R.string.welcome), fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = stringResource(id = R.string.ask_login))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { it ->
            email = it
        },
            label = {
                Text(text = stringResource(id = R.string.email))
            })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = password, onValueChange = { it ->
            password = it
        },
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.login(email, password)
        },
            enabled = authState.value != AuthState.Loading) {
            Text(text = stringResource(id = R.string.login))
        }

        Spacer(modifier = Modifier.height(32.dp))

//        TextButton(onClick = {  }) {
//            Text(text = "Forgot your password?")
//        }
        /* ou
        Text(text = "Esqueceu a senha?", modifier = Modifier.clickable {  })
       */

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = {
                navController.navigate(Routes.signupScreen)
            },
            enabled = authState.value != AuthState.Loading) {
            Text(text = stringResource(id = R.string.signup))
        }
//        Spacer(modifier = Modifier.height(12.dp))

//        Text(text = "Or sign in with")
//    Row (modifier = Modifier
//        .fillMaxWidth()
//        .padding(40.dp),
//        horizontalArrangement = Arrangement.SpaceEvenly) {
//        Image(painter = painterResource(id = R.drawable.google),
//            contentDescription = "Google",
//            modifier = Modifier
//                .size(60.dp)
//                .clickable {
//
//                }
//        )
//
//        Image(painter = painterResource(id = R.drawable.facebook),
//            contentDescription = "facebook",
//            modifier = Modifier
//                .size(60.dp)
//                .clickable {
//
//                }
//        )
//
//        Image(painter = painterResource(id = R.drawable.twitter),
//            contentDescription = "twitter",
//            modifier = Modifier
//                .size(60.dp)
//                .clickable {
//
//                }
//        )
//}

    }

}