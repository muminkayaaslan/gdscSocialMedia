@file:Suppress("NAME_SHADOWING")

package com.aslansoft.deneme.views


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SendPostScreen(navController: NavHostController, uri: String?) {
    val post = remember {
        mutableStateOf("")
    }
    val storage = Firebase.storage
    val context = LocalContext.current
    val auth = Firebase.auth
    val db = Firebase.firestore
    val currentUser = auth.currentUser
    val email = currentUser?.email
    val username = remember {
        mutableStateOf("")
    }
    val photoUrl = remember {
        mutableStateOf("")
    }
    val profilePhoto = remember { mutableStateOf("") }
    val timestamp: Timestamp = Timestamp.now()
    db.collection("users").document(email!!).get().addOnSuccessListener {
        val data = it.data
        username.value =data?.get("username") as? String ?: ""
        profilePhoto.value = data?.get("profilePhoto") as? String ?: ""
    }.addOnFailureListener{
        println(it.message)
    }







    Surface(modifier = Modifier
        .fillMaxSize()
        , color = MaterialTheme.colorScheme.primary) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if (uri != null){
                    val painter = rememberAsyncImagePainter(model = uri.toUri())
                    Image(modifier = Modifier.size(300.dp), contentScale = ContentScale.Crop,painter = painter , contentDescription = null )
                    val timestamp = System.currentTimeMillis()
                    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    val formattedDate = dateFormat.format(timestamp)
                    val fileName = "${username.value}_${formattedDate}"
                    val storageRef = storage.reference.child("posts/${username.value}/$fileName")
                    if(username.value.isNotEmpty()){
                        storageRef.putFile(uri.toUri())
                            .addOnSuccessListener {taskSnapShots ->
                                storageRef.downloadUrl.addOnSuccessListener {imageUrl ->
                                    photoUrl.value = imageUrl.toString()

                                }

                            }.addOnFailureListener {
                                println(it.message)
                            }
                    }







                    }else{
                        println("photouri" + uri)
                    }
            Spacer(modifier = Modifier.padding(vertical = 3.dp))

            OutlinedTextField(value = post.value,
                onValueChange = {
                    post.value = it
                }
                ,placeholder = { Text(text = "Durum Paylaş...",
                    color = Color.LightGray,
                    fontFamily = googleSans)},
                maxLines = 5,
                singleLine = false,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary
                ))
            Spacer(modifier = Modifier.padding(10.dp))
            val posted = post.value.trim()
            val postMap: HashMap<String,Any> = hashMapOf(
                "post" to posted,
                "username" to username.value,
                "date" to timestamp,
                "profile photo" to profilePhoto.value,
                "post_url" to photoUrl.value
            )



            OutlinedButton(onClick = {
                if (posted.isNotEmpty()){
                    if (photoUrl.value.isNotEmpty()){

                        if (username.value.isNotEmpty()){

                            db.collection("posts")
                                .add(postMap)
                                .addOnCompleteListener{
                                    Toast.makeText(context,
                                        "Gönderi başarı ile paylaşıldı",
                                        Toast.LENGTH_SHORT).show()
                                    navController.navigate("main_screen")
                                }.addOnFailureListener{
                                    println(it.localizedMessage)
                                    Toast.makeText(context,
                                        "Gönderi paylaşırken bir hata oluştu",
                                        Toast.LENGTH_LONG).show()
                                }
                        }else{
                            println("hata: username")
                        }

                    }else{
                        println("hata:photoUrl")
                    }
                }else{
                    Toast.makeText(context,"Lütfen Durum Yazın",Toast.LENGTH_SHORT).show()
                }


            }, border = BorderStroke(1.dp,MaterialTheme.colorScheme.onPrimary)) {
                Text(text = "Paylaş",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = googleSans)
            }
                }
            //Fotoğrafı Storage'a gönderme


        }

    }
