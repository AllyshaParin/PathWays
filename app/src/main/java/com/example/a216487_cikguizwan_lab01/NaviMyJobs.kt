package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyJobsScreenWithNav(navController: NavController, viewModel: ProfileViewModel) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("All", "Submitted", "Viewed", "Shortlisted", "Rejected")

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // --- 1. FIXED TABS AT TOP ---
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Color(0xFF2E7D32),
            edgePadding = 16.dp,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xFF2E7D32)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTabIndex == index) Color(0xFF2E7D32) else Color.Gray
                        )
                    }
                )
            }
        }

        // --- 2. SCROLLABLE LIST AREA ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sort Dropdown
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sort: Latest Applied", fontSize = 12.sp)
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                }
            }

            // --- 3. LIST LOGIC ---
            if (viewModel.appliedJobs.isEmpty()) {
                // EMPTY STATE
                Spacer(Modifier.height(80.dp))
                Icon(Icons.Default.WorkOutline, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                Spacer(Modifier.height(16.dp))
                Text("No Applications", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(
                    "You haven't applied for any jobs yet.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp),
                    fontSize = 14.sp
                )
            } else {
                // LIST OF JOBS
                Text(
                    "Submitted Applications (${viewModel.appliedJobs.size})",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                viewModel.appliedJobs.forEach { job ->
                    AppliedJobCard(job)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun AppliedJobCard(job: JobApplication) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(45.dp),
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Business, null, tint = Color.Red, modifier = Modifier.padding(8.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(job.jobTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Maukerja Recruitment Sdn Bhd", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(job.salaryRange, color = Color.Red, fontWeight = FontWeight.Bold)
            Text(job.location, color = Color.Gray, fontSize = 13.sp)

            Spacer(Modifier.height(16.dp))

            Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(4.dp)) {
                Text(
                    text = job.status,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}