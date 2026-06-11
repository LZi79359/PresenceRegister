package com.example.presenceregister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
            NavGraph()
        }
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val vm: PresenceViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    val highContrast by vm.highContrast.collectAsState()

    PresenceRegisterTheme(highContrast = highContrast) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { vm.toggleHighContrast() }) {
                    Icon(Icons.Default.InvertColors, contentDescription = "Toggle Contrast")                }
            }
        ) { padding ->
            NavHost(
                navController,
                startDestination = "main",
                modifier = Modifier.padding(padding)
            ) {
                composable("main")             { MainScreen(navController, vm) }
                composable(route = "register") { RegistrationScreen(navController, vm) }
                composable(route = "exit")     { ExitScreen(navController, vm) }
                composable(route = "data")     { DataScreen(navController, vm) }
            }
        }
    }
}