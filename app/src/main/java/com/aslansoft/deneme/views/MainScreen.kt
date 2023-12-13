package com.aslansoft.deneme.views


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun MainScreen(navController: NavHostController) {
val color = Color(3,3,70)
 Surface(modifier = Modifier.fillMaxSize(),color = color){
  Column( verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
     Button(onClick = { Firebase.auth.signOut()
                        navController.navigate("input_screen")},
         colors = ButtonDefaults.buttonColors(
             disabledContentColor = Color.Gray,
             contentColor = Color.White
         )) {
            Text(text = "Çıkış Yap", color = color)
     }
      Spacer(modifier = Modifier.padding(20.dp))
      Button(onClick = { navController.navigate("post_screen") }) {
          Text(text = "Paylaş")
      }
      Spacer(modifier = Modifier.padding(20.dp))
      Button(onClick = { navController.navigate("see_post_screen") }) {
          Text(text = "Postları Gör")
      }
  }



 }
}
