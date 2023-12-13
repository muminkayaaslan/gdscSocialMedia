package com.aslansoft.deneme.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Post(
    val username: String,
    val post: String
)
@Composable
fun SeePost(navController: NavHostController) {
    val db = Firebase.firestore
    val post = remember {
        mutableStateOf("")
    }
    val username = remember{
        mutableStateOf("")
    }
    val postList = remember {
        mutableStateListOf<Post>()
    }
    Surface(
        Modifier
            .fillMaxSize()
            .background(Color(3, 3, 70))) {
        db.collection("posts").get().addOnSuccessListener {documents ->
            for (document in documents){
                val postData: Map<String,Any> = document.data
                username.value = postData["username"].toString()
                post.value = postData["post"].toString()
                postList.add(Post(username.value, post.value))
            }

        }
        LazyColumn{
            items(postList.size){index ->
                val postData = postList[index]
                Column(modifier = Modifier
                    .background(Color(3, 3, 70))
                    .fillMaxWidth(), verticalArrangement = Arrangement.Center) {
                    Text(text = postData.username, color = Color.White)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = postData.post,color = Color.White)
                    Divider(
                        modifier = Modifier.fillMaxWidth().height(1.dp), color = Color.Green
                    )
                }

            }
        }
    }


}