package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerToolsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Career Tools", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE91E63) // Pinkish-Red header
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F8F8))
                .verticalScroll(rememberScrollState())
        ) {
            // --- Tool 1: Drop Resume (Non-functional) ---
            ToolItem(
                title = "Drop Resume",
                description = "Upload your resume and all hiring employers will see it.",
                isEnabled = false
            )

            // --- Tool 2: Don't have a resume? (Non-functional) ---
            ToolItem(
                title = "Don't have a resume?",
                description = "We've got you covered. Let's create your resume now!",
                isEnabled = false
            )

            // --- Tool 3: Job Matcher (Non-functional) ---
            ToolItem(
                title = "Job Matcher",
                description = "You can find the best matching job as easily as 1, 2, 3!",
                isEnabled = false
            )

            // --- Tool 4: ATS Resume Checker (Non-functional) ---
            ToolItem(
                title = "ATS Resume Checker",
                description = "Scan your resume to measure your resume compatibility.",
                isEnabled = false
            )

            // --- Tool 5: Salary Calculator (FUNCTIONAL) ---
            ToolItem(
                title = "Salary Calculator",
                description = "Find current salary market on specific jobs.",
                isEnabled = true,
                onTryNow = { navController.navigate("salary_input") }
            )
        }
    }
}

@Composable
fun ToolItem(
    title: String,
    description: String,
    isEnabled: Boolean,
    onTryNow: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Placeholder (Circular background like the screenshot)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFFFEBEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // You can add individual icons here based on the title
                Text("📁", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onTryNow,
                    enabled = isEnabled,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = "Try Now  ▶",
                        color = if (isEnabled) Color(0xFF3F51B5) else Color.LightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}