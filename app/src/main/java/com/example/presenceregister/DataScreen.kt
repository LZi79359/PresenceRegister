package com.example.presenceregister

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
fun DataScreen(nav: NavController, vm: PresenceViewModel) {
    val people by vm.people.collectAsState()

    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    var unlocked by remember { mutableStateOf(false) }
    var showChangePin by remember { mutableStateOf(false) }
    var oldPinInput by remember { mutableStateOf("") }
    var newPinInput by remember { mutableStateOf("") }
    var changePinError by remember { mutableStateOf("") }
    var confirmPinInput by remember { mutableStateOf("") }
    var toggledToInside by remember { mutableStateOf(false) }
    var pendingPerson by remember { mutableStateOf<Person?>(null) }

    if (!unlocked) {
        AlertDialog(
            onDismissRequest = { nav.popBackStack() },
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
                        unlocked = true
                    } else {
                        pinError = true
                        pinInput = ""
                    }
                }) { Text("Confirm") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { nav.popBackStack() }) { Text("Cancel") }
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
                            !vm.checkPinForChange(oldPinInput) -> changePinError = "Current PIN is incorrect"
                            newPinInput.length < 4 -> changePinError = "PIN must be at least 4 digits"
                            newPinInput != confirmPinInput -> changePinError = "New PINs do not match"
                            oldPinInput == newPinInput -> changePinError = "New PIN must be different from old PIN"
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
                    title = { Text(if (!toggledToInside) "Today's Data" else "People Inside") },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        OutlinedButton(onClick = { toggledToInside = !toggledToInside }) {
                            Text(if (!toggledToInside) "See Only People Inside" else "See All of Today's Data")
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
                            col1 = "Name",
                            col2 = "ID Card",
                            col3 = "Mobile",
                            col4 = {
                                Text(
                                    if (!toggledToInside) "Status" else "Action",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            isHeader = true
                        )
                    }
                }

                if (!toggledToInside) {
                    items(people) { person ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            PersonRow(
                                col1 = "${person.name} ${person.surname}",
                                col2 = person.idCard,
                                col3 = person.mobileNumber,
                                col4 = { Text(if (person.isInside) "Inside" else "Out") }
                            )
                        }
                    }
                } else {
                    items(people) { person ->
                        if (person.isInside) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                PersonRow(
                                    col1 = "${person.name} ${person.surname}",
                                    col2 = person.idCard,
                                    col3 = person.mobileNumber,
                                    col4 = {
                                        OutlinedButton(onClick = { pendingPerson = person }) {
                                            Text("Mark As Out")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

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
}