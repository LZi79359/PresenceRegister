package com.example.presenceregisterv2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(nav: NavController, vm: PresenceViewModel) {
    val highContrast by vm.highContrast.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showSignInDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff") },
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
        ){

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (highContrast) {
                    OutlinedButton(
                        onClick = { showDialog = true }, //TO-DO
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Text("IN/OUT", fontSize = 50.sp) //TO-DO
                    }
                    OutlinedButton(
                        onClick = { nav.navigate("admin") },
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Text("Admin", fontSize = 50.sp)
                    }
                }
                else {
                    Button(
                        onClick = { showDialog = true }, //TO-DO
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Text("IN/OUT", fontSize = 50.sp)
                    }
                    Button(
                        onClick = { nav.navigate("admin") }, //TO-DO
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                    ) {
                        Text("Admin", fontSize = 50.sp)
                    }
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {nav.popBackStack()},
                title = { Text("Enter Staff Pin")},
                text = {
                    OutlinedTextField(
                        value = pin,
                        onValueChange = {pin = it},
                        label = { Text("Pin")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        val pinInt = pin.toIntOrNull()
                        if (pinInt == null) return@TextButton

                        val isInside = vm.people.value.any { it.idCard == pin && it.isInside }

                        if (isInside) {
                            vm.markAsOut(pin)
                            showSignOutDialog = true
                            showDialog = false
                            pin = ""
                        } else {
                            val success = vm.signInStaff(pinInt)
                            if (success) {
                                showSignInDialog = true
                                showDialog = false
                                pin = ""
                            } else {
                                showErrorDialog = true
                                showDialog = false
                                pin = ""
                            }
                        }
                    }) { Text("Confirm") }
                },
                dismissButton = { TextButton(onClick = { showDialog = false; pin = "" }) {Text("Cancel")} }
            )
        }
        if (showSignInDialog) {
            AlertDialog(
                onDismissRequest = { showSignInDialog = false },
                title = { Text("Signed In") },
                text = { Text("Staff member successfully signed in.") },
                confirmButton = {
                    TextButton(onClick = { showSignInDialog = false }) { Text("OK") }
                }
            )
        }

        if (showSignOutDialog) {
            AlertDialog(
                onDismissRequest = { showSignOutDialog = false },
                title = { Text("Signed Out") },
                text = { Text("Staff member successfully signed out.") },
                confirmButton = {
                    TextButton(onClick = { showSignOutDialog = false }) { Text("OK") }
                }
            )
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Error") },
                text = { Text("PIN not recognised. Please try again.") },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) { Text("OK") }
                }
            )
        }
    }


}