package com.example.ayosehat.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ayosehat.itemview.FoodItem
import com.example.ayosehat.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navHostController: NavHostController) {

    val searchViewModel: SearchViewModel = viewModel()
    val foodList by searchViewModel.foodsList.observeAsState(null)

    var search by remember {
        mutableStateOf("")
    }

    var showFoodList by remember { mutableStateOf(false) } // Flag to control visibility

    Column {
        Text(
            text = "Search",
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
            ),
            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )

        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
                showFoodList = it.isNotBlank() // Set flag to true when search is not blank
            },
            label = {
                Text(text = "Search")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        )

        Box(modifier = Modifier.height(20.dp))

        if (showFoodList) { // Only display if showFoodList is true
            LazyColumn {
                if (foodList != null && foodList!!.isNotEmpty()){
                    val filterItems = foodList!!.filter { it.nama!!.contains(search, ignoreCase = true) }

                    items(filterItems ?: emptyList()) { pairs ->
                        FoodItem(
                            pairs,
                            navHostController,
                        )
                    }
                }
            }
        }
    }
}
