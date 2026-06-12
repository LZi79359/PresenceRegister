package com.example.presenceregisterv2

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.navigation.NavController

@Composable
private fun PersonRow(
    col1: String,
    col2: String,
    col3: String,
    col4: @Composable RowScope.() -> Unit,
    isHeader: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        val style = if (isHeader) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyMedium
        Text(col1, modifier = Modifier.weight(2f), style = style)
        Text(col2, modifier = Modifier.weight(1.5f), style = style)
        Text(col3, modifier = Modifier.weight(1.5f), style = style)
        this.col4()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StaffListScreen(nav: NavController, vm: PresenceViewModel) {
    val staff by vm.staff.collectAsState()
    var pendingDelete by remember { mutableStateOf<Staff?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff List") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    OutlinedButton(onClick = { nav.navigate("registerStaff") }) {
                        Text("New User")
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
            stickyHeader {
                Surface(shadowElevation = 4.dp) {
                    PersonRow(
                        col1 = "Pin",
                        col2 = "Name",
                        col3 = "Mobile",
                        col4 = {
                            Text(
                                "Action",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        isHeader = true
                    )
                }
            }

            items(staff) { person ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    PersonRow(
                        col1 = person.pin.toString(),
                        col2 = "${person.name} ${person.surname}",
                        col3 = person.mobileNumber,
                        col4 = {
                            OutlinedButton(onClick = {pendingDelete = person}) {
                                Text("Remove User")
                            }
                        }
                    )
                }
                }
            }
        }

    pendingDelete?.let { person ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text("Confirm Deletion of Staff User") },
            text = { Text("Ensure correct person:\n${person.name} ${person.surname} — ${person.pin}") },
            confirmButton = {
                TextButton(onClick = {
                    vm.removeStaff(person)
                }) { Text("Go") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("Cancel") }
            }
       )
    }
}


