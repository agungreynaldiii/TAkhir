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

class UserViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val postRef = db.getReference("post")
    val userRef = db.getReference("users")

    private val _post = MutableLiveData(listOf<PostModel>())
    val post : LiveData<List<PostModel>> get() = _post

    private val _users = MutableLiveData(UserModel())
    val users : LiveData<UserModel> get() = _users

    fun fetchUser(uid: String){
        userRef.child(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchPost(uid: String){
        postRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = snapshot.children.mapNotNull {
                    it.getValue(PostModel::class.java)
                }
                _post.postValue(postList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}