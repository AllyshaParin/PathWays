package com.example.a216487_cikguizwan_lab01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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

class NaviCompanyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                CompanyScreen()
            }
        }
    }
}

@Composable
fun CompanyScreen() {
    Scaffold(
        topBar = {
            // Header Section
            Box(modifier = Modifier.fillMaxWidth().background(BrandPrimary).statusBarsPadding().padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.weight(1f).height(45.dp), shape = RoundedCornerShape(25.dp), color = Color.White) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                            Icon(Icons.Default.Search, null, tint = Color.Gray)
                            Spacer(Modifier.width(8.dp))
                            Text("Try search company name", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Icon(Icons.Default.Notifications, null, tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        // LazyColumn provides the scrollability for the entire page
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ScreenBg)
        ) {
            // Section 1: Moving Logos
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Find the best places to work", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, textAlign = TextAlign.Center)
                    Text("To determine if it's the right fit for you", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))

                    Spacer(Modifier.height(24.dp))

                    AutoScrollingLogos()
                }
            }

            // Section 2: Discover New Companies
            item { CompanySectionHeader("Discover New Companies on Maukerja") }
            itemsIndexed(listOf("CTR Piling Sdn Bhd", "Proacc System Consulting", "Glp International", "Tech Solutions", "Creative Agency")) { index, name ->
                CompanyListItem(index + 1, name, "Industry Category")
            }

            item { Spacer(Modifier.height(16.dp)) }

            // Section 3: Most Viewed
            item { CompanySectionHeader("Most Viewed in Past 7 days") }
            itemsIndexed(listOf("AEON Co. (M) Bhd.", "Watsons Personal Care Stores", "Genting Group", "Tealive Malaysia", "ZUS Coffee")) { index, name ->
                CompanyListItem(index + 1, name, "Retail / Merchandise")
            }

            // Extra padding at the bottom for smooth scrolling
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun AutoScrollingLogos() {
    val scrollState = rememberScrollState()
    val logos = listOf("APOLLO", "Schlumberger", "Caixun", "GENTING", "AEON", "Tealive", "Watsons", "Baskin Robbins", "CTOS", "ZUS")

    LaunchedEffect(Unit) {
        while (true) {
            scrollState.animateScrollTo(
                value = scrollState.maxValue,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 15000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
            scrollState.scrollTo(0)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState, enabled = false), // Users cannot manually swipe the logos
        verticalAlignment = Alignment.CenterVertically
    ) {
        (logos + logos).forEach { logoName ->
            Text(
                text = logoName,
                modifier = Modifier.padding(horizontal = 30.dp),
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun CompanySectionHeader(title: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text("See All", color = Color.Red, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CompanyListItem(rank: Int, name: String, industry: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(rank.toString(), fontWeight = FontWeight.Bold, modifier = Modifier.width(30.dp))
        Box(modifier = Modifier.size(45.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Business, null, tint = Color.Gray)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2196F3), modifier = Modifier.size(14.dp).padding(start = 4.dp))
            }
            Text(industry, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CompanyPreview() {
    A216487_CikguIzwan_Lab01Theme {
        CompanyScreen()
    }
}