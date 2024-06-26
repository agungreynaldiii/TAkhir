package com.example.ayosehat.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ayosehat.navigation.Routes
import com.example.ayosehat.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val error by authViewModel.error.observeAsState()
    val isLoading by authViewModel.isLoading.observeAsState(false)

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    val context = LocalContext.current

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            authViewModel.clearError()
        }
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Login", style = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        ))

        Box(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, "")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.height(30.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            ElevatedButton(onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else {
                    authViewModel.login(email, password, context)
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Login",
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }

        TextButton(onClick = {
            navController.navigate(Routes.Register.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Buat Akun",
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginView() {
    // This is only for preview purpose
}
