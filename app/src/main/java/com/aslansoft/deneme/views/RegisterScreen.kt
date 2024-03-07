@file:Suppress("DEPRECATION")

package com.aslansoft.deneme.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    val auth = Firebase.auth


    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
        val userEmail = remember {
            mutableStateOf("")
        }

        val password = remember {
            mutableStateOf("")
        }
        val accessPassword = remember{
            mutableStateOf("")
        }
        val username = remember {
            mutableStateOf("")
        }
        val db = Firebase.firestore


        val context = LocalContext.current
        val passwordVisibility = remember {
            mutableStateOf(false)
        }
        val accessPasswordVisibility = remember {
            mutableStateOf(false)
        }
        val menuIsState = remember {
            mutableStateOf(false)
        }
        val userType = remember {
            mutableStateOf("")
        }
        val userState = remember {
            mutableStateOf(false)
        }


        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), contentAlignment = Alignment.Center){
                Image(bitmap = ImageBitmap.imageResource(R.drawable.logo_icon_social), contentDescription = null )
            }
            Box(modifier = Modifier
                .height(50.dp)
                .zIndex(1f), contentAlignment = Alignment.Center){
                Text(text = "Hoşgeldin ${username.value}",color= MaterialTheme.colorScheme.onPrimary, fontSize = 30.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold, fontFamily = googleSans)
            }
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(value = username.value , onValueChange ={
                username.value = it
            }, singleLine = true
                ,label = { Text(text = "Kullanıcı Adı", fontFamily = googleSans)},
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                println(username.value)
            OutlinedTextField(value = userEmail.value, onValueChange = {
                userEmail.value = it
            }, label = { Text(text = "E-Posta", fontFamily = googleSans) },
                placeholder = { Text(text = "username@example.com", fontFamily = googleSans)},
                singleLine = true, colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = Color.Gray,
                    focusedTextColor = MaterialTheme.colorScheme.background,
                    disabledLabelColor = Color.LightGray,
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedLabelColor = Color.Gray,
                    selectionColors = TextSelectionColors(handleColor = MaterialTheme.colorScheme.onSecondary, backgroundColor = MaterialTheme.colorScheme.primary),
                    cursorColor = MaterialTheme.colorScheme.onSecondary
                ),keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(value = password.value, onValueChange = {
                password.value = it
            }, label = { Text(text = "Parola", fontFamily = googleSans) },
                singleLine = true, colors = TextFieldDefaults.outlinedTextFieldColors(
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                , visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
                , trailingIcon = {
                    if (passwordVisibility.value){
                        Image(modifier = Modifier
                            .size(20.dp)
                            .clickable { passwordVisibility.value = false },bitmap = ImageBitmap.imageResource(R.drawable.visibility_off), contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary))
                    }else{
                        Image(modifier = Modifier
                            .size(20.dp)
                            .clickable { passwordVisibility.value = true },bitmap = ImageBitmap.imageResource(R.drawable.visibility), contentDescription = null , colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary) )
                    }

                }

            )

            OutlinedTextField(value = accessPassword.value, onValueChange = {
                accessPassword.value = it
            }, label = { Text(text = "Parola Doğrula", fontFamily = googleSans) },
                singleLine = true, colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = Color.Gray,
                    focusedTextColor = MaterialTheme.colorScheme.background,
                    disabledLabelColor = Color.LightGray,
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedLabelColor = Color.Gray,
                    selectionColors = TextSelectionColors(handleColor = MaterialTheme.colorScheme.onSecondary, backgroundColor = MaterialTheme.colorScheme.primary),
                    cursorColor = MaterialTheme.colorScheme.onSecondary
                ),keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (accessPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
                , trailingIcon = {
                    if (accessPasswordVisibility.value){
                        Image(modifier = Modifier
                            .size(20.dp)
                            .clickable { accessPasswordVisibility.value = false },bitmap = ImageBitmap.imageResource(R.drawable.visibility_off), contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary))
                    }else{
                        Image(modifier = Modifier
                            .size(20.dp)
                            .clickable { accessPasswordVisibility.value = true },bitmap = ImageBitmap.imageResource(R.drawable.visibility), contentDescription = null , colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary) )
                    }

                }

            )
            Spacer(modifier = Modifier.padding(5.dp))

        OutlinedTextField(value = userType.value, onValueChange ={
            userType.value = it
        },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { menuIsState.value = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }

            },
            label = { Text(text = "Kullanıcı Türü",color = Color.Gray)},
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
            ))

             Box(modifier = Modifier.wrapContentSize(Alignment.CenterEnd)) {
                 DropdownMenu(
                     expanded = menuIsState.value,
                     onDismissRequest = { menuIsState.value = false }) {
                     DropdownMenuItem(text = {
                         Text(
                             text = "Üye",
                             color = Color.Gray
                         )
                     },
                         onClick = {
                             userType.value = "Üye"
                             menuIsState.value = false
                         })
                     DropdownMenuItem(text = {
                         Text(
                             text = "Core Team Üyesi",
                             color = Color.Gray
                         )
                     },
                         onClick = {
                             userType.value = "Core Team Üyesi"
                             menuIsState.value = false
                         })
                     DropdownMenuItem(text = {
                         Text(
                             text = "Lider",
                             color = Color.Gray
                         )
                     },
                         onClick = {
                             userType.value = "Lider"
                             menuIsState.value = false
                         })

                 }
             }



            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedButton(onClick = {

                if (userEmail.value.isNotEmpty()  &&  password.value.isNotEmpty() && username.value.isNotEmpty() && accessPassword.value.isNotEmpty()){
                    if(userEmail.value.endsWith(".com")){
                        if (password.value == accessPassword.value){
                            if(password.value.length >= 6){
                                auth.fetchSignInMethodsForEmail(userEmail.value).addOnCompleteListener { task ->
                                    if (task.isSuccessful){

                                        auth.createUserWithEmailAndPassword(userEmail.value,password.value).addOnCompleteListener {createUser ->

                                            if (createUser.isSuccessful) {
                                                // Sign in success, update UI with the signed-in user's information
                                                val currentUser = auth.currentUser
                                                currentUser?.sendEmailVerification()?.addOnCompleteListener { verified ->
                                                    if (verified.isSuccessful){
                                                        Toast.makeText(context,"Doğruala E-postası Gönderildi",Toast.LENGTH_SHORT).show()
                                                    }else{
                                                        Toast.makeText(context,"E-Posta Gönderilirken Bir Hata Oluştu",Toast.LENGTH_SHORT).show()
                                                        println("sendEmailVerification:failure" + verified.exception)
                                                    }
                                                }
                                                val data = hashMapOf(
                                                    "username" to username.value,
                                                    "email" to userEmail.value,
                                                    "profilePhoto" to "",
                                                    "user_type" to userType.value,
                                                    "user_state" to userState.value
                                                )
                                                Toast.makeText(context,"Kayıt Olma Başarılı", Toast.LENGTH_SHORT).show()
                                                if (userType.value.isNotEmpty()){
                                                    db.collection("users").document(userEmail.value).set(data)
                                                    navController.navigate("login_screen")
                                                }else{
                                                    Toast.makeText(context,"Lütfen Kullanıcı Türünüzü Belirtin",Toast.LENGTH_SHORT).show()
                                                }

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("ErrorAuth", "signInWithEmail:failure", task.exception)
                                                Toast.makeText(
                                                    context,
                                                    task.exception.toString(),
                                                    Toast.LENGTH_SHORT,
                                                ).show()

                                            }
                                        }
                                    }else{
                                        Toast.makeText(context,"This E-Mail Adress Already Exist",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }else{
                                Toast.makeText(context,"Parolanız 6 Karakterden daha uzun olmalıdır",Toast.LENGTH_LONG).show()
                            }


                        }
                        else{
                            Toast.makeText(context,"Parolalar Aynı Olmalı",Toast.LENGTH_SHORT).show()
                        }


                    }
                    else{
                        Toast.makeText(context,"Lütfen E-maili doğru şekilde giriniz.",Toast.LENGTH_SHORT).show()
                    }
                    }
                else{
                    Toast.makeText(context,"Gerekli Alanları Doldurmak Zorundasınız", Toast.LENGTH_SHORT).show()
                }
            }, border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)) {
                Text(text = "Kayıt Ol", color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}