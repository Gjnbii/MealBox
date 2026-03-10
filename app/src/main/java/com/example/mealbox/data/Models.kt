package com.example.mealbox.data

import android.net.Uri
import androidx.compose.runtime.Immutable

enum class MealCategory {
    Breakfast, Lunch, Dinner, Snack
}

@Immutable
data class Ingredient(
    val name: String,
    val quantity: String,
    val category: String = "Other",
    val isBought: Boolean = false
)

data class Meal(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val ingredients: List<Ingredient> = emptyList(),
    val category: MealCategory,
    val notes: String = "",
    val imageUri: Uri? = null
)

data class WeeklyPlan(
    val plan: Map<DayOfWeek, Map<MealSlot, Meal?>> = emptyMap()
)

enum class DayOfWeek {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
}

enum class MealSlot {
    Breakfast, Lunch, Dinner
}

data class UserSettings(
    val mealsPerDay: Int = 3,
    val dietaryPreferences: Set<DietaryPreference> = emptySet(),
    val isDarkMode: Boolean = false
)

enum class DietaryPreference {
    Vegetarian, Vegan, GlutenFree, DairyFree
}
