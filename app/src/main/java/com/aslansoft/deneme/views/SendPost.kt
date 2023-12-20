package com.aslansoft.deneme.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun SendPostScreen(navController: NavHostController) {
    val post = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val auth = Firebase.auth
    val db = Firebase.firestore
    val currentUser = auth.currentUser
    val email = currentUser?.email
    val username = remember {
        mutableStateOf("")
    }
    val timestamp: Timestamp = Timestamp.now()
    db.collection("users").document(email!!).get().addOnSuccessListener {
        val data = it.data
        username.value =data?.get("username") as? String?: ""

    }.addOnFailureListener{
        println(it.message)
    }
    val postMap: HashMap<String,Any> = hashMapOf(
        "post" to post.value,
        "username" to username.value,
        "date" to timestamp
    )

    Surface(modifier = Modifier
        .fillMaxSize()
        , color = MaterialTheme.colorScheme.primary) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value = post.value, onValueChange = {
                    post.value = it

            }, placeholder = { Text(text = "Durum Paylaş...", color = Color.LightGray)}, maxLines = 2,singleLine = false, colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.secondary,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary
            ))
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedButton(onClick = {
                db.collection("posts").add(postMap).addOnCompleteListener{
                    Toast.makeText(context,"Gönderi başarı ile paylaşıldı",Toast.LENGTH_SHORT).show()
                    navController.navigate("main_screen")
                }.addOnFailureListener{
                    println(it.localizedMessage)
                    Toast.makeText(context,"Gönderi paylaşırken bir hata oluştu",Toast.LENGTH_LONG).show()
                }
            }) {
                Text(text = "Paylaş", color = MaterialTheme.colorScheme.secondary)
            }
        }

    }
}