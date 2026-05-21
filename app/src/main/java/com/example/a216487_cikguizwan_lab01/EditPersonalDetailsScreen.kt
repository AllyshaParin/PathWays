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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonalDetailsScreen(navController: NavController, viewModel: ProfileViewModel) {
    // Collect the Room data flow safely into a state value that Compose can read
    val profile by viewModel.userProfileState.collectAsStateWithLifecycle()

    // Interactive UI form states linked dynamically to Room database properties
    var name by remember(profile) { mutableStateOf(profile.name) }
    // ALIGNED: Listens to the ViewModel text initialization block dynamically
    var aboutMeText by remember(viewModel.aboutMe.value) { mutableStateOf(viewModel.aboutMe.value) }
    var gender by remember(profile) { mutableStateOf(profile.gender) }
    var dob by remember(profile) { mutableStateOf(profile.dob) }
    var nationality by remember(profile) { mutableStateOf(profile.nationality) }
    var cityState by remember(profile) { mutableStateOf(profile.cityState) }
    var postcode by remember(profile) { mutableStateOf(profile.postcode) }
    var address by remember(profile) { mutableStateOf(profile.address) }

    // KNN-specific quantitative features
    var age by remember(profile) { mutableStateOf(profile.age.toString()) }
    var selectedEduLevel by remember(profile) { mutableStateOf(profile.educationLevel) }
    var selectedLocCode by remember(profile) { mutableStateOf(profile.locationCode) }

    var genderExpanded by remember { mutableStateOf(false) }
    var eduMenuExpanded by remember { mutableStateOf(false) }
    var locMenuExpanded by remember { mutableStateOf(false) }

    val eduOptions = listOf("High School" to 1, "Diploma" to 2, "Bachelor's Degree" to 3, "Master's / PhD" to 4)
    val locOptions = listOf("Selangor / Kuala Lumpur" to 1, "Johor" to 2, "Pulau Pinang" to 3, "Other States" to 4)

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
                    viewModel.aboutMe.value = aboutMeText // Save About Me text safely to ViewModel state

                    // Create an updated copy of the Room Entity payload
                    val entityPayload = profile.copy(
                        name = name,
                        gender = gender,
                        dob = dob,
                        nationality = nationality,
                        cityState = cityState,
                        postcode = postcode,
                        address = address,
                        age = age.toIntOrNull() ?: profile.age,
                        educationLevel = selectedEduLevel,
                        locationCode = selectedLocCode
                    )
                    viewModel.updateProfile(entityPayload) // Save and trigger automatic KNN live-refresh
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                Text("SAVE", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Personal Details", fontWeight = FontWeight.Bold, color = Color.DarkGray)

            // --- About Me Field ---
            OutlinedTextField(
                value = aboutMeText,
                onValueChange = { aboutMeText = it },
                label = { Text("About Me") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                minLines = 3
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Split Row: Gender Selection and Age Number Vector Input
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Gender*") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            listOf("Male", "Female").forEach { selection ->
                                DropdownMenuItem(
                                    text = { Text(selection) },
                                    onClick = {
                                        gender = selection
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age*") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Date of Birth*") },
                trailingIcon = { Icon(Icons.Default.CalendarMonth, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // --- KNN Quantitative Dropdowns ---
            Spacer(modifier = Modifier.height(4.dp))
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

            Spacer(modifier = Modifier.height(12.dp))
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
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nationality,
                onValueChange = { nationality = it },
                label = { Text("Nationality") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Text("Current Address", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            OutlinedTextField(
                value = cityState,
                onValueChange = { cityState = it },
                label = { Text("City, State*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            OutlinedTextField(
                value = postcode,
                onValueChange = { postcode = it },
                label = { Text("Postcode") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}