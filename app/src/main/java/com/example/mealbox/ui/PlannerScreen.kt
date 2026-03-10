package com.example.mealbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealbox.data.DayOfWeek
import com.example.mealbox.data.Meal
import com.example.mealbox.data.MealSlot
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(viewModel: MainViewModel) {
    var showMealPicker by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf<DayOfWeek?>(null) }
    var selectedSlot by remember { mutableStateOf<MealSlot?>(null) }

    val currentDayIndex = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 // Mon=0, Sun=6

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weekly Planner") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(DayOfWeek.values()) { day ->
                val isToday = day.ordinal == currentDayIndex
                DayCard(
                    day = day,
                    isToday = isToday,
                    slots = viewModel.weeklyPlan[day] ?: emptyMap(),
                    onSlotClick = { slot ->
                        selectedDay = day
                        selectedSlot = slot
                        showMealPicker = true
                    },
                    onRemoveMeal = { slot ->
                        viewModel.removeMealFromPlan(day, slot)
                    }
                )
            }
        }

        if (showMealPicker) {
            MealPickerDialog(
                meals = viewModel.meals.value,
                onDismiss = { showMealPicker = false },
                onMealSelected = { meal ->
                    if (selectedDay != null && selectedSlot != null) {
                        viewModel.assignMeal(selectedDay!!, selectedSlot!!, meal)
                    }
                    showMealPicker = false
                }
            )
        }
    }
}

@Composable
fun DayCard(
    day: DayOfWeek,
    isToday: Boolean,
    slots: Map<MealSlot, Meal?>,
    onSlotClick: (MealSlot) -> Unit,
    onRemoveMeal: (MealSlot) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (isToday) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)) else null
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = day.name,
                fontWeight = FontWeight.Bold,
                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MealSlot.values().forEach { slot ->
                    MealSlotItem(
                        modifier = Modifier.weight(1f),
                        slotName = slot.name,
                        meal = slots[slot],
                        onClick = { onSlotClick(slot) },
                        onRemove = { onRemoveMeal(slot) }
                    )
                }
            }
        }
    }
}

@Composable
fun MealSlotItem(
    modifier: Modifier,
    slotName: String,
    meal: Meal?,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = slotName, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        if (meal != null) {
            Text(
                text = meal.name,
                fontSize = 14.sp,
                maxLines = 2,
                minLines = 2
            )
            IconButton(onClick = onRemove, modifier = Modifier.size(20.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Remove", modifier = Modifier.size(16.dp))
            }
        } else {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun MealPickerDialog(
    meals: List<Meal>,
    onDismiss: () -> Unit,
    onMealSelected: (Meal) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select a Meal") },
        text = {
            if (meals.isEmpty()) {
                Text("No meals in library. Add some first!")
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(meals) { meal ->
                        ListItem(
                            headlineContent = { Text(meal.name) },
                            modifier = Modifier.clickable { onMealSelected(meal) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
