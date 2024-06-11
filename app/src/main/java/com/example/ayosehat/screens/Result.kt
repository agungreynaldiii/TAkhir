package com.example.ayosehat.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ayosehat.R
import com.example.ayosehat.model.FoodModel
import com.example.ayosehat.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Result(navController: NavHostController, selectedFoodsString: String) {
    val selectedFoods = selectedFoodsString.split(",")
        .filter { it.isNotEmpty() }
        .chunked(5)
        .map { FoodModel(it[0], it[1].toFloat(), it[2].toFloat(), it[3].toFloat(), it[4].toFloat(), false) }

    var totalCalories = 0f
    var totalFat = 0f
    var totalCarbs = 0f
    var totalProtein = 0f

    for (food in selectedFoods) {
        totalCalories += food.kalori
        totalFat += food.lemak
        totalCarbs += food.karbo
        totalProtein += food.protein
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Result", style = TextStyle(color = Color.White)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.BottomNav.routes) }) {
                        Icon(Icons.Filled.Done, contentDescription = "Checklist", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.main)
                )
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(selectedFoods) { food ->
                ResultList(food) {}
            }
            item {
                TotalCard(totalCalories, totalFat, totalCarbs, totalProtein)
            }
        }
    }
}

@Composable
fun TotalCard(totalCalories: Float, totalFat: Float, totalCarbs: Float, totalProtein: Float) {
    val isExpanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { isExpanded.value = !isExpanded.value },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold)
                Text(String.format("%.2f kkal", totalCalories), fontWeight = FontWeight.Bold)
            }
            if (isExpanded.value) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Lemak")
                        Text("Karbohidrat")
                        Text("Protein")
                    }
                    Column {
                        Text(String.format("%.2f g", totalFat))
                        Text(String.format("%.2f g", totalCarbs))
                        Text(String.format("%.2f g", totalProtein))
                    }
                }
            }
        }
    }
}

@Composable
fun ResultList(food: FoodModel, onFoodItemClick: (FoodModel) -> Unit) {
    val isExpanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { isExpanded.value = !isExpanded.value },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(food.nama, fontWeight = FontWeight.Bold)
                Text(String.format("%.2f kkal", food.kalori), fontWeight = FontWeight.Bold)
            }
            if (isExpanded.value) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Lemak")
                        Text("Karbohidrat")
                        Text("Protein")
                    }
                    Column{
                        Text(String.format("%.2f g", food.lemak))
                        Text(String.format("%.2f g", food.karbo))
                        Text(String.format("%.2f g", food.protein))
                    }

                }
            }
        }
    }
}