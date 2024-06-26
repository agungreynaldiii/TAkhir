package com.example.ayosehat.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ayosehat.R
import com.example.ayosehat.navigation.Routes
import com.example.ayosehat.utils.SharedPref
import com.example.ayosehat.viewmodel.AddViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Add(navHostController: NavHostController){

    val postViewModel: AddViewModel = viewModel()
    val isPosted by postViewModel.isPosted.observeAsState(false)
    val isLoading by postViewModel.isLoading.observeAsState(false)
    val errorMessage by postViewModel.errorMessage.observeAsState()

    val context = LocalContext.current

    var post by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else android.Manifest.permission.READ_EXTERNAL_STORAGE

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
            isGranted: Boolean ->
        if (isGranted){

        } else {

        }
    }

    LaunchedEffect(isPosted) {
        if (isPosted) {
            post = ""
            imageUri = null
            Toast.makeText(context, "Berhasil post", Toast.LENGTH_SHORT).show()

            navHostController.navigate(Routes.Home.routes) {
                popUpTo(Routes.Add.routes) {
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            postViewModel.clearErrorMessage() // Clear the error message after showing it
        }
    }

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        val (crossPic, text, logo, username, editText, attachMedia,
            button, imageBox, progressIndicator) = createRefs()

        Image(painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "close",
            modifier = Modifier
                .constrainAs(crossPic) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {
                    navHostController.navigate(Routes.Home.routes) {
                        popUpTo(Routes.Add.routes) {
                            inclusive = true
                        }
                    }
                })

        Text(text = "Buat Post",
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            ), modifier = Modifier.constrainAs(text) {
                top.linkTo(crossPic.top)
                start.linkTo(crossPic.end, margin = 12.dp)
                bottom.linkTo(crossPic.bottom)
            }
        )

        Image(painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)),
            contentDescription = "close",
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(text.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(text = SharedPref.getUsername(context),
            style = TextStyle(
                fontSize = 20.sp
            ), modifier = Modifier.constrainAs(username) {
                top.linkTo(logo.top)
                start.linkTo(logo.end, margin = 12.dp)
                bottom.linkTo(logo.bottom)
            }
        )

        BasicTextFieldWithHint(hint = "Posting ...", value = post,
            onValueChange = { post = it }, modifier = Modifier
                .constrainAs(editText) {
                    top.linkTo(username.bottom)
                    start.linkTo(username.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth())

        if (imageUri == null) {
            Image(painter = painterResource(id = R.drawable.baseline_attach_file_24),
                contentDescription = "close",
                modifier = Modifier
                    .constrainAs(attachMedia) {
                        top.linkTo(editText.bottom)
                        start.linkTo(editText.start)
                    }
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    })
        } else {
            Box(modifier = Modifier
                .background(Color.Gray)
                .padding(1.dp)
                .constrainAs(imageBox) {
                    top.linkTo(editText.bottom)
                    start.linkTo(editText.start)
                    end.linkTo(parent.end)
                }
                .height(250.dp)) {
                Image(painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "close",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Icon(imageVector = Icons.Default.Close, contentDescription = "Remove Image",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                            imageUri = null
                        })
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progressIndicator) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        } else {
            TextButton(
                onClick = {
                    postViewModel.saveImage(post, FirebaseAuth.getInstance().currentUser!!.uid, imageUri)
                },
                modifier = Modifier
                    .constrainAs(button) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .background(colorResource(id = R.color.main))
            ) {
                Text(text = "POST", style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White
                ))
            }
        }
    }
}

@Composable
fun BasicTextFieldWithHint(hint: String, value: String, onValueChange: (String) -> Unit,
                           modifier: Modifier) {

    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(text = hint, color = Color.Gray)
        }

        BasicTextField(value = value, onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddPostView() {
    // Add() Uncomment to see preview
}
