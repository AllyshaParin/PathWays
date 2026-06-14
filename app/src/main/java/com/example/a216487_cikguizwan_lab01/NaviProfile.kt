package com.example.a216487_cikguizwan_lab01

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme
import java.io.File

class NaviProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = ProfileDatabase.getDatabase(applicationContext)
        val userDao = database.userProfileDao()
        val jobDao = database.jobDao()

        val profileViewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(userDao, jobDao) as T
            }
        }

        setContent {
            A216487_CikguIzwan_Lab01Theme {
                val navController = androidx.navigation.compose.rememberNavController()
                val profileViewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = profileViewModelFactory)

                ProfileScreenWithNav(navController = navController, viewModel = profileViewModel)
            }
        }
    }
}

@Composable
fun ProfileScreenWithNav(navController: NavController, viewModel: ProfileViewModel) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val profileData by viewModel.userProfileState.collectAsStateWithLifecycle()

    // Keep track of the temporary image file path across recompositions
    var tempImagePath by remember { mutableStateOf<String?>(null) }

    // Register hardware camera contract safely
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && !tempImagePath.isNullOrEmpty()) {
            val updatedProfile = profileData.copy(profilePicturePath = tempImagePath)
            viewModel.updateProfile(updatedProfile)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Header Section ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // =====================================================================
                    // UPDATED RESPONSIVE AVATAR CONTAINER WITH FIRM TOUCH WRAPPERS
                    // =====================================================================
                    Box(modifier = Modifier.size(85.dp)) {
                        Surface(
                            shape = CircleShape,
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.BottomStart)
                                .border(2.dp, Color.White, CircleShape),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            if (!profileData.profilePicturePath.isNullOrEmpty()) {
                                AsyncImage(
                                    model = File(profileData.profilePicturePath!!),
                                    contentDescription = "User Avatar",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Default Avatar",
                                    modifier = Modifier.padding(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // FIXED: Changed Surface to Box and structured modifiers for solid hit responses
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, Color.LightGray, CircleShape)
                                .clickable {
                                    try {
                                        // 1. Generate local directory files and paths
                                        val (file, uri) = StorageUtils.createImageFileUri(context)

                                        // 2. Assign path string to application memory
                                        tempImagePath = file.absolutePath

                                        // 3. Fire the Android Camera Application view screen
                                        cameraLauncher.launch(uri)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Trigger Camera",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    // =====================================================================

                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profileData.name.ifEmpty { "No Name Set" },
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(profileData.phone.ifEmpty { "No Phone" }, color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(profileData.email.ifEmpty { "No Email" }, color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                ProfileHeaderButton("Get Manager's Endorsement", Icons.Default.Shield)
                Spacer(Modifier.height(8.dp))
                ProfileHeaderButton("My Public Resume", Icons.Default.Description)
            }
        }

        // --- Content Body ---
        Column(modifier = Modifier.padding(16.dp)) {
            // Profile Progress Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Your Profile Progress", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("Unlock Direct Chat with Employers", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("99%", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Goal to unlock Chat!", style = MaterialTheme.typography.bodySmall)
                    }
                    LinearProgressIndicator(
                        progress = { 0.99f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = Color(0xFF4CAF50),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                    TextButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text("Show All Missing Information ⌄", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            ProfileListItem(Icons.Default.Person, "My Profile") { navController.navigate("profile_detail") }
            ProfileListItem(Icons.Outlined.Email, "Inbox") { /* Action */ }
            ProfileListItem(Icons.Outlined.WorkOutline, "Career Tools") { navController.navigate("career_tools")}
            ProfileListItem(Icons.Outlined.Settings, "Settings") { /* Action */ }
            ProfileListItem(Icons.Default.Refresh, "Check For Update") { /* Action */ }

            Spacer(Modifier.height(16.dp))

            // Wallet Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Maukerja Wallet", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.HelpOutline, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp).padding(start = 4.dp))
                    }
                    Text("(( • )) PING 3/10", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Check Wallet")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- LOGOUT SECTION ---
            TextButton(
                onClick = { /* Handle Logout */ },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.Red)
                    Spacer(Modifier.width(12.dp))
                    Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileListItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Text(text = label, modifier = Modifier.weight(1f), fontSize = 16.sp, color = Color.DarkGray)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
    }
}

@Composable
fun ProfileHeaderButton(text: String, icon: ImageVector?) {
    OutlinedButton(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        border = BorderStroke(1.dp, Color.White)
    ) {
        if (icon != null) {
            Icon(icon, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    val emptyFactory = object : ViewModelProvider.Factory {}
    A216487_CikguIzwan_Lab01Theme {
        val mockNavController = androidx.navigation.compose.rememberNavController()
        val mockViewModel : ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = emptyFactory)
        ProfileScreenWithNav(
            navController = mockNavController,
            viewModel = mockViewModel
        )
    }
}