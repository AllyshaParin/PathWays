package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryResultScreen(navController: NavController, viewModel: ProfileViewModel) {
    val result = viewModel.salaryState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Salary Checker", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
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
                text = "How Much Can You Earn as ${result.jobTitle} in ${result.location}?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- The Result Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Pay Range", color = Color.Gray, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "RM ${result.minPay} - RM ${result.maxPay}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2D3E50)
                    )
                    Text("/ Month", color = Color.Gray)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "The average total monthly income for ${result.jobTitle} in ${result.location} is RM ${result.averagePay}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* Action */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text("Find This Job Now!")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Dynamic Description text matching the image ---
            Text(
                text = "The ${result.jobTitle} position in ${result.location} typically pays between RM ${result.minPay} to RM ${result.maxPay} per month. In this role, the candidate will be responsible for providing support and fulfilling duties required of a ${result.jobTitle}. The job requires strong knowledge and specific skills relevant to the industry in Malaysia. Additionally, the candidate should have excellent problem-solving skills and the ability to work independently or as part of a team.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Share Button ---
            OutlinedButton(
                onClick = { /* Share Logic */ },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFFE91E63))
                Spacer(Modifier.width(8.dp))
                Text("Share", color = Color.Gray)
            }
        }
    }
}