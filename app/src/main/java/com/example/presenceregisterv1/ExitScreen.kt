package com.example.presenceregisterv1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Exit page, triggers upon pressing the out button

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExitScreen(nav: NavController, vm: PresenceViewModel) {
    val people by vm.people.collectAsState()

    var idCard by remember { mutableStateOf("") }
    var notFound by remember { mutableStateOf(false) }
    var matchedPerson by remember { mutableStateOf<Person?>(null) }
    var pendingPerson by remember { mutableStateOf<Person?>(null) }

    // ID card input dialog
    if (matchedPerson == null) {
        AlertDialog(
            onDismissRequest = { nav.popBackStack() },
            title = { Text("Enter ID Card or Passport") },
            text = {
                Column {
                    OutlinedTextField(
                        value = idCard,
                        onValueChange = {
                            idCard = it.uppercase()
                            notFound = false
                        },
                        label = { Text("ID Card or Passport") },
                        isError = notFound,
                        singleLine = true
                    )
                    if (notFound) {
                        Spacer(Modifier.height(4.dp))
                        Text("Person not found or not inside", color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val match = people.find { it.idCard == idCard.trim() && it.isInside }
                    if (match != null) {
                        matchedPerson = match
                        notFound = false
                    } else {
                        notFound = true
                    }
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { nav.popBackStack() }) { Text("Cancel") }
            }
        )
    } else {
        // Show matched person with Leave button
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Exit") },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                matchedPerson?.let { person ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${person.name} ${person.surname}")
                            Text(person.idCard)
                            Text(person.mobileNumber)
                            OutlinedButton(onClick = { pendingPerson = person }) {
                                Text("Leave")
                            }
                        }
                    }
                }
            }
        }

        // Confirmation dialog
        pendingPerson?.let { person ->
            AlertDialog(
                onDismissRequest = { pendingPerson = null },
                icon = { Icon(Icons.Default.Warning, contentDescription = null) },
                title = { Text("Confirm Exit") },
                text = { Text("Ensure correct person:\n${person.name} ${person.surname} — ${person.idCard}") },
                confirmButton = {
                    TextButton(onClick = {
                        vm.markAsOut(person.idCard)
                        nav.popBackStack()
                    }) { Text("Go") }
                },
                dismissButton = {
                    TextButton(onClick = { pendingPerson = null }) { Text("Cancel") }
                }
            )
        }
    }
}