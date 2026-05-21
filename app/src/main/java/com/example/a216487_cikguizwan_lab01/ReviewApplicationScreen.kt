package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewApplicationScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    // Dynamic arguments passed from the clicked job recommendation
    jobTitle: String = "Unknown Job",
    company: String = "Unknown Company",
    salary: String = "Unspecified Salary",
    location: String = "Malaysia"
) {
    // Collect Room state accurately to dynamically fill application reviews
    val profileData by viewModel.userProfileState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Review Application", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFE91E63))
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    // DYNAMIC: Submits the exact job the user clicked on into Room DB!
                    viewModel.applyForJob(
                        title = jobTitle,
                        company = company,
                        location = location,
                        salary = salary
                    )
                    // Clear backstack to home view safely
                    navController.popBackStack("home_content", false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirm & Submit Application", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Job Detail Quick Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Applying For:", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    Text(jobTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                    Text(company, fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(salary, color = Color.Red, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text(location, color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }

            Text(
                text = "Please verify your profile information before submitting to employers.",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Applicant Profile Summary", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color.Black)
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

                    ReviewRow(label = "Full Name", value = profileData.name)
                    ReviewRow(label = "Phone Number", value = profileData.phone)
                    ReviewRow(label = "Email Address", value = profileData.email)
                    ReviewRow(label = "Gender", value = profileData.gender)
                    ReviewRow(label = "Age Metric", value = "${profileData.age} years old")
                    ReviewRow(label = "Nationality", value = profileData.nationality)
                    ReviewRow(label = "Marital Status", value = profileData.maritalStatus)
                    ReviewRow(label = "Work Permit Status", value = profileData.workPermit)

                    val combinedAddress = "${profileData.address}, ${profileData.postcode} ${profileData.cityState}, ${profileData.country}"
                    ReviewRow(label = "Contact Address", value = combinedAddress)
                }
            }
        }
    }
}

@Composable
fun ReviewRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value.ifEmpty { "Not Provided" }, fontSize = 15.sp, color = Color.Black, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
    }
}