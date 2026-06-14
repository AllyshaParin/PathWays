package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityFeedScreen(navController: NavController, viewModel: ProfileViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.listenToCommunityPosts()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFFE91E63),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 8.dp, end = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Post", modifier = Modifier.size(26.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
        ) {

            // GREEN BANNER HEADER AREA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2E7D32))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text = "PATHWAYS COMMUNITY",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Career Hub & Tips",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // POSTS FEED
            Box(modifier = Modifier.fillMaxSize()) {
                if (viewModel.communityPosts.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No community tips shared yet. Be the first!", color = Color.Gray, fontSize = 15.sp)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(viewModel.communityPosts) { post ->
                            CommunityPostCard(
                                post = post,
                                onDeleteClick = {
                                    // Verifies the ID is valid before requesting a deletion target
                                    if (post.id.isNotBlank()) {
                                        FirebaseFirestore.getInstance()
                                            .collection("community_posts")
                                            .document(post.id)
                                            .delete()
                                            .addOnSuccessListener {
                                                // Trigger structural reload sequence
                                                viewModel.listenToCommunityPosts()
                                            }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // INPUT DIALOG WINDOW
        if (showDialog) {
            var titleInput by remember { mutableStateOf("") }
            var companyInput by remember { mutableStateOf("") }
            var reviewInput by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showDialog = false },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White,
                title = { Text(text = "Share Interview / Job Tip", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
                        OutlinedTextField(value = titleInput, onValueChange = { titleInput = it }, label = { Text("Job Title") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                        OutlinedTextField(value = companyInput, onValueChange = { companyInput = it }, label = { Text("Company Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                        OutlinedTextField(value = reviewInput, onValueChange = { reviewInput = it }, label = { Text("Your Experience / Helpful Tip") }, modifier = Modifier.fillMaxWidth(), minLines = 3, shape = RoundedCornerShape(10.dp))
                    }
                },
                confirmButton = {
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        onClick = {
                            if (titleInput.isNotBlank() && companyInput.isNotBlank() && reviewInput.isNotBlank()) {
                                viewModel.uploadCommunityPost(titleInput, companyInput, reviewInput) {
                                    showDialog = false
                                }
                            }
                        }
                    ) {
                        Text("Post to Cloud", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel", color = Color.Gray) }
                }
            )
        }
    }
}

@Composable
fun CommunityPostCard(post: CommunityPost, onDeleteClick: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(modifier = Modifier.size(42.dp), shape = CircleShape, color = Color(0xFFE8F5E9)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(22.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = post.jobTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF212121), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(text = post.companyName, color = Color(0xFF757575), fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }

                // OPTIONS DROPDOWN ANCHOR
                Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Post Options", tint = Color.Gray)
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete Post", color = Color.Red) },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                            onClick = {
                                menuExpanded = false
                                onDeleteClick()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color(0xFFBDBDBD), modifier = Modifier.size(18.dp).padding(top = 1.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = post.reviewText, fontSize = 14.sp, color = Color(0xFF424242), lineHeight = 20.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PersonOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Posted by: ${post.authorName.ifBlank { "Anonymous" }}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // FIXED: Wrapped in an explicit width calculation framework to block text wrapping bugs
                Surface(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "PathWays Verified",
                        fontSize = 10.sp,
                        color = Color(0xFF616161),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}