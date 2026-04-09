package com.example.a216487_cikguizwan_lab01

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme
import kotlinx.coroutines.launch

// --- BRANDING COLORS ---
val BrandPrimary = Color(0xFF4FEA54)
val BrandAccent = Color(0xFF00C853)
val CardBg = Color(0xFFFFFFFF)
val ScreenBg = Color(0xFFF0F2F5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                AppNavigator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator() {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf("Home") }

    // 1. SEARCH STATE (The source of truth for search bar)
    var searchQuery by remember { mutableStateOf("") }

    // 2. READY FOR WORK STATE
    var isReadyForWork by remember { mutableStateOf(false) }

    // Bottom Sheet State
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Placing HeaderSection here makes the search bar permanent across screens
            HeaderSection(
                query = searchQuery,
                onQueryChange = { newValue -> searchQuery = newValue }
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = currentScreen,
                onTabSelected = { label ->
                    // Navigation logic
                    when (label) {
                        "Home" -> currentScreen = "Home"
                        "Chat" -> currentScreen = "Chat"
                        "Company" -> currentScreen = "Company"
                        "Profile" -> currentScreen = "Profile"
                        "My Jobs" -> {
                            // Note: If you want BottomNav to stay, call the Composable
                            // instead of starting a new Activity.
                            context.startActivity(Intent(context, NaviMyJobActivity::class.java))
                        }
                        else -> currentScreen = label
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "Home" -> MainContent(
                    searchQuery = searchQuery,
                    onSearchChanged = { searchQuery = it },
                    onNavigate = { currentScreen = it },
                    isReady = isReadyForWork,
                    onToggleHire = { newState ->
                        isReadyForWork = newState
                        if (newState) showBottomSheet = true
                    }
                )
                // If you call these as Composables, the BottomNav stays visible!
//                "Chat" -> NaviChatActivity()//.ChatScreen()
//                "Company" -> CompanyScreen()
//                "Profile" -> NaviProfileActivity()//.ProfileScreen()
//
//                else -> DetailPage(pageName = currentScreen, onBack = { currentScreen = "Home" })
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = Color.White
                ) {
                    ReadyForWorkPopup(
                        isReady = isReadyForWork,
                        onReadyChange = { isReadyForWork = it },
                        onDismiss = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    onNavigate: (String) -> Unit,
    isReady: Boolean,
    onToggleHire: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatusToggleCard(
                    isChecked = isReady,
                    onCheckedChange = onToggleHire
                )
            }
            item {
                QuickActions(onActionClick = { label ->
                    when (label) {
                        "Jobs In Malaysia" -> context.startActivity(Intent(context, JobInMyActivity::class.java))
                        "Upload Resume" -> context.startActivity(Intent(context, DropResumeActivity::class.java))
                        "Chat" -> context.startActivity(Intent(context, NaviChatActivity::class.java))
                        else -> onNavigate(label)
                    }
                })
            }
            item { CareerCollections(onNavigate) }
            item { FeaturedVacancy() }
        }
    }
}

@Composable
fun StatusToggleCard(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Available for hire", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.HelpOutline, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            }
            // Switch reflects the shared state
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = BrandAccent)
            )
        }
    }
}

@Composable
fun ReadyForWorkPopup(
    isReady: Boolean,
    onReadyChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    // Local states for the other sub-settings in the popup
    var partTime by remember { mutableStateOf(false) }
    var freelance by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).background(Color.White)
    ) {
        Text("Ready for Work Settings", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        HorizontalDivider()

        // MAIN SWITCH in popup
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("I'm Ready for Work", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            Switch(
                checked = isReady,
                onCheckedChange = onReadyChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Red)
            )
        }

        // Other functional switches
        PopupSwitchRow("Open to Part Time", partTime) { partTime = it }
        PopupSwitchRow("Open to Freelance", freelance) { freelance = it }
        // ... add more rows as needed

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                Text("Cancel", color = Color.Gray)
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1.5f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Settings", color = Color.White)
            }
        }
    }
}

@Composable
fun PopupSwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.DarkGray)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.Red)
        )
    }
}
@Composable
fun HeaderSection(query: String, onQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandPrimary)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = { Text("Search jobs or skills...", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(Modifier.width(12.dp))
            Icon(Icons.Default.Notifications, "Alerts", tint = Color.White)
        }
    }
}

@Composable
fun StatusToggleCard() {
    var isChecked by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text("Available for hire", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Switch(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = SwitchDefaults.colors(checkedThumbColor = BrandAccent)
            )
        }
    }
}

@Composable
fun QuickActions(onActionClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val items = listOf(
            "Jobs In Malaysia" to Icons.Default.LocationOn,
            "Upload Resume" to Icons.Default.CloudUpload,
            "ATS Checker" to Icons.Default.QrCodeScanner,
            "See More" to Icons.Default.History
        )
        items.forEach { (label, icon) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onActionClick(label) }
            ) {
                Box(
                    modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)).background(CardBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, label, tint = BrandPrimary)
                }
                Text(label, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun CareerCollections(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Collections", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(
                "See More",
                color = BrandPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* Handle See More */ }
            )
        }

        Spacer(Modifier.height(12.dp))

        val categories = listOf("High Paying", "WFH Jobs", "Part-Time", "International")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CategoryButton(categories[0], Color(0xFFE8EAF6), Modifier.weight(1f)) {
                    context.startActivity(Intent(context, HighPayActivity::class.java))
                }
                CategoryButton(categories[1], Color(0xFFE0F2F1), Modifier.weight(1f)) {
                    context.startActivity(Intent(context, WFHActivity::class.java))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CategoryButton(categories[2], Color(0xD3FFEA71), Modifier.weight(1f)) {
                    context.startActivity(Intent(context, PartTimeActivity::class.java))
                }
                CategoryButton(categories[3], Color(0xFFFCE4EC), Modifier.weight(1f), onNavigate)
            }
        }
    }
}

@Composable
fun CategoryButton(label: String, bgColor: Color, modifier: Modifier, onClick: (String) -> Unit) {
    Button(
        onClick = { onClick(label) },
        modifier = modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = label,
            color = Color.DarkGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun FeaturedVacancy() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrandPrimary.copy(alpha = 0.05f))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(24.dp)).background(BrandAccent),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.TrendingUp, null, tint = Color.White)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Suggested for You", fontWeight = FontWeight.Bold)
                Text("Based on your search history", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun BottomNavBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(containerColor = CardBg) {
        val navItems = listOf(
            "Home" to Icons.Default.Home,
            "My Jobs" to Icons.Default.Work,
            "Chat" to Icons.Default.Chat,
            "Company" to Icons.Default.Business,
            "Profile" to Icons.Default.Person
        )
        navItems.forEach { (label, icon) ->
            NavigationBarItem(
                selected = selectedTab == label,
                onClick = { onTabSelected(label) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (selectedTab == label) Color.Red else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = label,
                        color = if (selectedTab == label) Color.Red else Color.Gray,
                        fontSize = 11.sp
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

@Composable
fun DetailPage(pageName: String, modifier: Modifier = Modifier, onBack: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to $pageName", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)) {
            Text("Back to Home")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
    A216487_CikguIzwan_Lab01Theme {
        AppNavigator()
    }
}