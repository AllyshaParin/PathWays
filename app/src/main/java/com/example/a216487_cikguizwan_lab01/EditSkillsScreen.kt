package com.example.a216487_cikguizwan_lab01

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditSkillsScreen(navController: NavController, viewModel: ProfileViewModel) {
    val suggestedSkills = listOf("Human Resources", "Recruitment", "Payroll", "Admin", "Clerical")
    var newLanguageName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Language & Skill", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFE91E63))
            )
        },
        bottomBar = {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())
        ) {
            Text("Suggested Skills*", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            FlowRow(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                suggestedSkills.forEach { skill ->
                    SuggestionChip(onClick = { }, label = { Text(skill) })
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Language Proficiency*", fontWeight = FontWeight.Bold, color = Color.DarkGray)

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newLanguageName,
                    onValueChange = { newLanguageName = it },
                    label = { Text("Add Language (e.g. Mandarin)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newLanguageName.isNotBlank()) {
                            // Ensure standard format: "Name - Level"
                            viewModel.addLanguage("$newLanguageName - Good")
                            newLanguageName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF))
                ) {
                    Text("Add")
                }
            }

            // --- FIXED LOOP ---
            viewModel.languages.forEachIndexed { index, languageEntry ->
                // Safe parsing
                val name = if (languageEntry.contains(" - ")) languageEntry.substringBefore(" -") else languageEntry
                val currentLevel = if (languageEntry.contains(" - ")) languageEntry.substringAfter("- ") else "Good"

                LanguageLevelRow(
                    language = name,
                    selectedLevel = currentLevel,
                    onLevelSelected = { newLevel ->
                        // Using index is safer than indexOf for mutable lists
                        viewModel.languages[index] = "$name - $newLevel"
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun LanguageLevelRow(
    language: String,
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(language, fontWeight = FontWeight.SemiBold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Poor", "Average", "Good").forEach { level ->
                val isSelected = selectedLevel.trim() == level
                OutlinedButton(
                    onClick = { onLevelSelected(level) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) Color(0xFFFFEBEE) else Color.Transparent,
                        contentColor = if (isSelected) Color.Red else Color.Gray
                    ),
                    border = BorderStroke(1.dp, if (isSelected) Color.Red else Color.LightGray)
                ) {
                    Text(level, fontSize = 11.sp)
                }
            }
        }
    }
}