package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFormScreen(navController: NavController, viewModel: ProfileViewModel) {
    val currentData = viewModel.uiState.value

    // State management for all fields
    var name by remember { mutableStateOf(currentData.name) }
    var gender by remember { mutableStateOf(currentData.gender) }
    var dob by remember { mutableStateOf(currentData.dob) }
    var phone by remember { mutableStateOf(currentData.phone) }
    var email by remember { mutableStateOf(currentData.email) }
    var maritalStatus by remember { mutableStateOf(currentData.maritalStatus) }
    var nationality by remember { mutableStateOf(currentData.nationality) }
    var workPermit by remember { mutableStateOf(currentData.workPermit) }
    var address by remember { mutableStateOf(currentData.address) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Personal Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE91E63))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Personal Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(16.dp))

            // --- Input Fields ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name (as per NRIC/Passport)*") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Gender*") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Date of Birth*") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = nationality,
                onValueChange = { nationality = it },
                label = { Text("Nationality*") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))
            Text("Additional Info", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = maritalStatus,
                onValueChange = { maritalStatus = it },
                label = { Text("Marital Status") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = workPermit,
                onValueChange = { workPermit = it },
                label = { Text("Work Permit") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Current Address") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(Modifier.height(32.dp))

            // --- Action Button ---
            Button(
                onClick = {
                    val updatedData = currentData.copy(
                        name = name,
                        gender = gender,
                        dob = dob,
                        phone = phone,
                        email = email,
                        maritalStatus = maritalStatus,
                        nationality = nationality,
                        workPermit = workPermit,
                        address = address
                    )
                    viewModel.updateProfile(updatedData)
                    navController.navigate("success")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                Text("Save", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}