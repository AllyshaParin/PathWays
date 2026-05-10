package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryInputScreen(navController: NavController, viewModel: ProfileViewModel) {
    var jobExpanded by remember { mutableStateOf(false) }
    var stateExpanded by remember { mutableStateOf(false) }

    var selectedJob by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("") }

    val jobs = listOf("It technician", "Rider", "Accountant", "General worker")
    val malaysiaStates = listOf("Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan", "Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah", "Sarawak", "Selangor", "Terengganu", "Kuala Lumpur", "Labuan", "Putrajaya")

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
                "Average Salary Checker in Malaysia",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.Bold
            )
            Text(
                "Discover your earning potential with our Salary Checker. Stay informed on industry trends.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Job Title Dropdown ---
            Text("Job Title", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            ExposedDropdownMenuBox(
                expanded = jobExpanded,
                onExpandedChange = { jobExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedJob,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Enter your job title") },
                    leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = jobExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = jobExpanded, onDismissRequest = { jobExpanded = false }) {
                    jobs.forEach { job ->
                        DropdownMenuItem(
                            text = { Text(job) },
                            onClick = {
                                selectedJob = job
                                jobExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Location Dropdown ---
            Text("Location", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            ExposedDropdownMenuBox(
                expanded = stateExpanded,
                onExpandedChange = { stateExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedState,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Select State") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = stateExpanded, onDismissRequest = { stateExpanded = false }) {
                    malaysiaStates.forEach { state ->
                        DropdownMenuItem(
                            text = { Text(state) },
                            onClick = {
                                selectedState = state
                                stateExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.calculateSalary(selectedJob, selectedState)
                    navController.navigate("salary_result")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                enabled = selectedJob.isNotEmpty() && selectedState.isNotEmpty()
            ) {
                Text("Check Salary", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Decorative "Looking for a Job" section (per your image)
            Text("Looking for a job?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "Upload your resume and all hiring employer will see it!",
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}