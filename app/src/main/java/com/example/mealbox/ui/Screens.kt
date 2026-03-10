package com.example.mealbox.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Planner : Screen("planner", "Planner", Icons.Default.DateRange)
    object Library : Screen("library", "Library", Icons.Default.MenuBook)
    object AddMeal : Screen("add_meal", "Add Meal", Icons.Default.Add)
    object ShoppingList : Screen("shopping_list", "Shopping", Icons.AutoMirrored.Filled.List)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Planner,
    Screen.Library,
    Screen.AddMeal,
    Screen.ShoppingList,
    Screen.Settings
)
