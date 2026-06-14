package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteJobsFeedScreen(navController: NavController, viewModel: ProfileViewModel) {

    // Auto-fetch data from the web API whenever this screen mounts
    LaunchedEffect(Unit) {
        viewModel.fetchLiveRemoteJobs()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Global Remote Jobs", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0288D1))
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            if (viewModel.isApiLoading.value) {
                // Loading Spinner while waiting for internet data response
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color(0xFF0288D1))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fetching global live job feeds...", color = Color.Gray)
                }
            } else if (viewModel.remoteJobsList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Failed to load live jobs. Check internet connection.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.remoteJobsList) { job ->
                        InternetJobCard(job)
                    }
                }
            }
        }
    }
}

@Composable
fun InternetJobCard(job: NetworkJob) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = Color(0xFF0288D1),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(job.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1)
                    Text(job.company_name, color = Color.DarkGray, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(job.candidate_required_location, color = Color.Gray, fontSize = 12.sp)
                }

                Text(
                    text = job.salary?.ifBlank { "Undisclosed" } ?: "Competitive Pay",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}