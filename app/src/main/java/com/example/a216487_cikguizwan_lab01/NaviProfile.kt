package com.example.a216487_cikguizwan_lab01

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a216487_cikguizwan_lab01.ui.theme.A216487_CikguIzwan_Lab01Theme

private val BrandRed = Color(0xFF4FEA54)

class NaviProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A216487_CikguIzwan_Lab01Theme {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandRed)
                .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile Image Placeholder
                    Box(modifier = Modifier.size(80.dp)) {
                        Surface(
                            shape = CircleShape,
                            modifier = Modifier.size(80.dp).border(2.dp, Color.White, CircleShape),
                            color = Color.LightGray
                        ) {
                            Icon(Icons.Default.Person, null, modifier = Modifier.padding(16.dp))
                        }
                        // Edit Icon
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(24.dp).align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Edit, null, tint = BrandRed, modifier = Modifier.padding(4.dp))
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("NURALLYSHA AYUNI BINTI SHAPARIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("+60195239583", color = Color.White, fontSize = 14.sp)
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("nurallyshaayuni@gmail.com", color = Color.White, fontSize = 14.sp)
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Action Buttons
                ProfileHeaderButton("Get Manager's Endorsement", Icons.Default.Shield)
                Spacer(Modifier.height(8.dp))
                ProfileHeaderButton("My Public Resume", Icons.Default.Description)
                Spacer(Modifier.height(8.dp))
                ProfileHeaderButton("Employer Preview", null)
            }
        }

        // --- White Content Body ---
        Column(modifier = Modifier.padding(16.dp)) {
            // Profile Progress Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Your Profile Progress", fontWeight = FontWeight.Bold)
                    Text("Unlock Direct Chat with Employers", fontSize = 12.sp, color = Color.Gray)

                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("99%", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Goal to unlock Chat!", fontSize = 12.sp, color = Color.Gray)
                    }
                    LinearProgressIndicator(
                        progress = 0.99f,
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF4CAF50),
                        trackColor = Color(0xFFE0E0E0)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Complete 1% more to start chatting directly with employers!", fontSize = 12.sp)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    Text(
                        "Show All Missing Information ⌄",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Wallet Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Maukerja Wallet", fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.HelpOutline, null, tint = Color.Gray, modifier = Modifier.size(16.dp).padding(start = 4.dp))
                    }
                    Text("(( • )) PING 3/10", fontSize = 12.sp, color = Color.Gray)

                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.StarOutline, null, tint = Color.Gray)
                            Text(" Voucher History", fontSize = 14.sp)
                        }
                        Text("Check Here", color = Color.Blue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandRed),
                        shape = RoundedCornerShape(8.dp)
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

@Composable
fun ProfileHeaderButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector?) {
    OutlinedButton(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
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
fun ProfilePreview() {
    A216487_CikguIzwan_Lab01Theme {
        ProfileScreen()
    }
}