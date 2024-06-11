package com.example.ayosehat.screens

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.data.EmptyGroup.data
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.ayosehat.api.ApiService
import com.example.ayosehat.api.MlApiService
import com.google.common.collect.Table
import java.io.File
import java.time.format.TextStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImageUpload(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp), // Top padding for content to avoid overlap with TopAppBar
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            var imageFile by remember { mutableStateOf<File?>(null) }
            var isLoading by remember { mutableStateOf(false) }
            var error by remember { mutableStateOf<String?>(null) }
            var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
            var labels by remember { mutableStateOf<List<String>>(emptyList()) }
            var foodInfos by remember { mutableStateOf<List<FoodInfo>>(emptyList()) }

            // Reset all state variables to their initial values
            fun resetState() {
                imageFile = null
                isLoading = false
                error = null
                uploadedImageUrl = null
                labels = emptyList()
                foodInfos = emptyList()
            }

            // Image selection launcher
            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                resetState() // Reset state when a new image is selected
                uri?.let {
                    Glide.with(context)
                        .asBitmap()
                        .load(it)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                val file = File(context.cacheDir, "image.jpg")
                                file.outputStream().use { out ->
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, out)
                                }
                                imageFile = file
                                Log.d(TAG, "Image file created: ${file.path}")
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                // Handle image clearing if needed
                            }
                        })
                }
            }

            // Image selection button
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Select Image")
            }

            // Image preview
            imageFile?.let { file ->
                val bitmap = BitmapFactory.decodeFile(file.path)
                val imageBitmap = bitmap.asImageBitmap()
                Image(bitmap = imageBitmap, contentDescription = null,
                    modifier = Modifier.size(150.dp))
            }

            // Upload button
            Button(onClick = {
                imageFile?.let { file ->
                    isLoading = true
                    error = null

                    Log.d(TAG, "Uploading image: ${file.path}")

                    MlApiService.uploadImage(file) { detectedImgPath, detectedLabels, detectedFoodInfos ->
                        isLoading = false
                        if (detectedImgPath != null && detectedLabels != null && detectedFoodInfos != null) {
                            uploadedImageUrl = detectedImgPath
                            labels = detectedLabels
                            foodInfos = detectedFoodInfos
                            Log.d(TAG, "Image uploaded successfully: $uploadedImageUrl")
                            Log.d(TAG, "Detected labels: $labels")
                        } else {
                            error = "Failed to upload image"
                            Log.e(TAG, error!!)
                        }
                    }
                }
            }) {
                Text("Upload Image")
            }

            // Loading state
            if (isLoading) {
                CircularProgressIndicator()
            }

            // Error state
            error?.let {
                Text(text = it, color = Color.Red)
            }

            // Display uploaded image
            uploadedImageUrl?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = "Uploaded Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            // Display detected labels and additional info in a table format
            if (labels.isNotEmpty() && foodInfos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Table(
                    headers = listOf("Makanan", "Karbo", "Protein", "Lemak", "Range Kalori"),
                    data = foodInfos.map { it.toTableRow() },
                    foodInfos = foodInfos
                )
            }
        }

        // TopAppBar
        TopAppBar(
            title = { Text("Image Upload") },
            actions = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Outlined.Check, contentDescription = "Check")
                }
            },
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

// Data class to hold food information
data class FoodInfo(
    val makanan: String,
    val karbo: String,
    val protein: String,
    val lemak: String,
    val rangeKalori: String
) {
    // Function to convert FoodInfo to a list of strings
    fun toTableRow(): List<String> {
        return listOf(makanan, karbo, protein, lemak, rangeKalori)
    }
}

// Function to create a table with headers and data
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Table(headers: List<String>, data: List<List<String>>, foodInfos: List<FoodInfo>) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            headers.forEach {
                Text(
                    text = it,
                    style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )
            }
        }
        Divider(color = Color.Black, thickness = 1.dp)
        data.forEach { rowData ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowData.forEach {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    )
                }
            }
            Divider(color = Color.Black, thickness = 1.dp)
        }

        // Calculate totals
        val totalKarbo = foodInfos.sumOf { it.karbo.toDoubleOrNull() ?: 0.0 }
        val totalProtein = foodInfos.sumOf { it.protein.toDoubleOrNull() ?: 0.0 }
        val totalLemak = foodInfos.sumOf { it.lemak.toDoubleOrNull() ?: 0.0 }
        val totalKalori = foodInfos.sumOf {it.rangeKalori.toDoubleOrNull() ?: 0.0}

        // Add totals row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
            Text(
                text = "$totalKarbo g",
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
            Text(
                text = "$totalProtein g",
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
            Text(
                text = "$totalLemak g",
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
            Text(
                text = "$totalKalori kkal",
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
        }
    }
}