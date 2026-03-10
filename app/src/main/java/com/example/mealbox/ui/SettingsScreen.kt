package com.example.mealbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mealbox.data.DietaryPreference
import com.example.mealbox.data.UserSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    var showClearWeekDialog by remember { mutableStateOf(false) }
    var showClearAllDialog by remember { mutableStateOf(false) }

    val settings = viewModel.settings

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("App Preferences", style = MaterialTheme.typography.titleMedium)
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dark Mode")
                            Switch(
                                checked = settings.isDarkMode,
                                onCheckedChange = { viewModel.updateSettings(settings.copy(isDarkMode = it)) }
                            )
                        }
                        
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        Text("Meals per day: ${settings.mealsPerDay}")
                        Slider(
                            value = settings.mealsPerDay.toFloat(),
                            onValueChange = { viewModel.updateSettings(settings.copy(mealsPerDay = it.toInt())) },
                            valueRange = 1f..5f,
                            steps = 3
                        )
                    }
                }
            }

            item {
                Text("Dietary Preferences", style = MaterialTheme.typography.titleMedium)
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        DietaryPreference.values().forEach { pref ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = settings.dietaryPreferences.contains(pref),
                                    onCheckedChange = { checked ->
                                        val newPrefs = if (checked) {
                                            settings.dietaryPreferences + pref
                                        } else {
                                            settings.dietaryPreferences - pref
                                        }
                                        viewModel.updateSettings(settings.copy(dietaryPreferences = newPrefs))
                                    }
                                )
                                Text(pref.name)
                            }
                        }
                    }
                }
            }

            item {
                Text("Data Management", style = MaterialTheme.typography.titleMedium)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { showClearWeekDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Clear Weekly Plan")
                    }

                    OutlinedButton(
                        onClick = { showClearAllDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Reset Meal Library")
                    }
                }
            }

            item {
                Text("About", style = MaterialTheme.typography.titleMedium)
                ListItem(
                    headlineContent = { Text("MealBox v1.0.0") },
                    supportingContent = { Text("Your ultimate weekly meal planner.") },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
                )
            }
        }
    }

    if (showClearWeekDialog) {
        AlertDialog(
            onDismissRequest = { showClearWeekDialog = false },
            title = { Text("Clear Week?") },
            text = { Text("This will remove all meals from your current weekly planner.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearWeek()
                    showClearWeekDialog = false
                }) { Text("Clear") }
            },
            dismissButton = {
                TextButton(onClick = { showClearWeekDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = { Text("Reset Library?") },
            text = { Text("This will delete all your saved meals and clear the planner. This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAllMeals()
                    showClearAllDialog = false
                }) { Text("Reset Everything") }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) { Text("Cancel") }
            }
        )
    }
}
