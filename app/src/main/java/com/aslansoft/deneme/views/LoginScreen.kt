@file:Suppress("DEPRECATION")

package com.aslansoft.deneme.views

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.firestore.ktx.firestore
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
        var userEmail by remember {
            mutableStateOf("")
        }
        var password: String by remember {
            mutableStateOf("")
        }
        val userState = remember {
            mutableStateOf(false)
        }
        val auth = Firebase.auth
        val context = LocalContext.current
        val db = Firebase.firestore


        Column(Modifier.fillMaxSize(),Arrangement.Center,Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(top = 30.dp))


            OutlinedTextField(value = userEmail, onValueChange ={
                userEmail = it

            } , label = { Text(text = "E-Posta", fontFamily = googleSans)},
                placeholder = { Text(text = "username@example.com",fontFamily = googleSans)},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = Color.Gray,
                    focusedTextColor = MaterialTheme.colorScheme.background,
                    disabledLabelColor = Color.LightGray,
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedLabelColor = Color.Gray,
                    selectionColors = TextSelectionColors(handleColor = MaterialTheme.colorScheme.onSecondary, backgroundColor = MaterialTheme.colorScheme.primary),
                    cursorColor = MaterialTheme.colorScheme.onSecondary
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email))
            Spacer(modifier = Modifier.padding(5.dp))
            val passwordVisibility = remember {
                mutableStateOf(false)
            }
            OutlinedTextField(value = password, onValueChange ={
                password = it
            },label = { Text(text = "Parola",fontFamily = googleSans)},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = Color.Gray,
                    focusedTextColor = MaterialTheme.colorScheme.background,
                    disabledLabelColor = Color.LightGray,
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedLabelColor = Color.Gray,
                    selectionColors = TextSelectionColors(handleColor = MaterialTheme.colorScheme.onSecondary, backgroundColor = MaterialTheme.colorScheme.primary),
                    cursorColor = MaterialTheme.colorScheme.onSecondary
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (passwordVisibility.value == true){
                        Image(modifier = Modifier
                            .size(20.dp)
                            .clickable { passwordVisibility.value = false },bitmap = ImageBitmap.imageResource(R.drawable.visibility_off), contentDescription = null, colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.onSecondary))
                    }else{
                        Image(modifier = Modifier
                            .size(20.dp)
                            .clickable { passwordVisibility.value = true },bitmap = ImageBitmap.imageResource(R.drawable.visibility), contentDescription = null , colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.onSecondary))
                    }

                }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            OutlinedButton(onClick = {
                if (userEmail.isNotEmpty() && userEmail.contains("@") && userEmail.endsWith(".com")){
                    db.collection("users").document(userEmail).get().addOnSuccessListener {
                        val data = it.data
                        userState.value = data?.get("user_state") as Boolean
                    }
                }
                if (userEmail.isNotEmpty() && password.isNotEmpty()){
                    if (userState.value){
                        auth.signInWithEmailAndPassword(userEmail,password).addOnSuccessListener {
                            val user = auth.currentUser
                            if (user != null && user.isEmailVerified){
                                navController.navigate("main_screen" )
                                println("userstate:${userState.value}")
                            }else{
                                Toast.makeText(context,"Lütfen E-postanızı Doğrulayın",Toast.LENGTH_SHORT).show()
                                println("userstate:${userState.value}")
                            }


                        }.addOnFailureListener{
                            Toast.makeText(context,"Giriş Yapma Başarısız",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(context,"Yönetici Onayı Bekleniyor...",Toast.LENGTH_SHORT).show()
                    }



            }else{
                Toast.makeText(context,"Gerekli Alanları Doldurun", Toast.LENGTH_SHORT).show()
            }
            }, border = BorderStroke(color = MaterialTheme.colorScheme.onBackground, width = 1.dp)) {
                Text(text = "Giriş Yap",color = MaterialTheme.colorScheme.onBackground,fontFamily = googleSans)
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(modifier = Modifier.clickable {
                if (userEmail.isNotEmpty()) {
                    auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Parola Sıfırlama E-Postası Gönderildi", Toast.LENGTH_SHORT).show()
                        } else {
                            println(task.exception)
                            Toast.makeText(context, "E-posta Gönderilemedi", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Sıfırlamak İstediğiniz Mail Adresini Giriniz", Toast.LENGTH_SHORT).show()
                }

            },text = "Parola Sıfırla", color = MaterialTheme.colorScheme.secondary, fontSize = 15.sp,fontFamily = googleSans)
            Spacer(modifier = Modifier.padding(vertical = 2.dp))

            Text(
                modifier = Modifier.clickable {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Doğrulama E-postası Gönderildi",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Doğrulama E-postası Gönderilemedi",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


                        }
                },
                text = "Doğrulama E-postası Gönder",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 15.sp,
                fontFamily = googleSans
            )
            Text(modifier = Modifier.clickable {
                navController.navigate("guest_screen")
            },text = "Misafir Kullanıcı",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 15.sp,
                fontFamily = googleSans)
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Column (
                Modifier
                    .fillMaxWidth()
                    .height(50.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally){
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.8.dp,
                    color = MaterialTheme.colorScheme.background
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Row (modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    if (isSystemInDarkTheme()){
                        Text("Hesabın Yok Mu?", color = MaterialTheme.colorScheme.secondary, fontSize = 15.sp,fontFamily = googleSans)
                    }
                    else
                    {
                        Text("Hesabın Yok Mu?", color = Color.Gray, fontSize = 15.sp,fontFamily = googleSans)
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(modifier = Modifier.clickable {
                        navController.navigate("register_screen")
                    },text = "Kaydol", color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp,fontFamily = googleSans)
                }
            }

        }
        val activity = (LocalContext.current as? Activity)
    BackHandler(true) {
        activity?.finish()
    }
    }

}