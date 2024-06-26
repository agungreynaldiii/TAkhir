package com.example.ayosehat.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import android.util.Log

class HomeViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val post = db.getReference("post")

    private val _postAndUsers = MutableLiveData<List<Pair<PostModel, UserModel>>>()
    val postAndUsers: LiveData<List<Pair<PostModel, UserModel>>> = _postAndUsers

    init {
        fetchPostAndUsers {
            Log.d("HomeViewModel", "Fetched posts and users: $it")
            _postAndUsers.value = it
        }
    }

    private fun fetchPostAndUsers(onResult: (List<Pair<PostModel, UserModel>>) -> Unit){
        post.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<Pair<PostModel, UserModel>>()
                for (postSnapshot in snapshot.children){
                    val post = postSnapshot.getValue(PostModel::class.java)
                    post?.let {
                        fetchUserFromPost(it) { user ->
                            result.add(0, it to user)

                            if (result.size == snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeViewModel", "Database error: ${error.message}")
            }
        })
    }

    fun fetchUserFromPost(post: PostModel, onResult: (UserModel) -> Unit){
        db.getReference("users").child(post.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeViewModel", "Database error: ${error.message}")
                }
            })
    }
}