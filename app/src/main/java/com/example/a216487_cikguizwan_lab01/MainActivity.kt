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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme
import kotlinx.coroutines.launch

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
    val navController = rememberNavController()
    val profileViewModel: ProfileViewModel = viewModel() // Shared ViewModel for all screens

    var currentScreen by remember { mutableStateOf("Home") }
    var searchQuery by remember { mutableStateOf("") }
    var isReadyForWork by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeaderSection(
                query = searchQuery,
                onValueChange = { newValue -> searchQuery = newValue }
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = currentScreen,
                onTabSelected = { label -> currentScreen = label
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

                // PART B NAVIGATION INTEGRATION:
                "Profile" -> {
                    NavHost(
                        navController = navController,
                        startDestination = "profile_view", // This must match the first screen
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Correctly link the view to the View Screen
                        composable("profile_view") {
                            ProfileScreenWithNav(navController, profileViewModel)
                        }

                        // Correctly link the form to the Form Screen
                        composable("profile_form") {
                            ProfileFormScreen(navController, profileViewModel)
                        }

                        composable("success") {
                            SuccessScreen(navController)
                        }
                    }
                }

                "Chat" -> context.startActivity(Intent(context, NaviChatActivity::class.java))
                "Company" -> context.startActivity(Intent(context, NaviCompanyActivity::class.java))
                "My Jobs" -> context.startActivity(Intent(context, NaviMyJobActivity::class.java))
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface
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
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Available for hire", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.HelpOutline, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
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
    var partTime by remember { mutableStateOf(false) }
    var freelance by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).background(MaterialTheme.colorScheme.surface)
    ) {
        Text("Ready for Work Settings", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineSmall)
        HorizontalDivider()

        PopupSwitchRow("I'm Ready for Work", isReady, onReadyChange)
        PopupSwitchRow("Open to Part Time", partTime) { partTime = it }
        PopupSwitchRow("Open to Freelance", freelance) { freelance = it }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                Text("Cancel")
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1.5f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = MaterialTheme.shapes.small
            ) {
                Text("Save Settings")
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
        Text(label, color = MaterialTheme.colorScheme.onSurface)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun HeaderSection(query: String, onValueChange: (String) -> Unit) { // Changed parameter name here
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = query,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clip(MaterialTheme.shapes.small),
                placeholder = { Text("Search jobs or skills...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(Modifier.width(12.dp))
            Icon(Icons.Default.Notifications, "Alerts", tint = MaterialTheme.colorScheme.onPrimary)
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
                    modifier = Modifier
                        .size(56.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, label, tint = MaterialTheme.colorScheme.primary)
                }
                Text(label, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
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
            Text("Collections", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "See More",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(Modifier.height(12.dp))

        val categories = listOf("High Paying", "WFH Jobs", "Part-Time", "International")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CategoryButton(categories[0], MaterialTheme.colorScheme.secondaryContainer, Modifier.weight(1f)) {
                    context.startActivity(Intent(context, HighPayActivity::class.java))
                }
                CategoryButton(categories[1], MaterialTheme.colorScheme.tertiaryContainer, Modifier.weight(1f)) {
                    context.startActivity(Intent(context, WFHActivity::class.java))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CategoryButton(categories[2], MaterialTheme.colorScheme.primaryContainer, Modifier.weight(1f)) {
                    context.startActivity(Intent(context, PartTimeActivity::class.java))
                }
                CategoryButton(categories[3], MaterialTheme.colorScheme.surfaceVariant, Modifier.weight(1f), onNavigate)
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
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSecondaryContainer, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun FeaturedVacancy() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.TrendingUp, null, tint = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Suggested for You", style = MaterialTheme.typography.titleMedium)
                Text("Based on your search history", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun BottomNavBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
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
                icon = { Icon(icon, label) },
                label = { Text(text = label) },
                alwaysShowLabel = true
            )
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