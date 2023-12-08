@file:Suppress("DEPRECATION")

package com.aslansoft.deneme.views

import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    val auth = Firebase.auth


    val backgroundColor = Color(3, 3, 70, 255)
    Surface(Modifier.fillMaxSize(), color = backgroundColor) {
        val userEmail = remember {
            mutableStateOf("")
        }

        val password = remember {
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

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), contentAlignment = Alignment.Center){
                Image(bitmap = ImageBitmap.imageResource(R.drawable.iconbg), contentDescription = null )
            }
            Box(modifier = Modifier
                .height(50.dp)
                .zIndex(1f), contentAlignment = Alignment.Center){
                Text(text = "Welcome New Friend",color= Color.White, fontSize = 30.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(value = username.value , onValueChange ={
                username.value = it
            }, singleLine = true
                ,label = { Text(text = "Username")},
                colors= OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    disabledLabelColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White)
                )
                println(username.value)
            OutlinedTextField(value = userEmail.value, onValueChange = {
                userEmail.value = it
            }, label = { Text(text = "Email", color = Color.White) },
                placeholder = { Text(text = "username@example.com")},
                singleLine = true, colors = outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
            )
            )
            OutlinedTextField(value = password.value, onValueChange = {
                password.value = it
            }, label = { Text(text = "Password",color = Color.White) },
                singleLine = true, colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White
            ), visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
                /*trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {

                    }
                }*/

            )

            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedButton(onClick = {

                if (userEmail.value.isNotEmpty()&&password.value.isNotEmpty()){
                    if(userEmail.value.endsWith("@gmail.com")){
                        auth.fetchSignInMethodsForEmail(userEmail.value).addOnCompleteListener { task ->
                            if (task.isSuccessful){

                                auth.createUserWithEmailAndPassword(userEmail.value,password.value).addOnCompleteListener {

                                    if (it.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        println("başarılı")
                                        val data = hashMapOf(
                                            "username" to username.value,
                                            "email" to userEmail.value
                                        )
                                        Toast.makeText(context,"Kayıt Olma Başarılı", Toast.LENGTH_SHORT).show()
                                        db.collection("users").document(userEmail.value).set(data)
                                        navController.navigate("login_screen")
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

                    }
                    else{
                        Toast.makeText(context,"Lütfen E-maili doğru şekilde giriniz.",Toast.LENGTH_SHORT).show()
                    }
                    }

                else{
                    Toast.makeText(context,"Gerekli Alanları Doldurmak Zorundasınız", Toast.LENGTH_SHORT).show()
                }


            }) {
                Text(text = "SIGN UP", color = Color.White)
            }
        }

    }
}