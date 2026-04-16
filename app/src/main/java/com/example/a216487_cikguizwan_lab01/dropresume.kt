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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme

class DropResumeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                DropResumeScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropResumeScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Drop Resume", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            contentPadding = PaddingValues(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "Drop Your Resume",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F)
                )
                Text(
                    "#KerjaCariKita",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Red
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Want your resume to be viewed by more than 10,000 employers? Drop your resume here!",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(24.dp))
            }

            // Form Fields
            item {
                ResumeDropdownField("When you can start working?", "I can start immediately")
                Spacer(Modifier.height(20.dp))

                Text("Choose 1 Resume to Drop", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, null, tint = Color.Gray)
                        Spacer(Modifier.width(8.dp))
                        Text("resume_Nurallysha.pdf", Modifier.weight(1f))
                        Icon(Icons.Default.Visibility, null, tint = Color.Red)
                    }
                }
                TextButton(onClick = {}, contentPadding = PaddingValues(0.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AddCircle, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                        Text(" Upload New Resume", color = Color.Red)
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            item {
                ResumeTextField("What job are you searching?", "e.g Admin Assistant")
                ResumeDropdownField("Select your preferred industry", "Other industries")
                ResumeDropdownField("Where do you want to work?", "Malaysia")
                ResumeTextField("Search a country, city or state", "")

                Spacer(Modifier.height(30.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Submit", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(40.dp))
            }

            // Footer Companies
            item {
                Text("Top Companies Hiring Right Now", fontWeight = FontWeight.Bold, color = Color(0xFF37474F))
                Spacer(Modifier.height(16.dp))
                // Simple placeholder for company logos row
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("CTOS", fontWeight = FontWeight.Black, color = Color(0xFF006064))
                    Text("GENTING", fontWeight = FontWeight.Black, color = Color.Red)
                    Text("AEON", fontWeight = FontWeight.Black, color = Color(0xFF880E4F))
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ResumeDropdownField(label: String, selected: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedCard(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(selected)
                Icon(Icons.Default.ArrowDropDown, null)
            }
        }
    }
}

@Composable
fun ResumeTextField(label: String, placeholder: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DropResumePreview() {
    A216487_CikguIzwan_Lab01Theme {
        DropResumeScreen(onBack = {})
    }
}