package com.example.presenceregister

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Registration screen, triggers on pressing 'In' on main screen, prompts user for their information and inserts it into the database

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(nav: NavController, vm: PresenceViewModel) {
    // variable initialisation
    var name    by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var idCard  by remember { mutableStateOf("") }

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
            OutlinedTextField(value = name,    onValueChange = { name = it },    label = { Text("Name") },    modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = surname, onValueChange = { surname = it }, label = { Text("Surname") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = idCard,  onValueChange = { idCard = it },  label = { Text("ID Card") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))

            var errorMessage by remember { mutableStateOf("")}

            // On submit checks that all fields have been entered, and then checks if the person is currently inside by checking the ID card, and if found its status.
            Button(
                onClick = {
                    if (name.isNotBlank() && surname.isNotBlank() && idCard.isNotBlank()) {
                        val success = vm.registerPerson(name, surname, idCard)
                        if (success) {
                            nav.popBackStack()
                        }
                        else {
                            errorMessage = "This person is already inside"
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Enter")
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
