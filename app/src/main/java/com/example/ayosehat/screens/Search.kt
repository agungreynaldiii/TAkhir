package com.example.ayosehat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val selectedItems by searchViewModel.selectedFoods.observeAsState(emptyList())

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Tambah Makanan", color = Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.main)),
                actions = {
                    IconButton(
                        onClick = {
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
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
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )

            LazyColumn(modifier = Modifier.weight(1f).fillMaxSize()) {
                val filteredList = if (searchText.isNotEmpty()) {
                    foodList.filter { it.nama.lowercase().contains(searchText.lowercase()) }
                } else {
                    emptyList()
                }

                items(filteredList) { currentFood ->
                    FoodListItem(
                        food = currentFood,
                        isSelected = selectedItems.contains(currentFood)
                    ) { selectedFood ->
                        if (selectedItems.contains(selectedFood)) {
                            searchViewModel.removeSelectedFood(selectedFood)
                        } else {
                            searchViewModel.addSelectedFood(selectedFood)
                        }
                    }
                }
            }

            Button(
                onClick = { navController.navigate(Routes.ImageUpload.routes) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.main))
            ) {
                Text("Upload Image", color = Color.White)
            }
        }
    }
}

@Composable
fun FoodListItem(food: FoodModel, isSelected: Boolean, onCheckboxClick: (FoodModel) -> Unit) {
    var isChecked by remember { mutableStateOf(isSelected) }

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

            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement = Arrangement.SpaceAround
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
                    food.isSelected = isChecked
                    onCheckboxClick(food)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.White,
                    uncheckedColor = Color.White,
                    checkmarkColor = Color.Black
                )
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