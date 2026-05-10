package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditJobPreferencesScreen(navController: NavController, viewModel: ProfileViewModel) {
    // Local state for the form
    var jobLookingFor by remember { mutableStateOf("") }
    var expectedSalary by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Job Preferences", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFE91E63))
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    // Logic to save data to viewModel could go here
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("What job are you looking for?*", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            OutlinedTextField(
                value = jobLookingFor,
                onValueChange = { jobLookingFor = it },
                placeholder = { Text("e.g. Human Resources") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Expected Salary*", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "MYR",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.width(90.dp)
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = expectedSalary,
                    onValueChange = { expectedSalary = it },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.weight(1f)
                )
            }
            Text("Monthly salary", color = Color.Gray, fontSize = 12.sp)
        }
    }
}