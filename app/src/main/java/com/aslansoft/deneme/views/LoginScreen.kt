@file:Suppress("DEPRECATION")

package com.aslansoft.deneme.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), contentAlignment = Alignment.Center){
            Image(modifier = Modifier.padding(50.dp),bitmap = ImageBitmap.imageResource(R.drawable.logo_icon_social), contentDescription = null )
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

            } , label = { Text(text = "E-Posta", fontFamily = googleSans)},
                placeholder = { Text(text = "username@example.com",fontFamily = googleSans)},
                
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    disabledLabelColor = Color.LightGray,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = Color.White,
                    selectionColors = TextSelectionColors(handleColor = MaterialTheme.colorScheme.onPrimary, backgroundColor = MaterialTheme.colorScheme.primary),
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email))
            Spacer(modifier = Modifier.padding(5.dp))
            val passwordVisibility = remember {
                mutableStateOf(false)
            }
            OutlinedTextField(value = password, onValueChange ={
                password = it
            },label = { Text(text = "Parola",fontFamily = googleSans)},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    disabledLabelColor = Color.LightGray,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = Color.White,
                    selectionColors = TextSelectionColors(handleColor = MaterialTheme.colorScheme.onPrimary, backgroundColor = MaterialTheme.colorScheme.primary),
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (passwordVisibility.value == true){
                        Image(modifier = Modifier
                            .size(20.dp)
                            .clickable { passwordVisibility.value = false },bitmap = ImageBitmap.imageResource(R.drawable.visibility_off), contentDescription = null, colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.secondary))
                    }else{
                        Image(modifier = Modifier
                            .size(20.dp)
                            .clickable { passwordVisibility.value = true },bitmap = ImageBitmap.imageResource(R.drawable.visibility), contentDescription = null , colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.secondary) )
                    }

                }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            OutlinedButton(onClick = { if (userEmail.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(userEmail,password).addOnSuccessListener {
                    navController.navigate("main_screen")

                }.addOnFailureListener{
                    Toast.makeText(context,"Giriş Yapma Başarısız",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"Gerekli Alanları Doldurun", Toast.LENGTH_SHORT).show()
            }
            }) {
                Text(text = "Giriş Yap",color = Color.White,fontFamily = googleSans)
            }
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            Row (modifier = Modifier
                .fillMaxWidth()
                .height(20.dp), horizontalArrangement = Arrangement.Center){
                Text("Hesabın Yok Mu?", color = MaterialTheme.colorScheme.secondary, fontSize = 15.sp,fontFamily = googleSans)
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(modifier = Modifier.clickable {
                                                   navController.navigate("register_screen")
                },text = "Kaydol", color = MaterialTheme.colorScheme.onPrimary, fontSize = 15.sp,fontFamily = googleSans)
            }

        }
    }
}