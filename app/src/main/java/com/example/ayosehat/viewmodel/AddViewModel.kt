package com.example.ayosehat.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ayosehat.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("post")

    private val storageRef = FirebaseStorage.getInstance().reference
    private val imageRef = storageRef.child("post/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun saveImage(post: String, userId: String, imageUri: Uri?) {
        // Validate if either post (caption) or imageUri is provided
        if (post.isBlank() && imageUri == null) {
            _errorMessage.value = "Postingan tidak boleh kosong"
            return
        }

        _isLoading.value = true
        if (imageUri != null) {
            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    saveData(post, userId, it.toString())
                }
            }.addOnFailureListener {
                _isLoading.postValue(false)
                _errorMessage.postValue("Failed to upload image.")
            }
        } else {
            saveData(post, userId, "")
        }
    }

    fun saveData(post: String, userId: String, image: String) {
        val postData = PostModel(post, image, userId, System.currentTimeMillis().toString())
        userRef.child(userRef.push().key!!).setValue(postData).addOnSuccessListener {
            _isPosted.postValue(true)
            _isLoading.postValue(false)
        }.addOnFailureListener {
            _isPosted.postValue(false)
            _isLoading.postValue(false)
            _errorMessage.postValue("Failed to post data.")
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}