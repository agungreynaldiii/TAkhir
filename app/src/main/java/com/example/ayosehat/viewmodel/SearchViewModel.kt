package com.example.ayosehat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ayosehat.model.FoodModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val foodsRef = db.getReference("foods") // Ganti "foods" dengan path referensi Firebase Anda

    private val _foodsList = MutableLiveData<List<FoodModel>>(emptyList())
    val foodsList: LiveData<List<FoodModel>> = _foodsList

    private val _selectedFoods = MutableLiveData<List<FoodModel>>(emptyList())
    val selectedFoods: LiveData<List<FoodModel>> = _selectedFoods

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        fetchFoods()
    }

    private fun fetchFoods() {
        foodsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val foodList = snapshot.children.mapNotNull { it.getValue(FoodModel::class.java) }
                    _foodsList.value = foodList
                    _errorMessage.value = null // Bersihkan pesan error jika berhasil
                } catch (e: Exception) {
                    _errorMessage.value = "Error fetching data: ${e.message}"
                    Log.e("SearchViewModel", "Error fetching food data", e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = "Data fetching cancelled: ${error.message}"
                Log.e("SearchViewModel", "Error fetching food data", error.toException())
            }
        })
    }

    fun addSelectedFood(food: FoodModel) {
        val currentSelectedFoods = _selectedFoods.value?.toMutableList() ?: mutableListOf()
        currentSelectedFoods.add(food)
        _selectedFoods.value = currentSelectedFoods
    }

    fun removeSelectedFood(food: FoodModel) {
        _selectedFoods.value = _selectedFoods.value?.toMutableList()?.filter { it != food }
    }
}
