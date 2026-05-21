package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobInMyScreen(navController: NavController, viewModel: ProfileViewModel) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color(0xFF2E7D32)).statusBarsPadding()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.White)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("selangor", color = Color.Black, fontSize = 16.sp)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(onClick = {}, label = { Text("Filter") }, leadingIcon = { Icon(Icons.Default.FilterList, null, modifier = Modifier.size(18.dp)) })
                    AssistChip(onClick = {}, label = { Text("Sort by : Relevance") })
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                JobCard(
                    title = "Purchasing-Junior Executive",
                    company = "Zensho Foods Malaysia",
                    salary = "MYR2,000 - MYR2,500",
                    onApply = {
                        val title = "Purchasing-Junior Executive"
                        val company = "Zensho Foods Malaysia"
                        val salary = "MYR2,000 - MYR2,500"
                        val location = "Petaling Jaya, Selangor"

                        viewModel.calculateSalary(title, "Selangor")
                        viewModel.applyForJob(title, company, location, salary)

                        // FIXED: Safely URL encode arguments matching the NavHost route signature
                        val encTitle = android.net.Uri.encode(title)
                        val encCompany = android.net.Uri.encode(company)
                        val encSalary = android.net.Uri.encode(salary)
                        val encLoc = android.net.Uri.encode(location)
                        navController.navigate("review_application/$encTitle/$encCompany/$encSalary/$encLoc")
                    }
                )
            }
            item {
                JobCard(
                    title = "Office Administrator",
                    company = "PGH Group Trading Sdn Bhd",
                    salary = "MYR3,500 - MYR5,500",
                    onApply = {
                        val title = "Office Administrator"
                        val company = "PGH Group Trading Sdn Bhd"
                        val salary = "MYR3,500 - MYR5,500"
                        val location = "Petaling Jaya, Selangor"

                        viewModel.calculateSalary(title, "Selangor")
                        viewModel.applyForJob(title, company, location, salary)

                        // FIXED: Safely URL encode arguments matching the NavHost route signature
                        val encTitle = android.net.Uri.encode(title)
                        val encCompany = android.net.Uri.encode(company)
                        val encSalary = android.net.Uri.encode(salary)
                        val encLoc = android.net.Uri.encode(location)
                        navController.navigate("review_application/$encTitle/$encCompany/$encSalary/$encLoc")
                    }
                )
            }
            item {
                JobCard(
                    title = "Admin Assistant",
                    company = "Lestari Maju Sdn Bhd",
                    salary = "MYR2,200 - MYR3,000",
                    onApply = {
                        val title = "Admin Assistant"
                        val company = "Lestari Maju Sdn Bhd"
                        val salary = "MYR2,200 - MYR3,000"
                        val location = "Petaling Jaya, Selangor"

                        viewModel.calculateSalary(title, "Selangor")
                        viewModel.applyForJob(title, company, location, salary)

                        // FIXED: Safely URL encode arguments matching the NavHost route signature
                        val encTitle = android.net.Uri.encode(title)
                        val encCompany = android.net.Uri.encode(company)
                        val encSalary = android.net.Uri.encode(salary)
                        val encLoc = android.net.Uri.encode(location)
                        navController.navigate("review_application/$encTitle/$encCompany/$encSalary/$encLoc")
                    }
                )
            }
        }
    }
}

@Composable
fun JobCard(title: String, company: String, salary: String, onApply: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(40.dp).background(Color(0xFFFFEBEE), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Business, null, tint = Color.Red)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(company, color = Color.Gray, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Text(" Petaling Jaya, Selangor", color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                    Text("Chat Available", color = Color(0xFF2E7D32), modifier = Modifier.padding(4.dp), fontSize = 10.sp)
                }
                Surface(color = Color(0xFFFFF3E0), shape = RoundedCornerShape(4.dp)) {
                    Text("Near Train Station", color = Color(0xFFE65100), modifier = Modifier.padding(4.dp), fontSize = 10.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(salary, color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("APPLY", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}