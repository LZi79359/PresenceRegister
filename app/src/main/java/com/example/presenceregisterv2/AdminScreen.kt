package com.example.presenceregisterv2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(nav: NavController, vm: PresenceViewModel) {
    val people by vm.people.collectAsState()
    val countInside = people.count { it.isInside }
    val highContrast by vm.highContrast.collectAsState()
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    val unlocked by vm.adminUnlocked.collectAsState()
    var showChangePin by remember { mutableStateOf(false) }
    var oldPinInput by remember { mutableStateOf("") }
    var newPinInput by remember { mutableStateOf("") }
    var changePinError by remember { mutableStateOf("") }
    var confirmPinInput by remember { mutableStateOf("") }


    if (!unlocked) {
        AlertDialog(
            onDismissRequest = { vm.lockAdmin(); nav.popBackStack() },
            title = { Text("Enter PIN") },
            text = {
                Column {
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it; pinError = false },
                        label = { Text("PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = pinError,
                        singleLine = true
                    )
                    if (pinError) {
                        Spacer(Modifier.height(4.dp))
                        Text("Incorrect PIN", color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (vm.checkPin(pinInput)) {
                        vm.unlockAdmin()
                    } else {
                        pinError = true
                        pinInput = ""
                    }
                }) { Text("Confirm") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { vm.lockAdmin(); nav.popBackStack() }) { Text("Cancel") }
                    TextButton(onClick = { showChangePin = true }) { Text("Change PIN") }
                }
            }
        )

        if (showChangePin) {
            AlertDialog(
                onDismissRequest = { nav.popBackStack() },
                title = { Text("Change PIN") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = oldPinInput,
                            onValueChange = { oldPinInput = it; changePinError = "" },
                            label = { Text("Old PIN") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            isError = changePinError.isNotEmpty(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = newPinInput,
                            onValueChange = { newPinInput = it; changePinError = "" },
                            label = { Text("New PIN") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            isError = changePinError.isNotEmpty(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = confirmPinInput,
                            onValueChange = { confirmPinInput = it; changePinError = "" },
                            label = { Text("Confirm new PIN") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            isError = changePinError.isNotEmpty(),
                            singleLine = true
                        )
                        if (changePinError.isNotEmpty()) {
                            Text(changePinError, color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        when {
                            !vm.checkPinForChange(oldPinInput) -> changePinError =
                                "Current PIN is incorrect"

                            newPinInput.length < 4 -> changePinError =
                                "PIN must be at least 4 digits"

                            newPinInput != confirmPinInput -> changePinError =
                                "New PINs do not match"

                            oldPinInput == newPinInput -> changePinError =
                                "New PIN must be different from old PIN"

                            else -> {
                                vm.changePin(newPinInput)
                                showChangePin = false
                                oldPinInput = ""
                                newPinInput = ""
                                confirmPinInput = ""
                            }
                        }
                    }) { Text("Confirm") }
                },
                dismissButton = {
                    TextButton(onClick = { showChangePin = false }) { Text("Cancel") }
                }
            )
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Admin") },
                    navigationIcon = {
                        IconButton(onClick = { vm.lockAdmin(); nav.popBackStack() }) {
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
                Text("People Inside: $countInside", fontSize = 22.sp)
                Spacer(Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (highContrast) {
                        OutlinedButton(
                            onClick = { nav.navigate("staffList") },//TO-DO
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                        ) {
                            Text("Staff List", fontSize = 35.sp)
                        }
                        OutlinedButton(
                            onClick = { nav.navigate("Data") }, //TO-DO
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                        ) {
                            Text("Today's Data", fontSize = 35.sp)
                        }
                    } else {
                        Button(
                            onClick = { nav.navigate("staffList") },//TO-DO
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                        ) {
                            Text("Staff List", fontSize = 35.sp)
                        }
                        Button(
                            onClick = { nav.navigate("Data") },//TO-DO
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                        ) {
                            Text("Today's Data", fontSize = 35.sp)
                        }
                    }
                }
            }
        }
    }
}