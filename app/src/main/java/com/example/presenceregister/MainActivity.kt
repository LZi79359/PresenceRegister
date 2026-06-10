package com.example.presenceregister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presenceregister.ui.theme.PresenceRegisterTheme

// Navigation initialization

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PresenceRegisterTheme {
                NavGraph()
            }
        }
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val vm: PresenceViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
        context.applicationContext as android.app.Application)
    )

    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController, vm) }
        composable(route = "register") {RegistrationScreen(navController, vm)}
        composable(route = "exit") {ExitScreen(navController, vm)}
        composable(route = "data") {DataScreen(navController, vm)}
    }
}