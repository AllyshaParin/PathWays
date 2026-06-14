package com.example.a216487_cikguizwan_lab01

import androidx.navigation.compose.currentBackStackEntryAsState
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme
import kotlinx.coroutines.launch
import com.google.firebase.FirebaseApp

// --- Data Models ---
data class CompanyData(val name: String, val logoRes: Int, val brandColor: Color)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = ProfileDatabase.getDatabase(applicationContext)

        val profileViewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(
                    userProfileDao = database.userProfileDao(),
                    jobDao = database.jobDao()
                ) as T
            }
        }

        setContent {
            A216487_CikguIzwan_Lab01Theme {
                AppNavigator(factory = profileViewModelFactory)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator(factory: ViewModelProvider.Factory) {
    val context = LocalContext.current
    val navController = rememberNavController()

    val profileViewModel: ProfileViewModel = viewModel(factory = factory)

    LaunchedEffect(Unit) {
        profileViewModel.loadKnnRecommendations()
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var currentTab by remember { mutableStateOf("Home") }
    var searchQuery by remember { mutableStateOf("") }

    var isReadyForWork by remember { mutableStateOf(false) }
    var openToPartTime by remember { mutableStateOf(false) }
    var openToFreelance by remember { mutableStateOf(false) }
    var openToSingapore by remember { mutableStateOf(false) }
    var openToOnsite by remember { mutableStateOf(false) }
    var openToVolunteer by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        if (currentRoute == "home_content") {
            profileViewModel.loadKnnRecommendations()
        }
    }

    LaunchedEffect(currentTab) {
        if (currentTab == "Chat") {
            context.startActivity(Intent(context, NaviChatActivity::class.java))
            currentTab = "Home"
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeaderSection(
                query = searchQuery,
                onValueChange = { searchQuery = it },
                currentRoute = currentRoute,
                navController = navController
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = when (currentRoute) {
                    "home_content" -> "Home"
                    "navimyjob" -> "My Jobs"
                    "profile_view" -> "Profile"
                    else -> currentTab
                },
                onTabSelected = { label ->
                    currentTab = label
                    when (label) {
                        "Home" -> navController.navigate("home_content") { popUpTo(0) }
                        "Profile" -> navController.navigate("profile_view") { launchSingleTop = true }
                        "My Jobs" -> navController.navigate("navimyjob") { launchSingleTop = true }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "home_content") {
                composable("home_content") {
                    MainContent(
                        isReady = isReadyForWork,
                        onToggleHire = { newState ->
                            isReadyForWork = newState
                            if (newState) showBottomSheet = true
                        },
                        navController = navController,
                        viewModel = profileViewModel
                    )
                }
                composable("profile_view") { ProfileScreenWithNav(navController, profileViewModel) }
                composable("profile_detail") { MyProfileDetailScreen(navController, profileViewModel) }
                composable("career_tools") { CareerToolsScreen(navController) }
                composable("salary_input") { SalaryInputScreen(navController, profileViewModel) }
                composable("salary_result") { SalaryResultScreen(navController, profileViewModel) }

                composable("community_feed") { CommunityFeedScreen(navController, profileViewModel) }
                composable("remote_jobs_feed") { RemoteJobsFeedScreen(navController, profileViewModel) }
                composable("ats_scanner") { AtsScannerScreen(navController, profileViewModel) }

                composable(
                    route = "review_application/{jobTitle}/{company}/{salary}/{location}",
                    arguments = listOf(
                        navArgument("jobTitle") { type = NavType.StringType },
                        navArgument("company") { type = NavType.StringType },
                        navArgument("salary") { type = NavType.StringType },
                        navArgument("location") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val jobTitle = backStackEntry.arguments?.getString("jobTitle") ?: "Unknown Job"
                    val company = backStackEntry.arguments?.getString("company") ?: "Unknown Company"
                    val salary = backStackEntry.arguments?.getString("salary") ?: "Unspecified Salary"
                    val location = backStackEntry.arguments?.getString("location") ?: "Malaysia"

                    ReviewApplicationScreen(
                        navController = navController,
                        viewModel = profileViewModel,
                        jobTitle = jobTitle,
                        company = company,
                        salary = salary,
                        location = location
                    )
                }

                composable("job_in_malaysia") { JobInMyScreen(navController, profileViewModel) }
                composable("navimyjob") { MyJobsScreenWithNav(navController, profileViewModel) }
                composable("success") { SuccessScreen(navController) }
                composable("edit_profile") { ProfileFormScreen(navController, profileViewModel) }
            }
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
                    partTime = openToPartTime,
                    onPartTimeChange = { openToPartTime = it },
                    freelance = openToFreelance,
                    onFreelanceChange = { openToFreelance = it },
                    singapore = openToSingapore,
                    onSingaporeChange = { openToSingapore = it },
                    onsite = openToOnsite,
                    onOnsiteChange = { openToOnsite = it },
                    volunteer = openToVolunteer,
                    onVolunteerChange = { openToVolunteer = it },
                    onDismiss = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            showBottomSheet = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainContent(
    isReady: Boolean,
    onToggleHire: (Boolean) -> Unit,
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val knnJobs by viewModel.recommendedJobs

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { StatusToggleCard(isChecked = isReady, onCheckedChange = onToggleHire) }
            item {
                QuickActions(onActionClick = { label ->
                    when (label) {
                        "Jobs In Malaysia" -> navController.navigate("job_in_malaysia")
                        "Upload Resume" -> context.startActivity(Intent(context, DropResumeActivity::class.java))
                        "Chat" -> context.startActivity(Intent(context, NaviChatActivity::class.java))
                        "ATS Checker" -> navController.navigate("ats_scanner")
                        "Community" -> navController.navigate("community_feed")
                        "Remote Jobs" -> navController.navigate("remote_jobs_feed")
                    }
                })
            }
            item { CareerCollections(onNavigate = { navController.navigate(it) }) }
            item { FeaturedVacancy() }
            item { FeaturedJobsRow(recommendedJobs = knnJobs, navController = navController) }
            item { VacancySummaryCard() }
            item { WalkInBanner() }
            item { TopCompaniesSection() }
            item { AvailabilityCard() }
        }
    }
}

// =====================================================================
// FIXED HEADER SECTION: DYNAMICALLY REMOVES SEARCH BAR ON OTHER PAGES
// =====================================================================
@Composable
fun HeaderSection(
    query: String,
    onValueChange: (String) -> Unit,
    currentRoute: String?,
    navController: NavController
) {
    // Determine if the current screen requires a search bar layout container
    val shouldShowSearch = currentRoute == "home_content" || currentRoute == "job_in_malaysia"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2E7D32))
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Column {
            if (currentRoute == "home_content") {
                Text(
                    text = "PathWays",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Show back navigation arrow only when safe to pop back stack layouts
                if (currentRoute != "home_content") {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(end = 4.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                }

                if (shouldShowSearch) {
                    // Render search input container only if matching valid search feeds
                    TextField(
                        value = if (currentRoute == "job_in_malaysia") "selangor" else query,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(26.dp)),
                        placeholder = { Text("Search jobs or skills...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                } else {
                    // Fallback title banner text if the search field framework is hidden
                    val screenTitle = when(currentRoute) {
                        "profile_view" -> "My Profile Dashboard"
                        "career_tools" -> "Career Hub & Tips"
                        "navimyjob" -> "My Applications"
                        else -> "PathWays Community"
                    }
                    Text(
                        text = screenTitle,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f).padding(vertical = 8.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))
                Icon(Icons.Default.Notifications, "Alerts", tint = Color.White)
            }
        }
    }
}

@Composable
fun StatusToggleCard(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("I am Ready For Work", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.HelpOutline, null, modifier = Modifier.size(18.dp), tint = Color.Gray)
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFE91E63))
            )
        }
    }
}

@Composable
fun ReadyForWorkPopup(
    isReady: Boolean, onReadyChange: (Boolean) -> Unit,
    partTime: Boolean, onPartTimeChange: (Boolean) -> Unit,
    freelance: Boolean, onFreelanceChange: (Boolean) -> Unit,
    singapore: Boolean, onSingaporeChange: (Boolean) -> Unit,
    onsite: Boolean, onOnsiteChange: (Boolean) -> Unit,
    volunteer: Boolean, onVolunteerChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Ready for Work Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        PreferenceToggle("I'm Ready for Work", isReady, onReadyChange, isMaster = true)
        HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)

        PreferenceToggle("Open to Part Time", partTime, onPartTimeChange)
        PreferenceToggle("Open to Freelance", freelance, onFreelanceChange)
        PreferenceToggle("Open to Work in Singapore", singapore, onSingaporeChange)
        PreferenceToggle("Open to Work Fully in Office/Onsite", onsite, onOnsiteChange)
        PreferenceToggle("Open to Volunteer", volunteer, onVolunteerChange, showInfo = true)

        Spacer(Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray, fontSize = 16.sp)
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.height(48.dp).weight(0.7f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Settings", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun PreferenceToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, isMaster: Boolean = false, showInfo: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontSize = 16.sp, fontWeight = if (isMaster) FontWeight.Bold else FontWeight.Normal)
            if (showInfo) {
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.Info, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFFE91E63)))
    }
}

@Composable
fun QuickActions(onActionClick: (String) -> Unit) {
    val items = listOf(
        "Jobs In Malaysia" to Icons.Default.LocationOn,
        "Upload Resume" to Icons.Default.CloudUpload,
        "ATS Checker" to Icons.Default.QrCodeScanner,
        "Community" to Icons.Default.Forum,
        "Remote Jobs" to Icons.Default.Public
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
    ) {
        items(items) { (label, icon) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(76.dp)
                    .clickable { onActionClick(label) }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, label, tint = Color(0xFFE91E63))
                }
                Text(
                    text = label,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 12.sp
                )
            }
        }
    }
}

@Composable
fun CareerCollections(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Collections", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("See More", color = Color(0xFFE91E63), modifier = Modifier.clickable { })
        }
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryButton("High Paying", Color(0xFFFFEBEE), Modifier.weight(1f)) {
                context.startActivity(Intent(context, HighPayActivity::class.java))
            }
            CategoryButton("WFH Jobs", Color(0xFFE3F2FD), Modifier.weight(1f)) {
                context.startActivity(Intent(context, WFHActivity::class.java))
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryButton("Part-Time", Color(0xFFF1F8E9), Modifier.weight(1f)) {
                context.startActivity(Intent(context, PartTimeActivity::class.java))
            }
            CategoryButton("International", Color(0xFFFFF3E0), Modifier.weight(1f)) {
                onNavigate("navimyjob")
            }
        }
    }
}

@Composable
fun CategoryButton(label: String, bgColor: Color, modifier: Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier.height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = bgColor), shape = RoundedCornerShape(8.dp)) {
        Text(text = label, color = Color.Black, fontSize = 12.sp)
    }
}

@Composable
fun TopCompaniesSection() {
    val companies = listOf(
        CompanyData("Grab", R.drawable.grab, Color.Green),
        CompanyData("Maybank", R.drawable.maybank, Color.Yellow),
        CompanyData("Petronas", R.drawable.petronas, Color.Cyan)
    )
    Column {
        Text("Top Companies", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
            items(companies) { company ->
                Card(modifier = Modifier.width(120.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = company.logoRes),
                            contentDescription = "${company.name} Logo",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(company.name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedJobsRow(recommendedJobs: List<JobEntity>, navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Suggested Jobs for You (KNN Match)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF2E7D32),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Top 5 Picks",
                color = Color(0xFFE91E63),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.wrapContentWidth()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (recommendedJobs.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth().height(100.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Matching optimal local opportunities...", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(vertical = 4.dp)) {
                items(recommendedJobs) { job ->
                    val logoResourceId = remember(job.company) {
                        val drawableFileName = when (job.company.lowercase().trim()) {
                            "grab" -> "grab"
                            "maybank" -> "maybank"
                            "petronas" -> "petronas"
                            "texas chicken" -> "texas"
                            "uniqlo" -> "uniqlo"
                            else -> "ic_default_company"
                        }
                        context.resources.getIdentifier(drawableFileName, "drawable", context.packageName)
                    }

                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .clickable {
                                val encTitle = android.net.Uri.encode(job.title)
                                val encCompany = android.net.Uri.encode(job.company)
                                val encSalary = android.net.Uri.encode(job.salary)
                                val encLoc = android.net.Uri.encode(
                                    when (job.locationCode) {
                                        1 -> "Selangor/KL"
                                        2 -> "Johor"
                                        3 -> "Penang"
                                        else -> "Malaysia"
                                    }
                                )
                                navController.navigate("review_application/$encTitle/$encCompany/$encSalary/$encLoc")
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (logoResourceId != 0) {
                                    androidx.compose.foundation.Image(
                                        painter = androidx.compose.ui.res.painterResource(id = logoResourceId),
                                        contentDescription = "${job.company} Logo",
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Business,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp))
                                            .padding(4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(job.title, fontWeight = FontWeight.Bold, maxLines = 1, fontSize = 14.sp)
                                    Text(job.company, color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(job.salary, color = Color(0xFFE91E63), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VacancySummaryCard() {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Vacancies You Like", fontWeight = FontWeight.Bold)
            Text("211 jobs available in Semenyih", color = Color(0xFF4CAF50), fontSize = 12.sp)
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))) {
                Text("View 211 vacancies >")
            }
        }
    }
}

@Composable
fun WalkInBanner() {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F3))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Restaurant, null, tint = Color.Black)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Walk-in Interview @ Black Canyon", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Every Monday to Friday | 3PM - 5PM", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun AvailabilityCard() {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Help employers know when you can start", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            OutlinedCard(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text("I can start immediately", modifier = Modifier.padding(12.dp))
            }
        }
    }
}

@Composable
fun FeaturedVacancy() {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.TrendingUp, null)
            Spacer(Modifier.width(12.dp))
            Text("Suggested for You based on history")
        }
    }
}

@Composable
fun BottomNavBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
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
                label = { Text(text = label, fontSize = 9.sp) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
    val emptyFactory = object : ViewModelProvider.Factory {}
    A216487_CikguIzwan_Lab01Theme {
        AppNavigator(factory = emptyFactory)
    }
}