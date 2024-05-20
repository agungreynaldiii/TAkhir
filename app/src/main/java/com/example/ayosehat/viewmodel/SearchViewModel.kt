package com.example.ayosehat.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ayosehat.model.FoodModel
import com.example.ayosehat.model.PostModel
import com.example.ayosehat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class SearchViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val foods = db.getReference("foods")

    private val _foods = MutableLiveData<List<FoodModel>>()
    val foodsList: LiveData<List<FoodModel>> = _foods

    init {
        fetchFoods {
            _foods.value = it
        }
    }

    private fun fetchFoods(onResult: (List<FoodModel>) -> Unit){
        foods.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<FoodModel>()
                for (postSnapshot in snapshot.children){
                    val post = postSnapshot.getValue(FoodModel::class.java)
                    result.add(post!!)
                }
                onResult(result)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

//    fun fetchUserFromPost(post: PostModel, onResult: (UserModel) -> Unit){
//        db.getReference("users").child(post.userId)
//            .addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                    val user = snapshot.getValue(UserModel::class.java)
//                    user?.let(onResult)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//            })
//    }

}