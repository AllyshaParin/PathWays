package com.example.a216487_cikguizwan_lab01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

class WFHActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                WFHScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WFHScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Career Collection", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BrandPrimary)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(ScreenBg),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Blue WFH Header Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("WFH Jobs", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1565C0))
                            Text("Work When You Want", color = Color.Gray)
                        }
                        Icon(Icons.Default.HomeWork, null, modifier = Modifier.size(48.dp), tint = Color(0xFF1565C0))
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("93 Jobs Available", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Select All", fontSize = 14.sp)
                        Checkbox(checked = false, onCheckedChange = {})
                    }
                }
            }

            // List of WFH Jobs
            items(5) {
                WFHJobCard()
            }
        }
    }
}

@Composable
fun WFHJobCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF5F5F5)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Business, null, tint = Color.Gray)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("WFH Interpreter | MAN-ENG MY", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Agensi Pekerjaan JobScoper Sdn Bhd", color = Color.Gray, fontSize = 13.sp)
                }
                Checkbox(checked = false, onCheckedChange = {})
            }
            Spacer(Modifier.height(8.dp))
            Text("$ MYR7,000 - MYR9,000 Per Month", color = Color.Red, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                Text(" Bandar Kuala Lumpur, WP Kuala Lumpur", color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("One-Click Apply", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WFHPreview() {
    A216487_CikguIzwan_Lab01Theme {
        WFHScreen(onBack = {})
    }
}