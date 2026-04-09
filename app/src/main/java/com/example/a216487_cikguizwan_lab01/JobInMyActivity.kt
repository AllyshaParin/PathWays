package com.example.a216487_cikguizwan_lab01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme

class JobInMyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                JobInMyScreen(onBack = { finish() })
            }
        }
    }
}

@Composable
fun JobInMyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(BrandPrimary).statusBarsPadding()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    // Search Bar styled like your screenshot
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
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp) // Use modifier for size
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("selangor", color = Color.Black, fontSize = 16.sp)
                        }
                    }
                }
                // Filter and Sort row
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
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(ScreenBg),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { JobCard("Purchasing-Junior Executive", "Zensho Foods Malaysia", "MYR2,000 - MYR2,500") }
            item { JobCard("Office Administrator", "PGH Group Trading Sdn Bhd", "MYR3,500 - MYR5,500") }
            item { JobCard("Admin Assistant", "Lestari Maju Sdn Bhd", "MYR2,200 - MYR3,000") }
        }
    }
}

@Composable
fun JobCard(title: String, company: String, salary: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Placeholder for Logo
                Box(Modifier.size(40.dp).background(Color.LightGray, RoundedCornerShape(4.dp)))
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
            // Tags
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
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)), // Red apply button
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("APPLY", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JobInMyPreview() {
    A216487_CikguIzwan_Lab01Theme {
        JobInMyScreen(onBack = {})
    }
}