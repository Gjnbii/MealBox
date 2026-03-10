package com.example.mealbox.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mealbox.data.*
import java.util.Locale

class MainViewModel : ViewModel() {
    var meals = mutableStateOf(listOf<Meal>())
        private set

    var weeklyPlan = mutableStateMapOf<DayOfWeek, MutableMap<MealSlot, Meal?>>()
        private set

    var settings by mutableStateOf(UserSettings())
        private set

    init {
        DayOfWeek.values().forEach { day ->
            weeklyPlan[day] = mutableStateMapOf(
                MealSlot.Breakfast to null,
                MealSlot.Lunch to null,
                MealSlot.Dinner to null
            )
        }
    }

    fun addMeal(meal: Meal) {
        meals.value = meals.value + meal
    }

    fun assignMeal(day: DayOfWeek, slot: MealSlot, meal: Meal?) {
        weeklyPlan[day]?.let {
            it[slot] = meal
        }
    }

    fun removeMealFromPlan(day: DayOfWeek, slot: MealSlot) {
        weeklyPlan[day]?.let {
            it[slot] = null
        }
    }

    fun clearWeek() {
        weeklyPlan.keys.forEach { day ->
            weeklyPlan[day]?.keys?.forEach { slot ->
                weeklyPlan[day]!![slot] = null
            }
        }
    }

    fun clearAllMeals() {
        meals.value = emptyList()
        clearWeek()
    }

    fun updateSettings(newSettings: UserSettings) {
        settings = newSettings
    }
    
    fun getShoppingList(): List<Ingredient> {
        val allIngredients = mutableListOf<Ingredient>()
        weeklyPlan.values.forEach { dayPlan ->
            dayPlan.values.filterNotNull().forEach { meal ->
                allIngredients.addAll(meal.ingredients)
            }
        }
        
        return allIngredients.groupBy { it.name.lowercase() }.map { (name, list) ->
            Ingredient(
                name = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                quantity = list.joinToString(", ") { it.quantity },
                category = list.first().category
            )
        }
    }
}
