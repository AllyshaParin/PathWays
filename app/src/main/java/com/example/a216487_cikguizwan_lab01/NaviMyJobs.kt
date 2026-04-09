package com.example.a216487_cikguizwan_lab01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

class NaviMyJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                MyJobsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyJobsScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("All", "Submitted", "Viewed", "Shortlisted", "Rejected")

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(BrandPrimary).statusBarsPadding()) {
                // Search Row
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Search Job Title, Company or Skills", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Icon(Icons.Default.Notifications, null, tint = Color.White)
                }

                // Tabs Row
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color.Red,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color.Red
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) Color.Red else Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ScreenBg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Sort Dropdown Placeholder
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Sort by : Latest Applied", fontSize = 12.sp)
                        Icon(Icons.Default.ArrowDropDown, null, tint = Color.Blue)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Empty State Illustration Placeholder
            Card(
                modifier = Modifier.size(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.WorkOutline, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("You haven't applied any job yet", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                "Get hired by searching from thousands of jobs and start applying today",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Search Jobs")
                }
            }

            Spacer(Modifier.weight(1.5f))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NaviMyJobPreview() {
    A216487_CikguIzwan_Lab01Theme {
        MyJobsScreen()
    }
}