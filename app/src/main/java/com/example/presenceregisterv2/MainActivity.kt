package com.example.presenceregisterv2

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
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
import com.example.presenceregisterv2.ui.theme.PresenceRegisterTheme
import androidx.core.view.WindowCompat
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay

// Navigation initialization
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavGraph()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.let {
            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onResume() {
        super.onResume()
        startLockTask()
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
                composable("main") { MainScreen(navController, vm) }
                composable(route = "register") { RegistrationScreen(navController, vm) }
                composable(route = "exit") { ExitScreen(navController, vm) }
                composable(route = "data") { DataScreen(navController, vm) }
                composable(route = "staff") { StaffScreen(navController, vm) }
                composable(route = "admin") { AdminScreen(navController, vm) }
                composable(route = "guest") { GuestScreen(navController, vm) }
                composable(route = "staffList") {StaffListScreen(navController, vm) }
                composable(route = "registerStaff") {StaffRegistrationScreen(navController, vm) }

            }
        }
    }
}
