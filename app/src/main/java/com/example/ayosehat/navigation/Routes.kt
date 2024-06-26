package com.example.ayosehat.navigation

sealed class Routes(val routes: String){
//    object  Welcome : Routes("welcome")
    object  Home : Routes("home")
    object  Notification : Routes("notification")
    object  Profile : Routes("profile")
    object  Search : Routes("search")
    object  Splash : Routes("splash")
    object  Add : Routes("add")
    object BottomNav: Routes("bottomNav")
    object  Login : Routes("login")
    object Register: Routes("register")
    object ChatGPT: Routes("chatgpt")
    object Result : Routes("result/{selectedFoods}") {
        fun createRoute(selectedFoods: String) = "result/$selectedFoods"
    }
    object ImageUpload: Routes("imageUpload")

}
