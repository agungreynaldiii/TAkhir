package com.example.ayosehat.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    var route: String,
    val icon: ImageVector
)
