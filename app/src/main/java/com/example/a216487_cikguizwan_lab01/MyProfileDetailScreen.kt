package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun MyProfileDetailScreen(navController: NavController, viewModel: ProfileViewModel) {
    // Safely observe the live Room database state flow stream
    val data by viewModel.userProfileState.collectAsStateWithLifecycle()

    // Extra non-entity properties from your ViewModel
    val aboutMeText by viewModel.aboutMe
    val skillsList by viewModel.skills
    val languagesList = viewModel.languages

    // Map the integer code from Room back into a readable string
    val readableEduLevel = when (data.educationLevel) {
        1 -> "High School"
        2 -> "Diploma"
        3 -> "Bachelor's Degree"
        4 -> "Master's / PhD"
        else -> "Not Specified"
    }

    Scaffold(
        // FIX: Forces the Scaffold to completely drop the automatic top status bar padding gap
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F8F8))
                .verticalScroll(rememberScrollState())
        ) {
            // =================================================================
            // STREAMLINED HEADER ROW (Replaces TopAppBar for compact look)
            // =================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE91E63)) // Matching your brand pink color
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "My Profile",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // =================================================================
            // SECTION 1: Personal Details (Room Database Source)
            // =================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Personal Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF5F5F5),
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                navController.navigate("edit_profile")
                            }
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            modifier = Modifier.padding(8.dp),
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Room Data Fields
                ProfileInfoRow("Full Name (as per NRIC/Passport)", data.name)

                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        ProfileInfoRow("Age", data.age.toString())
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ProfileInfoRow("Gender", data.gender)
                    }
                }

                ProfileInfoRow("Highest Education Level", readableEduLevel)

                ProfileInfoRow("Date of Birth", data.dob)
                ProfileInfoRow("Phone Number", data.phone)
                ProfileInfoRow("Personal Email", data.email)
                ProfileInfoRow("Marital Status", data.maritalStatus)
                ProfileInfoRow("Nationality", data.nationality)
                ProfileInfoRow("Do You Need Working Permit?", data.workPermit)

                val fullAddress = listOf(data.address, data.cityState, data.postcode, data.country)
                    .filter { it.isNotBlank() }
                    .joinToString(", ")

                ProfileInfoRow("Current Address", fullAddress.ifEmpty { "-" })
            }

            Spacer(modifier = Modifier.height(12.dp))

            // =================================================================
            // SECTION 2: Extra Data Fields (ViewModel Source)
            // =================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Professional Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProfileInfoRow("About Me", aboutMeText)

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Skills & Talents",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    skillsList.forEach { skill ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text(skill, fontSize = 12.sp) },
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Languages Known",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))

                languagesList.forEach { language ->
                    Text(
                        text = "• $language",
                        fontSize = 15.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.ifEmpty { "-" },
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}