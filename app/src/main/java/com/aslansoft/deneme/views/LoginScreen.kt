@file:Suppress("DEPRECATION")

package com.aslansoft.deneme.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val backgroundColor = Color(3, 3, 70, 255)
    Surface(color = backgroundColor) {
        Box(modifier = Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center){
            Image(modifier = Modifier.padding(50.dp),bitmap = ImageBitmap.imageResource(R.drawable.iconbg), contentDescription = null )
        }
        Column(Modifier.fillMaxSize(),Arrangement.Center,Alignment.CenterHorizontally) {
            var userEmail by remember {
                mutableStateOf("")
            }
            var password: String by remember {
                mutableStateOf("")
            }
            val auth = Firebase.auth
            val context = LocalContext.current

            OutlinedTextField(value = userEmail, onValueChange ={
                userEmail = it

            } , label = { Text(text = "E-Mail")},
                placeholder = { Text(text = "username@example.com")},
                
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.Yellow,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email))
            Spacer(modifier = Modifier.padding(5.dp))
            val passwordVisibility = remember {
                mutableStateOf(false)
            }
            OutlinedTextField(value = password, onValueChange ={
                password = it
            },label = { Text(text = "Password")},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedLabelColor = Color.White,
                    focusedLabelColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.padding(5.dp))
            OutlinedButton(onClick = { if (userEmail.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(userEmail,password).addOnSuccessListener {
                    navController.navigate("main_screen")

                }.addOnFailureListener{
                    Toast.makeText(context,"Password Error Or Email Error",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"Gerekli Alanları Doldurun", Toast.LENGTH_SHORT).show()
            }
            }) {
                Text(text = "Log In",color = Color.White)
            }
        }
    }
}