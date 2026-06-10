package com.example.presenceregister

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Data page, shows all the people who entered that day along with their current status, triggers on pressing "Today's Data" button

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(nav: NavController, vm: PresenceViewModel) {
    // gets all people from the database
    val people by vm.people.collectAsState()

    // variable initialization
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    var unlocked by remember { mutableStateOf(false) }

    // Show pin dialog if not unlocked
    if (!unlocked) {
        AlertDialog(
            onDismissRequest = { nav.popBackStack() }, // dismiss = go back
            title = { Text("Enter PIN") },
            text = {
                Column {
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            pinInput = it
                            pinError = false
                        },
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
                    // on confirm pressed compares input to pre-determined pin
                    if (vm.checkPin(pinInput)) {
                        unlocked = true
                    } else {
                        pinError = true
                        pinInput = ""
                    }
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { nav.popBackStack() }) { Text("Cancel") }
            }
        )
    } else {
        // Actual data screen content
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Today's Data") },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(people) { person ->
                    val status = if (person.isInside) "Inside" else "Out"
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${person.name} ${person.surname}")
                            Text(person.idCard)
                            Text(status)
                        }
                    }
                }
            }
        }
    }
}