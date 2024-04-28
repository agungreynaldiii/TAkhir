package com.example.ayosehat.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ayosehat.item_view.PostItem
//import com.example.ayosehat.item_view.PostItem
import com.example.ayosehat.utils.SharedPref
import com.example.ayosehat.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.time.Duration.Companion.seconds

@Composable
fun Home(navHostController: NavHostController){

    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val postAndUsers by homeViewModel.postAndUsers.observeAsState(null)


    LazyColumn{
        items(postAndUsers ?: emptyList()){pairs ->
            PostItem(post = pairs.first,
                users = pairs.second,
                navHostController,
                FirebaseAuth.getInstance().currentUser!!.uid)
        }
    }


}

@Preview(showBackground = true)
@Composable
fun ShowHome(){
//    Home()
}