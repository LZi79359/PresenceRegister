package com.example.presenceregisterv2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Main screen linking to the other pages
@Composable
fun MainScreen(nav: NavController, vm: PresenceViewModel) {
    val highContrast by vm.highContrast.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (highContrast) {
                OutlinedButton(
                    onClick = { nav.navigate("guest") },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                ) {
                    Text("VISITOR", fontSize = 50.sp)
                }
                OutlinedButton(
                    onClick = { nav.navigate("staff") },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                ) {
                    Text("STAFF", fontSize = 50.sp)
                }
            }
            else {
                Button(
                    onClick = { nav.navigate("guest") },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                ) {
                    Text("VISITOR", fontSize = 50.sp)
                }
                Button(
                    onClick = { nav.navigate("staff") },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                ) {
                    Text("STAFF", fontSize = 50.sp)
                }
            }
        }
    }
}