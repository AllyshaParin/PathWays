package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonalDetailsScreen(navController: NavController, viewModel: ProfileViewModel) {
    val profile = viewModel.uiState.value

    var name by remember { mutableStateOf(profile.name) }
    var aboutMeText by remember { mutableStateOf(viewModel.aboutMe.value) } // About Me state
    var gender by remember { mutableStateOf(profile.gender) }
    var dob by remember { mutableStateOf(profile.dob) }
    var nationality by remember { mutableStateOf(profile.nationality) }
    var cityState by remember { mutableStateOf(profile.cityState) }
    var postcode by remember { mutableStateOf(profile.postcode) }
    var address by remember { mutableStateOf(profile.address) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Personal Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFE91E63))
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.aboutMe.value = aboutMeText // Save About Me
                    viewModel.updateProfile(profile.copy(
                        name = name, gender = gender, dob = dob,
                        nationality = nationality, cityState = cityState,
                        postcode = postcode, address = address
                    ))
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) { Text("Save", fontWeight = FontWeight.Bold) }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text("Personal Details", fontWeight = FontWeight.Bold, color = Color.DarkGray)

            // --- About Me Field ---
            OutlinedTextField(
                value = aboutMeText,
                onValueChange = { aboutMeText = it },
                label = { Text("About Me") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                minLines = 3
            )

            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Full Name*") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            // Gender Selection
            var genderExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = genderExpanded, onExpandedChange = { genderExpanded = it }) {
                OutlinedTextField(
                    value = gender, onValueChange = {}, readOnly = true,
                    label = { Text("Gender*") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = genderExpanded, onDismissRequest = { genderExpanded = false }) {
                    listOf("Male", "Female").forEach { selection ->
                        DropdownMenuItem(text = { Text(selection) }, onClick = { gender = selection; genderExpanded = false })
                    }
                }
            }

            OutlinedTextField(
                value = dob, onValueChange = { dob = it },
                label = { Text("Date of Birth*") },
                trailingIcon = { Icon(Icons.Default.CalendarMonth, null) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = nationality, onValueChange = { nationality = it },
                label = { Text("Nationality") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Text("Current Address", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            OutlinedTextField(value = cityState, onValueChange = { cityState = it }, label = { Text("City, State*") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(value = postcode, onValueChange = { postcode = it }, label = { Text("Postcode") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}