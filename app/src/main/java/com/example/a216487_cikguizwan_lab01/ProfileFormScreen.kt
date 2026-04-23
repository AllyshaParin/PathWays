package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFormScreen(navController: NavController, viewModel: ProfileViewModel) {
    val currentData = viewModel.uiState.value

    var name by remember { mutableStateOf(currentData.name) }
    var phone by remember { mutableStateOf(currentData.phone) }
    var email by remember { mutableStateOf(currentData.email) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Update Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        // --- Input Fields ---
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(32.dp))

        // --- Action Buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Gap between buttons
        ) {
            // CANCEL BUTTON (Task 1: Handling transitions)
            OutlinedButton(
                onClick = {
                    navController.popBackStack() // Just goes back, no data saved
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Cancel")
            }

            // SAVE BUTTON (Task 2: ViewModel Update)
            Button(
                onClick = {
                    viewModel.updateProfile(name, phone, email) // Save to ViewModel
                    navController.navigate("success") // Go to Success Screen
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save")
            }
        }
    }
}