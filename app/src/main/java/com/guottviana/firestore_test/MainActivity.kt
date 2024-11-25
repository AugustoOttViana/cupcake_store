package com.guottviana.firestore_test

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.guottviana.firestore_test.navigation.AppNavigation
import com.guottviana.firestore_test.navigation.Routes
import com.guottviana.firestore_test.navigation.TopNavigationBar
import com.guottviana.firestore_test.ui.screens.auth.AuthViewModel
import com.guottviana.firestore_test.ui.theme.Firestore_testTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val getpermission = Intent()
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(getpermission)
            }
        }

        val authViewModel : AuthViewModel by viewModels()
        setContent {
            Firestore_testTheme {
                val shouldShowNav = remember {
                    mutableStateOf(true)
                }

                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AnimatedVisibility(
                            visible = shouldShowNav.value,
                            enter = fadeIn()) {
                                TopNavigationBar(
                                    authViewModel = authViewModel,
                                    navToAdd = {
                                        navController.navigate(Routes.addCupcakeScreen)
                                    },
                                    navToAdministration = {
                                        navController.navigate(Routes.administration)
                                    },
                                    navToCart = {
                                        navController.navigate(Routes.cartScreen)
                                    },
                                    navToUser = {
                                        navController.navigate(Routes.userScreen)
                                    },
                                    navToOrder = {
                                        navController.navigate(Routes.orderScreen)
                                    },
                                    navToHome = {
                                        navController.navigate(Routes.homeScreen)
                                    },
                                    navController = navController)
                            }
                    }
                ) { paddingValues ->

                    AppNavigation(
                        authViewModel = authViewModel,
                        navController = navController,
                        paddingValues = paddingValues,
                        shouldShowNav = shouldShowNav
                    )
                }

            }
        }
    }
}
