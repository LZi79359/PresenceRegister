package com.example.presenceregisterv2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Registration screen, triggers on pressing 'In' on main screen, prompts user for their information and inserts it into the database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffRegistrationScreen(nav: NavController, vm: PresenceViewModel) {
    // variable initialisation
    var name    by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var pin  by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registration") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(value = pin, onValueChange = { pin = it }, label = { Text("Pin") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            OutlinedTextField(value = name, onValueChange = { name = it.uppercase() }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = surname, onValueChange = { surname = it.uppercase() }, label = { Text("Surname") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = mobileNumber, onValueChange = { mobileNumber = it }, label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), singleLine = true)


            Spacer(Modifier.height(8.dp))

            var errorMessage by remember { mutableStateOf("") }
            var showError by remember { mutableStateOf(false) }

            // On submit checks that all fields have been entered, and then checks if the person is currently inside by checking the ID card, and if found its status.
            Button(
                onClick = {
                    errorMessage = when {
                        name.isBlank() || surname.isBlank() || pin.isBlank() || mobileNumber.isBlank() -> "All fields are required"
                        !mobileNumber.all { it.isDigit() || it == '+' } -> "Only digits and + are allowed in mobile field"
                        mobileNumber.replace("+", "").length < 8 -> "Mobile number too Short"
                        mobileNumber.replace("+", "").length > 15 -> "Mobile number too Long"
                        pin.isBlank() || pin.toIntOrNull() == null -> "PIN must be a valid number"
                        else -> ""
                    }
                    if (errorMessage.isEmpty()) {
                        val success = vm.registerStaff(pin.toInt(), name,  surname, mobileNumber)
                        if (success) {
                            nav.popBackStack()
                        }
                        else {
                            errorMessage = "This pin is already registered"
                        }
                    }
                    else{
                        showError = true;
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Enter")
            }

            if (showError) {
                AlertDialog(
                    onDismissRequest = { nav.popBackStack() },
                    title = { Text("Error") },
                    text = { Text(errorMessage, color = MaterialTheme.colorScheme.error) },
                    confirmButton = {
                        TextButton(onClick = {showError = false} ) { Text("OK") }
                    }
                )
            }

        }
    }
}
