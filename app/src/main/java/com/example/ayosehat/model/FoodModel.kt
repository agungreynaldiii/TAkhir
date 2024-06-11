package com.example.ayosehat.model

data class FoodModel(
    val nama: String = "",
    val kalori: Float = 0f,
    val lemak: Float = 0f,
    val karbo: Float = 0f,
    val protein: Float = 0f,
    var isSelected: Boolean = false,
    var isExpanded: Boolean = false
)
