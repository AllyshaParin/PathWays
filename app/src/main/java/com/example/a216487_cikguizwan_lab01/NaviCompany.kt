package com.example.a216487_cikguizwan_lab01

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.rotate
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme

class NaviCompanyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                CompanyScreenWithNav()
            }
        }
    }
}

@Composable
fun CompanyScreenWithNav() {
    val context = LocalContext.current
    val selectedTab = "Company"

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // Task 1
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        placeholder = {
                            Text("Try search company name", fontSize = 14.sp)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.outline)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, "Clear", tint = MaterialTheme.colorScheme.outline)
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        singleLine = true
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(Icons.Default.Notifications, null, tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        bottomBar = {
            // Task: Persistent Bottom Navigation
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { label ->
                    when (label) {
                        "Home" -> {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            context.startActivity(intent)
                        }
                        "Chat" -> context.startActivity(Intent(context, NaviChatActivity::class.java))
                        "My Jobs" -> context.startActivity(Intent(context, NaviMyJobActivity::class.java))
                        "Profile" -> context.startActivity(Intent(context, NaviProfileActivity::class.java))
                        "Company" -> { /* Already here */ }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Section 1: Auto-Scrolling Logos
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Find the best places to work",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Determine if it's the right fit for you",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                    AutoScrollingLogos()
                }
            }

            // Section 2: Discover New Companies
            item { CompanySectionHeader("Discover New Companies") }
            itemsIndexed(listOf("CTR Piling Sdn Bhd", "Proacc System Consulting", "Glp International")) { index, name ->
                CompanyListItem(index + 1, name, "Industry Category")
            }

            item { Spacer(Modifier.height(16.dp)) }

            // Section 3: Most Viewed
            item { CompanySectionHeader("Most Viewed (Past 7 days)") }
            itemsIndexed(listOf("AEON Co. (M) Bhd.", "Watsons Store", "Genting Group")) { index, name ->
                CompanyListItem(index + 1, name, "Retail / Merchandise")
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun AutoScrollingLogos() {
    val scrollState = rememberScrollState()
    val logos = listOf("APOLLO", "Schlumberger", "Caixun", "GENTING", "AEON", "Tealive")

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
            .horizontalScroll(scrollState, enabled = false),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (logos + logos).forEach { logoName ->
            Text(
                text = logoName,
                modifier = Modifier.padding(horizontal = 30.dp),
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun CompanySectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            "See All",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun CompanyListItem(rank: Int, name: String, industry: String) {
    // Task 3: State to track if the card is expanded
    var isExpanded by remember { mutableStateOf(false) }

    // Task 3: Animation for the rotation of the arrow icon
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 180f,
        animationSpec = tween(durationMillis = 300), // Standard smooth 300ms transition
        label = "ArrowRotation"
    )
    //task 2
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .animateContentSize( // Task 3: Automatically animates height change
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { isExpanded = !isExpanded }, // Toggle expansion on click
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    rank.toString(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(30.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Business, null, tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Icon(
                            Icons.Default.CheckCircle,
                            null,
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(14.dp).padding(start = 4.dp)
                        )
                    }
                    Text(industry, color = MaterialTheme.colorScheme.outline, fontSize = 13.sp)
                }

                // Task 3: Animate the arrow icon
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier.rotate(rotationState)
                )
            }

            // Task 3: This content only appears when the card is expanded
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Company Overview:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "This company is a leader in $industry, offering great benefits and a collaborative work environment. Click to view 24 active job openings.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
                Button(
                    onClick = { /* Navigate to Details */ },
                    modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("View Profile", fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CompanyPreview() {
    A216487_CikguIzwan_Lab01Theme {
        CompanyScreenWithNav()
    }
}