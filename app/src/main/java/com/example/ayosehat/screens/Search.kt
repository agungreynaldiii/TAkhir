package com.example.ayosehat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ayosehat.R
import com.example.ayosehat.model.FoodModel
import com.example.ayosehat.navigation.Routes
import com.example.ayosehat.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel()
    val foodList by searchViewModel.foodsList.observeAsState(emptyList())

    var searchText by remember { mutableStateOf("") }
    val selectedItems by searchViewModel.selectedFoods.observeAsState(emptyList()) // Amati selectedFoods dari ViewModel

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Tambah Makanan", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.main)),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { // Navigate back on click
                        Icon(Icons.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Navigate with the string representation of selected items
                            val selectedFoodsString = selectedItems.filter { it.isSelected }.joinToString(",") {
                                "${it.nama},${it.kalori},${it.lemak},${it.karbo},${it.protein}"
                            }
                            navController.navigate("result/$selectedFoodsString")
                        }
                    ) {
                        Icon(Icons.Filled.Done, "Checklist", tint = Color.White)
                    }
                }

            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField( // Search bar
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textStyle = TextStyle(color = Color.Black),
                placeholder = { Text("Cari makanan") },
                leadingIcon = {
                    IconButton(onClick = { /* Handle search action */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search), // Replace with your icon
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors() // Use default colors
            )

            // Add the button to navigate to ImageUploadScreen
            Button(
                onClick = { navController.navigate(Routes.ImageUpload.routes) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Upload Image")
            }

            // Conditionally show the LazyColumn
            if (searchText.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val filteredList = foodList.filter { it.nama.lowercase().contains(searchText.lowercase()) }

                    items(filteredList) { currentFood -> // Rename to avoid shadowing
                        FoodListItem(
                            food = currentFood,
                            isSelected = selectedItems.contains(currentFood)
                        ) { selectedFood -> // Rename to avoid shadowing
                            if (selectedItems.contains(selectedFood)) {
                                searchViewModel.removeSelectedFood(selectedFood) // Panggil fungsi dari viewModel
                            } else {
                                searchViewModel.addSelectedFood(selectedFood) // Panggil fungsi dari viewModel
                            }
                        }
                    }

                }
            }
            else {
                if (foodList.isNotEmpty()) { // Jika foodList tidak kosong, tampilkan daftar makanan
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(foodList) { food ->
                            FoodListItem(food, isSelected = selectedItems.contains(food)) { food ->
                                if (selectedItems.contains(food)) {
                                    searchViewModel.removeSelectedFood(food)
                                } else {
                                    searchViewModel.addSelectedFood(food)
                                }
                            }
                        }
                    }
                } else { // Tampilkan pesan loading atau lainnya jika foodList kosong
                    Text("Loading...", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun FoodListItem(food: FoodModel, isSelected: Boolean, onCheckboxClick: (FoodModel) -> Unit) {
    var isChecked by remember { mutableStateOf(isSelected) } // Use var for mutable state

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.main))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = food.nama,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            // Create a Row for the nutritional columns
            Row(
                modifier = Modifier.weight(2f), // Give more weight to the nutrition info
                horizontalArrangement = Arrangement.SpaceAround // Distribute evenly
            ) {
                NutritionColumn("Kalori", food.kalori.toString())
                NutritionColumn("Lemak", food.lemak.toString())
                NutritionColumn("Karbo", food.karbo.toString())
                NutritionColumn("Protein", food.protein.toString())
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    food.isSelected = isChecked // Update the food item's isSelected property
                    onCheckboxClick(food)
                },// Let the row handle the click
                colors = CheckboxDefaults.colors(checkedColor = Color.White)
            )
        }
    }
}

@Composable
fun NutritionColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = TextStyle(color = Color.White, fontSize = 12.sp))
        Text(text = value, style = TextStyle(color = Color.White, fontSize = 10.sp))
    }
}