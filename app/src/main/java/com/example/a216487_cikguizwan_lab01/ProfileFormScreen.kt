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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFormScreen(navController: NavController, viewModel: ProfileViewModel) {
    // Safely collect Room database updates in alignment with lifecycle changes
    val currentData by viewModel.userProfileState.collectAsStateWithLifecycle()

    // Interactive UI form states
    var name by remember(currentData) { mutableStateOf(currentData.name) }
    var gender by remember(currentData) { mutableStateOf(currentData.gender) }
    var dob by remember(currentData) { mutableStateOf(currentData.dob) }
    var phone by remember(currentData) { mutableStateOf(currentData.phone) }
    var email by remember(currentData) { mutableStateOf(currentData.email) }
    var nationality by remember(currentData) { mutableStateOf(currentData.nationality) }
    var maritalStatus by remember(currentData) { mutableStateOf(currentData.maritalStatus) }
    var workPermit by remember(currentData) { mutableStateOf(currentData.workPermit) }
    var address by remember(currentData) { mutableStateOf(currentData.address) }

    // KNN Vector specific inputs
    var age by remember(currentData) { mutableStateOf(currentData.age.toString()) }
    var selectedEduLevel by remember(currentData) { mutableStateOf(currentData.educationLevel) }
    var selectedLocCode by remember(currentData) { mutableStateOf(currentData.locationCode) }

    var eduMenuExpanded by remember { mutableStateOf(false) }
    var locMenuExpanded by remember { mutableStateOf(false) }

    val eduOptions = listOf("High School" to 1, "Diploma" to 2, "Bachelor's Degree" to 3, "Master's / PhD" to 4)
    val locOptions = listOf("Selangor / Kuala Lumpur" to 1, "Johor" to 2, "Pulau Pinang" to 3, "Other States" to 4)

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
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Personal Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name (as per NRIC/Passport)*") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender*") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age*") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Date of Birth*") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            // --- KNN Quantitative Dropdowns ---
            ExposedDropdownMenuBox(
                expanded = eduMenuExpanded,
                onExpandedChange = { eduMenuExpanded = !eduMenuExpanded }
            ) {
                OutlinedTextField(
                    value = eduOptions.firstOrNull { it.second == selectedEduLevel }?.first ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Highest Education Level*") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = eduMenuExpanded) },
                    // FIXED: Added .menuAnchor() here to anchor the dropdown container correctly
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = eduMenuExpanded,
                    onDismissRequest = { eduMenuExpanded = false }
                ) {
                    eduOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.first) },
                            onClick = {
                                selectedEduLevel = option.second
                                eduMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = locMenuExpanded,
                onExpandedChange = { locMenuExpanded = !locMenuExpanded }
            ) {
                OutlinedTextField(
                    value = locOptions.firstOrNull { it.second == selectedLocCode }?.first ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Preferred Location Zone*") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locMenuExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = locMenuExpanded,
                    onDismissRequest = { locMenuExpanded = false }
                ) {
                    locOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.first) },
                            onClick = {
                                selectedLocCode = option.second
                                locMenuExpanded = false
                            }
                        )
                    }
                }
            }
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

            Button(
                onClick = {
                    val entityPayload = currentData.copy(
                        name = name,
                        gender = gender,
                        dob = dob,
                        phone = phone,
                        email = email,
                        nationality = nationality,
                        maritalStatus = maritalStatus,
                        workPermit = workPermit,
                        address = address,
                        age = age.toIntOrNull() ?: currentData.age,
                        educationLevel = selectedEduLevel,
                        locationCode = selectedLocCode
                        // Note: cityState and country remain untouched via copy()
                    )
                    viewModel.updateProfile(entityPayload)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                Text("SAVE", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}