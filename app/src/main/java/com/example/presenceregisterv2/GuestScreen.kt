package com.example.presenceregisterv2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Main screen linking to the other pages
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestScreen(nav: NavController, vm: PresenceViewModel) {
    val highContrast by vm.highContrast.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Visitors") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (highContrast) {
                    OutlinedButton(
                        onClick = { nav.navigate("register") },
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Text("IN", fontSize = 50.sp)
                    }
                    OutlinedButton(
                        onClick = { nav.navigate("exit") },
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Text("OUT", fontSize = 50.sp)
                    }
                } else {
                    Button(
                        onClick = { nav.navigate("register") },
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Text("IN", fontSize = 50.sp)
                    }
                    Button(
                        onClick = { nav.navigate("exit") },
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Text("OUT", fontSize = 50.sp)
                    }
                }
            }
        }
    }
}