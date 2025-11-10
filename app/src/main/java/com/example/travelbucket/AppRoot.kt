package com.example.travelbucket.ui

import androidx.compose.runtime.*
import com.example.travelbucket.data.local.User
import com.example.travelbucket.ui.auth.AuthScreen
import com.example.travelbucket.ui.home.HomeScreen

@Composable
fun AppRoot() {
    var loggedInUser by remember { mutableStateOf<User?>(null) }

    if (loggedInUser == null) {
        AuthScreen(onAuthSuccess = { user -> loggedInUser = user })
    } else {
        HomeScreen(user = loggedInUser!!, onLogout = { loggedInUser = null })
    }
}
