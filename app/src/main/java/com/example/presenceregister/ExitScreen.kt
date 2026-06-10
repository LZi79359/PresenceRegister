package com.example.presenceregister

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    // gets all people who are inside
    val people by vm.people.collectAsState()
    val inside = people.filter { it.isInside }

    // person whose status is going to be changed upon confirmation
    var pendingPerson by remember { mutableStateOf<Person?>(null) }

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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(inside) { person ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${person.name} ${person.surname}  •  ${person.idCard}")
                    OutlinedButton(onClick = { pendingPerson = person }) {
                        Text("Leave")
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
            text  = { Text("Ensure correct person:\n${person.name} ${person.surname} — ${person.idCard}") },
            confirmButton = {
                TextButton(onClick = {
                    vm.markAsOut(person.idCard)
                    pendingPerson = null
                }) { Text("Go") }
            },
            dismissButton = {
                TextButton(onClick = { pendingPerson = null }) { Text("Cancel") }
            }
        )
    }
}