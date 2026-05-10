package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileDetailScreen(navController: NavController, viewModel: ProfileViewModel) {
    // We use 'by' to observe the state so the UI recomposes when data changes
    val data by viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", color = Color.White) },
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
                .background(Color(0xFFF8F8F8)) // Light grey background like the app
                .verticalScroll(rememberScrollState())
        ) {
            // White Card Section for Personal Details
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

                    // The Pencil Icon Button to navigate to the Form
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF5F5F5),
                        modifier = Modifier.size(40.dp).clickable {
                            navController.navigate("edit_personal_details")
                        }
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.padding(8.dp),
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Displaying the Shared Data
                ProfileInfoRow("Full Name (as per NRIC/Passport)", data.name)
                ProfileInfoRow("Gender", data.gender)
                ProfileInfoRow("Date of Birth", data.dob)
                ProfileInfoRow("Phone Number", data.phone)
                ProfileInfoRow("Personal Email", data.email)
                ProfileInfoRow("Marital Status", data.maritalStatus)
                ProfileInfoRow("Nationality", data.nationality)
                ProfileInfoRow("Do You Need Working Permit?", data.workPermit)

                // Combined Address Display
                val fullAddress = "${data.address}, ${data.cityState}, ${data.postcode}, ${data.country}"
                ProfileInfoRow("Current Address", fullAddress)
            }
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
