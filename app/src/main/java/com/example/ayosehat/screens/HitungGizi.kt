package com.example.ayosehat.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ayosehat.R

//import com.example.hitunggizi.R

@Composable
fun HitungGizi(navController: NavController) { // Pass NavController as a parameter
    val mealOptions = listOf("Makan Siang", "Makan Pagi", "Makan Malam")
    var selectedMeal by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Display title (optional)
        Text(text = "Hitung Gizi")

        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) { // Wrap with Box
            LazyColumn {
                items(mealOptions) { mealOption ->
                    Surface( // Wrap each item in Surface for boxing
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable { selectedMeal = mealOption }
                            .border( // Add border with main color
                                width = 1.dp,
                                color = colorResource(id = R.color.main),
                                shape = RoundedCornerShape(11.dp), // Set rounded corner radius
                            ),
                    ) {
                        Row( // Use Row to arrange elements horizontally
                            modifier = Modifier.fillMaxWidth(0.8f), // Reserve 80% for text
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = mealOption,
                                modifier = Modifier.padding(8.dp),
                                color = Color.Black,
                                style = TextStyle() // Set text color (optional)
                            )
                            IconButton(onClick = {  // Handle icon click and navigate
                                navController.navigate("add") // Replace "add" with your actual route name
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.plus_1), // Use your icon resource
                                    contentDescription = "Tambah", // Set content description
                                    tint = Color.Black // Set icon tint color (optional)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
