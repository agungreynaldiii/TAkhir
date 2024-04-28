package com.example.ayosehat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ayosehat.screens.Add
import com.example.ayosehat.screens.BottomNav
import com.example.ayosehat.screens.Home
import com.example.ayosehat.screens.Login
import com.example.ayosehat.screens.Notification
import com.example.ayosehat.screens.Profile
import com.example.ayosehat.screens.Register
import com.example.ayosehat.screens.Search
import com.example.ayosehat.screens.Splash

@Composable
fun NavGraph(navController: NavHostController){

    NavHost(navController = navController,
        startDestination = Routes.Splash.routes){


        composable(Routes.Splash.routes){
            Splash(navController)
        }

        composable(Routes.Home.routes){
            Home(navController)
        }

        composable(Routes.Search.routes){
            Search()
        }

        composable(Routes.Notification.routes){
            Notification()
        }

        composable(Routes.Profile.routes){
            Profile(navController)
        }

        composable(Routes.Search.routes){
            Search()
        }

        composable(Routes.Add.routes){
            Add(navController)
        }

        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }

        composable(Routes.Login.routes){
            Login(navController)
        }

        composable(Routes.Register.routes){
            Register(navController)
        }
    }
}