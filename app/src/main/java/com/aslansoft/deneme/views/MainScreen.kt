package com.aslansoft.deneme.views


import android.webkit.WebSettings.TextSize
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
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
            ,color = MaterialTheme.colorScheme.primary) {

        DisposableEffect( Unit){
            db.collection("posts").get().addOnSuccessListener {documents ->
                for (document in documents){
                    val postData: Map<String,Any> = document.data
                    username.value = postData["username"].toString()
                    post.value = postData["post"].toString()
                    postList.add(Post(username.value, post.value))

                }

            }.addOnFailureListener{
                println(it.localizedMessage)
                Toast.makeText(context,it.localizedMessage,Toast.LENGTH_SHORT).show()
                postList.toList()
            }
        onDispose {  }
         }



        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn{

                items(postList.size){index ->
                    val postData = postList[index]

                    Column(modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth(), verticalArrangement = Arrangement.Center) {

                        Text(modifier = Modifier.padding(start = 15.dp , top = 10.dp),text = postData.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp )

                        Text(modifier = Modifier.padding(start = 15.dp, top = 2.dp),text = postData.post,color = Color.White, fontSize = 17.sp)

                        Spacer(modifier = Modifier.padding(2.dp))
                        SimpleLineDivider()
                    }

                }
            }

        }

        Column (verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally){
            MainBottomBar(navController = navController)
        }






    }
 }

@Composable
fun MainBottomBar(navController: NavHostController?) {


    NavigationBar(modifier = Modifier
        .height(60.dp)
        .padding(start = 5.dp, end = 5.dp, bottom = 10.dp)
        .clip(
            RoundedCornerShape(
                topEnd = 30.dp,
                topStart = 30.dp,
                bottomEnd = 30.dp,
                bottomStart = 30.dp
            )
        ),
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.onTertiary
        ){


        //ana ekran butonu
        NavigationBarItem(selected = true,
            onClick = { navController?.navigate("main_screen") },
            icon = { Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = null) },
            colors = NavigationBarItemDefaults
                .colors(selectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.onPrimary)
        )


        Button(modifier = Modifier
            .size(80.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White),
            onClick = { navController?.
            navigate("post_screen")
            }) {
            Icon(modifier = Modifier.size(100.dp),imageVector = Icons.Filled.Add,
                contentDescription = null)
        }


        //Profil ekranına yönlendiren buton
        NavigationBarItem(selected = false ,
            onClick = { navController?.
        navigate("profile_screen") },
            icon = { Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null
        ) },colors = NavigationBarItemDefaults.run {
                colors(unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        disabledIconColor = MaterialTheme.colorScheme.onSecondary
                        )
            })
    }


}

@Composable
fun SimpleLineDivider() {
    if (isSystemInDarkTheme()){
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp), color = Color.Gray
        )
    }
    else{
        Divider(
            Modifier
                .fillMaxWidth()
                .height(0.5.dp), color = Color.White
        )
    }

}