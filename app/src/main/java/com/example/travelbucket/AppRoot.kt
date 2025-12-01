package com.example.travelbucket.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import androidx.navigation.NavType
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
            val navController = rememberNavController()
            val userDatabase = com.example.travelbucket.data.local.UserDatabase.getDatabase(context)
            val dreamDestinationRepository = com.example.travelbucket.data.repository.DreamDestinationRepository(
                userDatabase.dreamDestinationDao(),
                loggedInUser!!.email
            )
            val dreamDestinationViewModel: com.example.travelbucket.ui.dreamdestinations.DreamDestinationViewModel = viewModel(
                factory = com.example.travelbucket.ui.dreamdestinations.DreamDestinationViewModelFactory(dreamDestinationRepository)
            )

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    val countryViewModel: CountryViewModel = viewModel(
                        factory = CountryViewModelFactory(
                            ServiceLocator.provideCountryRepository(context, loggedInUser!!.email)
                        )
                    )
                    HomeScreen(
                        viewModel = countryViewModel,
                        email = loggedInUser!!.email,
                        onLogout = {
                            authViewModel.logout()
                            loggedInUser = null
                        },
                        onDreamDestinationsClick = {
                            navController.navigate("dream_destinations")
                        }
                    )
                }

                composable("dream_destinations") {
                    com.example.travelbucket.ui.dreamdestinations.DreamDestinationsListScreen(
                        viewModel = dreamDestinationViewModel,
                        onAddClick = {
                            navController.navigate("camera")
                        }
                    )
                }

                composable("camera") {
                    com.example.travelbucket.ui.dreamdestinations.CameraScreen(
                        onImageCaptured = { path ->
                            val encodedPath = java.net.URLEncoder.encode(path, "UTF-8")
                            navController.navigate("dream_destination_form/$encodedPath")
                        },
                        onError = {
                            // Handle error
                        }
                    )
                }

                composable(
                    route = "dream_destination_form/{photoPath}",
                    arguments = listOf(navArgument("photoPath") { type = NavType.StringType })
                ) { backStackEntry ->
                    val photoPath = backStackEntry.arguments?.getString("photoPath") ?: ""
                    com.example.travelbucket.ui.dreamdestinations.DreamDestinationFormScreen(
                        photoPath = photoPath,
                        onSave = { name, notes ->
                            dreamDestinationViewModel.addDestination(name, notes, photoPath)
                            navController.popBackStack("dream_destinations", inclusive = false)
                        },
                        onCancel = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
