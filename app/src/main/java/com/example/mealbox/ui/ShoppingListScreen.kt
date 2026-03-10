package com.example.mealbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.mealbox.data.Ingredient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: MainViewModel) {
    val shoppingList = viewModel.getShoppingList()
    var checkedItems by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping List") },
                actions = {
                    IconButton(onClick = { checkedItems = emptySet() }) {
                        Icon(Icons.Default.ClearAll, contentDescription = "Clear Checked")
                    }
                }
            )
        }
    ) { padding ->
        if (shoppingList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No ingredients needed yet. Plan some meals!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(shoppingList) { ingredient ->
                    val isChecked = checkedItems.contains(ingredient.name)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                checkedItems = if (it) {
                                    checkedItems + ingredient.name
                                } else {
                                    checkedItems - ingredient.name
                                }
                            }
                        )
                        Text(
                            text = "${ingredient.name} (${ingredient.quantity})",
                            style = MaterialTheme.typography.bodyLarge,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                            color = if (isChecked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
