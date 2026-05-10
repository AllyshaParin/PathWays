package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewApplicationScreen(navController: NavController, viewModel: ProfileViewModel) {
    val profile = viewModel.uiState.value
    val job = viewModel.salaryState.value
    val languages = viewModel.languages

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Review Application", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFE91E63))
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextButton(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) {
                        Text("Cancel", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { navController.navigate("navimyjob") },
                        modifier = Modifier.weight(1.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Submit Application", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF5F5F5)).verticalScroll(rememberScrollState())
        ) {
            // --- Job Summary ---
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Surface(modifier = Modifier.size(50.dp).border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))) {
                        Icon(Icons.Default.Business, null, modifier = Modifier.padding(8.dp), tint = Color.Red)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(job.jobTitle.ifEmpty { "Sales Advisor" }, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Super Ceramic Tiles & Design Sdn Bhd", color = Color.Gray, fontSize = 14.sp)
                        Text("RM ${job.minPay} - RM ${job.maxPay} Per Month", color = Color.Red, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // --- Personal Details (Expanded) ---
            ReviewSectionHeader("Personal Details", onEdit = { navController.navigate("edit_personal_details") })
            Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
                DetailItem("About Me", viewModel.aboutMe.value)
                DetailItem("Full Name", profile.name)
                DetailItem("Gender", profile.gender)
                DetailItem("Date of Birth", profile.dob)
                DetailItem("Nationality", profile.nationality)
                DetailItem("Address", "${profile.address}, ${profile.postcode}, ${profile.cityState}")
            }

            // --- Contact (Edit Button Removed) ---
            PaddingValues(16.dp)
            Text(
                "Contact",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF2D3E50)
            )
            Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
                DetailItem("Email", profile.email)
                DetailItem("Phone Number", profile.phone)
            }

            // --- Language Proficiency (Dynamic) ---
            ReviewSectionHeader("Language Proficiency", onEdit = { navController.navigate("edit_skills") })
                try {
                    navController.navigate("edit_skills")
                } catch (e: Exception) {
                    println("Navigation Error: ${e.message}")
                }

            Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
                viewModel.languages.forEach { lang: String ->
                    Text(
                        text = lang,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun ReviewSectionHeader(title: String, onEdit: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2D3E50))
        OutlinedButton(
            onClick = onEdit,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color.Red)
        ) {
            Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp), tint = Color.Red)
            Spacer(Modifier.width(4.dp))
            Text("Edit", color = Color.Red, fontSize = 12.sp)
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(value.ifEmpty { "-" }, color = Color.DarkGray, fontSize = 15.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewApplicationPreview() {
    A216487_CikguIzwan_Lab01Theme {
        // Create a mock NavController for the preview
        val mockNavController = androidx.navigation.compose.rememberNavController()

        // Initialize the real ViewModel (it will use the default data we defined)
        val mockViewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

        // Manually set a mock job so the Job Card isn't empty in the preview
        mockViewModel.calculateSalary("Accountant", "Selangor")

        ReviewApplicationScreen(
            navController = mockNavController,
            viewModel = mockViewModel
        )
    }
}