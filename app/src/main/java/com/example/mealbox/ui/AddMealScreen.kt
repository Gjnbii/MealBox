package com.example.mealbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealbox.data.Ingredient
import com.example.mealbox.data.Meal
import com.example.mealbox.data.MealCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(viewModel: MainViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(MealCategory.Breakfast) }
    var notes by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf<Ingredient>() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add New Meal") })
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
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Meal Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("Category", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MealCategory.values().forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.name) }
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Ingredients", style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = { ingredients.add(Ingredient("", "")) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Ingredient")
                    }
                }
            }

            itemsIndexed(ingredients) { index, ingredient ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = ingredient.name,
                        onValueChange = { ingredients[index] = ingredient.copy(name = it) },
                        placeholder = { Text("Item") },
                        modifier = Modifier.weight(0.6f)
                    )
                    OutlinedTextField(
                        value = ingredient.quantity,
                        onValueChange = { ingredients[index] = ingredient.copy(quantity = it) },
                        placeholder = { Text("Qty") },
                        modifier = Modifier.weight(0.3f)
                    )
                    IconButton(onClick = { ingredients.removeAt(index) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }

            item {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            viewModel.addMeal(
                                Meal(
                                    name = name,
                                    category = category,
                                    ingredients = ingredients.toList(),
                                    notes = notes
                                )
                            )
                            navController.navigate(Screen.Library.route) {
                                popUpTo(Screen.Planner.route)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotBlank()
                ) {
                    Text("Save Meal")
                }
            }
        }
    }
}
