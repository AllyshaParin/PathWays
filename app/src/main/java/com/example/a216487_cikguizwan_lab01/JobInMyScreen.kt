package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun JobInMyScreen(navController: NavController, viewModel: ProfileViewModel) {
    // REMOVED internal Scaffold and topBar logic to prevent "Double Header"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Only keep the sub-header (Filter/Sort) because the Search bar is now in MainActivity
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = {},
                label = { Text("Filter") },
                leadingIcon = { Icon(Icons.Default.FilterList, null, modifier = Modifier.size(18.dp)) }
            )
            AssistChip(
                onClick = {},
                label = { Text("Sort by : Relevance") }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                JobCard(
                    title = "Purchasing-Junior Executive",
                    company = "Zensho Foods Malaysia",
                    salary = "MYR2,000 - MYR2,500",
                    onApply = {
                        viewModel.calculateSalary("Purchasing-Junior Executive", "Selangor")
                        viewModel.applyForJob("Purchasing-Junior Executive", "Selangor", "MYR2,000 - MYR2,500")
                        navController.navigate("review_application")
                    }
                )
            }
            item {
                JobCard(
                    title = "Office Administrator",
                    company = "PGH Group Trading Sdn Bhd",
                    salary = "MYR3,500 - MYR5,500",
                    onApply = {
                        viewModel.calculateSalary("Office Administrator", "Selangor")
                        viewModel.applyForJob("Office Administrator", "Selangor", "MYR3,500 - MYR5,500")
                        navController.navigate("review_application")
                    }
                )
            }
            item {
                JobCard(
                    title = "Admin Assistant",
                    company = "Lestari Maju Sdn Bhd",
                    salary = "MYR2,200 - MYR3,000",
                    onApply = {
                        viewModel.calculateSalary("Admin Assistant", "Selangor")
                        viewModel.applyForJob("Admin Assistant", "Selangor", "MYR2,200 - MYR3,000")
                        navController.navigate("review_application")
                    }
                )
            }
        }
    }
}

// JobCard component remains the same
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
                    Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(4.dp)),
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