//package com.example.ayosehat.itemview
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.constraintlayout.compose.ConstraintLayout
//import androidx.navigation.NavHostController
//import com.example.ayosehat.model.FoodModel
//
//@Composable
//fun FoodItem(
//    foods: FoodModel,
//    navHostController: NavHostController
//){
//    Column {
//        ConstraintLayout(modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)) {
//
//            val (nama, protein, kalori, karbo, lemak) = createRefs()
//
//            Text(
//                text = foods.nama,
//                style = TextStyle(
//                    fontSize = 20.sp
//                ), modifier = Modifier.constrainAs(nama) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                }
//            )
//
//            Text(
//                text = foods.protein.toString(),
//                style = TextStyle(
//                    fontSize = 20.sp
//                ), modifier = Modifier.constrainAs(protein) {
//                    top.linkTo(nama.bottom, margin = 8.dp)
//                    start.linkTo(nama.start)
//                }
//            )
//
//            Text(
//                text = foods.kalori.toString(),
//                style = TextStyle(
//                    fontSize = 20.sp
//                ), modifier = Modifier.constrainAs(kalori) {
//                    top.linkTo(protein.bottom, margin = 8.dp)
//                    start.linkTo(protein.start)
//                }
//            )
//
//            Text(
//                text = foods.karbo.toString(),
//                style = TextStyle(
//                    fontSize = 20.sp
//                ), modifier = Modifier.constrainAs(karbo) {
//                    top.linkTo(kalori.bottom, margin = 8.dp)
//                    start.linkTo(kalori.start)
//                }
//            )
//
//            Text(
//                text = foods.lemak.toString(),
//                style = TextStyle(
//                    fontSize = 20.sp
//                ), modifier = Modifier.constrainAs(lemak) {
//                    top.linkTo(karbo.bottom, margin = 8.dp)
//                    start.linkTo(karbo.start)
//                }
//            )
//
//        }
//    }
//
//    Divider(color = Color.Black, thickness = 1.dp)
//
//}
//
////@Preview(showBackground = true)
////@Composable
////fun ShowPostItem() {
////    PostItem()
////}