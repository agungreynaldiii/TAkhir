package com.example.ayosehat.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ayosehat.model.UserModel
import com.example.ayosehat.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: MutableLiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        if (!isValidEmail(email)) {
            _error.postValue("Email harus dalam format @gmail.com")
            return
        }

        if (!isValidPassword(password)) {
            _error.postValue("Password minimal 8 huruf")
            return
        }

        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                _isLoading.value = false
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    getData(auth.currentUser!!.uid, context)
                } else {
                    _error.postValue("Password/email tidak sesuai")
                }
            }
    }

    private fun getData(uid: String, context: Context) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                if (userData != null) {
                    SharedPref.storeData(userData.name, userData.email, userData.bio, userData.username, userData.imageUrl, context)
                } else {
                    _error.postValue("Gagal mengambil data pengguna")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.postValue("Gagal mengambil data pengguna: ${error.message}")
            }
        })
    }

    fun register(email: String, password: String, name: String, bio: String, username: String, imageUri: Uri, context: Context) {
        if (!isValidEmail(email)) {
            _error.postValue("Email harus dalam format @gmail.com")
            return
        }

        if (!isValidPassword(password)) {
            _error.postValue("Password minimal 8 huruf")
            return
        }

        if (name.isEmpty() || bio.isEmpty() || username.isEmpty() || imageUri == Uri.EMPTY) {
            _error.postValue("Semua field harus terisi")
            return
        }

        checkUsernameExists(username) { exists ->
            if (exists) {
                _error.postValue("Username tidak boleh sama")
            } else {
                _isLoading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        _isLoading.value = false
                        if (it.isSuccessful) {
                            _firebaseUser.postValue(auth.currentUser)
                            saveImage(email, password, name, bio, username, imageUri, auth.currentUser?.uid, context)
                        } else {
                            _error.postValue("Gagal mendaftar: ${it.exception?.message}")
                        }
                    }
            }
        }
    }

    private fun checkUsernameExists(username: String, callback: (Boolean) -> Unit) {
        userRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.exists())
            }

            override fun onCancelled(error: DatabaseError) {
                _error.postValue("Gagal memeriksa username: ${error.message}")
            }
        })
    }

    private fun saveImage(email: String, password: String, name: String, bio: String, username: String, imageUri: Uri, uid: String?, context: Context) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email, password, name, bio, username, it.toString(), uid, context)
            }.addOnFailureListener {
                _error.postValue("Gagal mendapatkan URL gambar")
            }
        }.addOnFailureListener {
            _error.postValue("Gagal mengunggah gambar")
        }
    }

    private fun saveData(email: String, password: String, name: String, bio: String, username: String, imageUrl: String, uid: String?, context: Context) {
        val userData = UserModel(email, password, name, bio, username, imageUrl)

        userRef.child(uid!!).setValue(userData).addOnSuccessListener {
            SharedPref.storeData(name, email, bio, username, imageUrl, context)
        }.addOnFailureListener {
            _error.postValue("Gagal menyimpan data pengguna")
        }
    }

    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }

    private fun isValidEmail(email: String): Boolean {
        return email.endsWith("@gmail.com") && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun clearError() {
        _error.postValue(null)
    }
}