package com.example.a216487_cikguizwan_lab01

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme

class NaviProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                ProfileScreenWithNav()
            }
        }
    }
}

@Composable
fun ProfileScreenWithNav() {
    val context = LocalContext.current
    val selectedTab = "Profile"

    Scaffold(
        bottomBar = {
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
                        "Company" -> { /* Navigate to Company if activity exists */ }
                        "Profile" -> { /* Already here */ }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Profile Image Placeholder
                        Box(modifier = Modifier.size(80.dp)) {
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier.size(80.dp).border(2.dp, Color.White, CircleShape),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Icon(Icons.Default.Person, null, modifier = Modifier.padding(16.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                            // Edit Icon
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(24.dp).align(Alignment.TopEnd)
                            ) {
                                Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(4.dp))
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "NURALLYSHA AYUNI BINTI SHAPARIN",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("+60195239583", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
                                Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("nurallyshaayuni@gmail.com", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
                                Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Action Buttons
                    ProfileHeaderButton("Get Manager's Endorsement", Icons.Default.Shield)
                    Spacer(Modifier.height(8.dp))
                    ProfileHeaderButton("My Public Resume", Icons.Default.Description)
                }
            }

            // --- Content Body ---
            Column(modifier = Modifier.padding(16.dp)) {
                // Task 2: Profile Progress Card
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

                // Task 2: Wallet Card
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
            }
        }
    }
}

@Composable
fun ProfileHeaderButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector?) {
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
    A216487_CikguIzwan_Lab01Theme {
        ProfileScreenWithNav()
    }
}