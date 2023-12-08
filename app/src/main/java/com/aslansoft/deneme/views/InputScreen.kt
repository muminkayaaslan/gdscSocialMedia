package com.aslansoft.deneme.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R

@Composable
fun InputScreen(navController: NavHostController) {
    val backgroundColor = Color(3, 3, 70, 255)
    Surface(color = backgroundColor) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)){
            Image(modifier = Modifier
                .align(Alignment.Center)
                .background(backgroundColor),bitmap = ImageBitmap.imageResource(R.drawable.iconbg),contentDescription = null )
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedButton(onClick = { navController.navigate("login_screen") }) {
                Text(text = "Log In", color = Color.White)
            }
            Spacer(modifier = Modifier.padding(5.dp))
            OutlinedButton(onClick = { navController.navigate("register_screen") }) {
                Text(text = "Sign Up",color = Color.White)
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(modifier = Modifier,onClick = {  }, colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green
            )) {
                Text("Hello world")
            }
        }
    }
}

