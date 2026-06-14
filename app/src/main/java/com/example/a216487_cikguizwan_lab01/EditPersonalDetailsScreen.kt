package com.example.a216487_cikguizwan_lab01

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonalDetailsScreen(navController: NavController, viewModel: ProfileViewModel) {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current
    val profile by viewModel.userProfileState.collectAsStateWithLifecycle()

    // Interactive UI form states linked dynamically to Room database properties
    var name by remember(profile) { mutableStateOf(profile.name) }
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

    // --- Profile Picture URI & File Management States ---
    var capturedImageFile by remember { mutableStateOf<File?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoPath by remember(profile) { mutableStateOf(profile.profilePicturePath) }

    var genderExpanded by remember { mutableStateOf(false) }
    var eduMenuExpanded by remember { mutableStateOf(false) }
    var locMenuExpanded by remember { mutableStateOf(false) }

    val eduOptions = listOf("High School" to 1, "Diploma" to 2, "Bachelor's Degree" to 3, "Master's / PhD" to 4)
    val locOptions = listOf("Selangor / Kuala Lumpur" to 1, "Johor" to 2, "Pulau Pinang" to 3, "Other States" to 4)

    // Hardware camera sensor callback contract with tool-preview guard handling
    val cameraLauncher = if (isPreview) null else {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success && capturedImageFile != null) {
                currentPhotoPath = capturedImageFile!!.absolutePath
            }
        }
    }

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
                    viewModel.aboutMe.value = aboutMeText

                    // Create an updated copy of the Room Entity payload including photo path
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
                        locationCode = selectedLocCode,
                        profilePicturePath = currentPhotoPath // 👈 FIXED: Saves path to Room database record
                    )
                    viewModel.updateProfile(entityPayload)
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
            // =====================================================================
            // TEXT INPUT FORM FIELDS SECTION
            // =====================================================================
            Text("Personal Details", fontWeight = FontWeight.Bold, color = Color.DarkGray)

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