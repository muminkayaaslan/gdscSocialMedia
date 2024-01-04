package com.aslansoft.deneme.views


import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Post(
    val username: String,
    val post: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = Firebase.firestore
    val post = remember { mutableStateOf("") }
    val username = remember{ mutableStateOf("") }
    val postList = remember { mutableStateListOf<Post>() }
    val isLoading = remember { mutableStateOf(false) }
    val alertDialogState = remember {
        mutableStateOf(false)
    }
    Surface(
        Modifier
            .fillMaxSize()
            ,color = MaterialTheme.colorScheme.primary) {

        LaunchedEffect( Unit){
            isLoading.value = true
            db.collection("posts").orderBy("date",Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
                for (document in documents){
                    val postData: Map<String,Any> = document.data
                    username.value = postData["username"].toString()
                    post.value = postData["post"].toString()
                    postList.add(Post(username.value, post.value))
                    isLoading.value = false
                }
            }.addOnFailureListener{
                println(it.localizedMessage)
                Toast.makeText(context,it.localizedMessage,Toast.LENGTH_SHORT).show()
                postList.toList()
                isLoading.value = false
            }
         }



        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp,
                        topStart = 10.dp,
                        topEnd = 10.dp
                    )
                ),
                title = { Text(modifier = Modifier.
                padding(top = 12.dp),
                    text = "Ana Sayfa", fontFamily = googleSans)}
                , actions = {

                    Badge(navController = navController, newMessageCount = 5)
                       
                    }

                ,colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary),
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary

                ))

                if (isLoading.value){
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()){
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.onPrimary)
                    }

                }else{
                    LazyColumn(modifier = Modifier.weight(1f)){
                        item { 
                            Spacer(modifier = Modifier.padding(vertical = 3.dp))
                        }
                        items(postList.size){index ->
                            val postData = postList[index]

                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp)
                                .background(
                                    MaterialTheme.colorScheme.onPrimary,
                                    RoundedCornerShape(10.dp)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                , verticalArrangement = Arrangement.Center) {
                                Row (modifier = Modifier.fillMaxWidth()){

                                    if (postData.username != username.value){
                                        if (alertDialogState.value){
                                            AlertDialog(onDismissRequest = { alertDialogState.value = false }){
                                                Button(modifier = Modifier.fillMaxWidth(),onClick = { navController.navigate("") }, colors = ButtonDefaults.buttonColors(
                                                    contentColor = Color.Red,
                                                    containerColor = MaterialTheme.colorScheme.onPrimary
                                                )) {

                                                }
                                            }
                                        }

                                    }
                                    Text(modifier = Modifier.padding(start = 15.dp , top = 10.dp),text = postData.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = googleSans )

                                }

                                Text(modifier = Modifier.padding(start = 15.dp, top = 1.dp, bottom = 3.dp),text = postData.post,color = Color.White, fontSize = 17.sp, fontFamily = googleSans)

                            }

                        }
                    }
                }
            Column (verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally){
                MainBottomBar(navController = navController)
            }
        }








    }
    val activity = (LocalContext.current as? Activity)
    BackHandler(true) {
        activity?.finish()
    }
 }

@Composable
fun MainBottomBar(navController: NavHostController?) {


    NavigationBar(modifier = Modifier
        .height(60.dp)
        .padding(start = 5.dp, end = 5.dp, bottom = 10.dp)
        .clip(
            RoundedCornerShape(
                topEnd = 20.dp,
                topStart = 20.dp,
                bottomEnd = 20.dp,
                bottomStart = 20.dp
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


        val googleBlue = Color(99, 154, 245, 255)
        if (isSystemInDarkTheme()){
            Button(modifier = Modifier
                .size(80.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White),
                onClick = { navController?.
                navigate("camera")
                }) {
                Icon(modifier = Modifier.size(100.dp),imageVector = Icons.Filled.Add,
                    contentDescription = null)
            }
        }else{
            Button(modifier = Modifier
                .size(80.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
                colors = ButtonDefaults.buttonColors(containerColor = googleBlue,
                    contentColor = Color.White),
                onClick = { navController?.
                navigate("camera")
                }) {
                Icon(modifier = Modifier.size(100.dp),imageVector = Icons.Filled.Add,
                    contentDescription = null)
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Badge(navController: NavHostController, newMessageCount: Int = 0) {
        Box(modifier = Modifier
            .size(40.dp)
            .clickable {
                navController
                    .navigate("massagelist_screen")
            }){
            Image(modifier = Modifier
                .size(40.dp)
                .padding(top = 10.dp)
                ,imageVector = Icons.Filled.Send, contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary))
            BadgedBox(modifier = Modifier
                .align(Alignment.TopEnd)
                .size(35.dp)
                .padding(top = 15.dp, end = 14.dp),badge = {
                Badge(modifier = Modifier.fillMaxSize(),contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) {
                    Text(text = newMessageCount.toString(), fontSize = 14.sp)
                }
            }) {}


        }

    }

