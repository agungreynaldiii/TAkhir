//package com.example.ayosehat.screens
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.example.ayosehat.ui.theme.RobotoRegular
//import com.example.ayosehat.R
//
//@Composable
//fun Welcome(
//    navController: NavController.Companion,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier.fillMaxSize()
//    ) {
//        // Solid color background
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center,
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(colorResource(id = R.color.main)),
//                contentAlignment = Alignment.Center
//            ) {
//
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 24.dp)
//                ) {
//                    Spacer(modifier = Modifier.weight(1f))
//
//                    Text(
//                        "AyoSehat!",
//                        fontSize = 48.sp,
//                        fontFamily = RobotoRegular,
//                        fontWeight = FontWeight(500),
//                        color = Color.White // Fully qualify the Color class
//                    )
//
//                    Text(
//                        "Pantau Gizi Harianmu dengan Mudah",
//                        textAlign = TextAlign.Center,
//                        fontFamily = RobotoRegular,
//                        fontSize = 15.sp,
//                        fontWeight = FontWeight(700),
//                        color = Color.White // Fully qualify the Color class
//                    )
//
//                    Spacer(modifier = Modifier.weight(1f))
//
//                    Button(
//                        onClick = { navController.navigate("Register") },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White), // Fully qualify the Color class
//                        modifier = Modifier
//                            .width(385.dp)
//                            .height(43.dp),
//                        shape = RoundedCornerShape(11.dp)
//                    ) {
//                        // Button content
//                        Text(
//                            text = "Mulai",
//                            color = colorResource(id = R.color.main),
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.weight(0.02f))
//
//                    OutlinedButton(
//                        onClick = { navController.navigate("Login") },
//                        modifier = Modifier
//                            .width(385.dp)
//                            .height(43.dp),
//                        border = BorderStroke(1.dp, Color.White), // Fully qualify the Color class
//                        shape = RoundedCornerShape(11.dp)
//                    ) {
//                        Text(
//                            text = "Masuk",
//                            color = Color.White, // Fully qualify the Color class
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//
//                    Spacer(modifier = Modifier.weight(0.1f))
//                }
//            }
//        }
//    }
//}
//
//
//
//@Preview(showBackground = true, widthDp = 320, heightDp = 640)
//@Composable
//fun WelcomePreview() {
//    Welcome(rememberNavController())
//}