package com.example.travelbucket.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelbucket.data.local.User
import com.example.travelbucket.data.remote.ServiceLocator
import com.example.travelbucket.ui.auth.AuthScreen
import com.example.travelbucket.ui.auth.AuthViewModel
import com.example.travelbucket.ui.home.CountryViewModel
import com.example.travelbucket.ui.home.CountryViewModelFactory
import com.example.travelbucket.ui.home.HomeScreen
import com.example.travelbucket.ui.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    val auth = FirebaseAuth.getInstance()
    var loggedInUser by remember { mutableStateOf<User?>(null) }
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(300)
        showSplash = false
        println("DEBUG: loggedInUser changed -> $loggedInUser")
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            val localUser = authViewModel.getLocalUser(firebaseUser.uid)
            loggedInUser = localUser
            println("DEBUG: loggedInUser changed -> $loggedInUser")
        }
    }

    AnimatedVisibility(
        visible = showSplash,
        exit = fadeOut()
    ) {
        SplashScreen()
    }

    if (!showSplash) {
        if (loggedInUser == null) {
            AuthScreen(onAuthSuccess = { loggedInUser = it })
        } else {
            // inject your CountryViewModel here
            val countryViewModel: CountryViewModel = viewModel(
                factory = CountryViewModelFactory(
                    ServiceLocator.provideCountryRepository(context, loggedInUser!!.email)
                )
            )


            HomeScreen(
                viewModel = countryViewModel,
                email = loggedInUser!!.email,
                onLogout = {
                    // whatever you do for logout
                    authViewModel.logout()
                }
            )

        }
    }
}
