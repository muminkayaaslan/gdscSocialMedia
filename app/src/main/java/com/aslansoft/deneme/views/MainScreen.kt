package com.aslansoft.deneme.views


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Post(
    val username: String,
    val post: String,
    val profilePhoto: String,
    val postPhoto: String
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

    val postPhoto = remember {
        mutableStateOf("")
    }
    val userPP = remember {
        mutableStateOf("")
    }
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val myUserName = remember {
        mutableStateOf("")
    }
    LaunchedEffect(currentUser?.email) {
        currentUser?.email?.let { email ->
            db.collection("users").document(email).get().addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.data
                myUserName.value = data?.get("username") as? String ?: ""
            }.addOnFailureListener { exception ->
                println(exception.localizedMessage)
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
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
                    userPP.value = postData["profile photo"].toString()
                    postPhoto.value = postData["post_url"].toString()
                    postList.add(Post(username.value, post.value,userPP.value,postPhoto.value))
                    isLoading.value = false
                }
            }.addOnFailureListener{
                println(it.localizedMessage)
                Toast.makeText(context,it.localizedMessage,Toast.LENGTH_SHORT).show()
                postList.toList()
                isLoading.value = false
            }
        }


        val gradient = Brush.linearGradient(listOf(Color.Cyan, Color.Green))
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .background(gradient, RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp))
                .clip(
                    RoundedCornerShape(10.dp
                    )
                ),
                title = { Text(modifier = Modifier.
                padding(top = 12.dp),
                    text = "GDSC", fontFamily = googleSans)}
                , actions = {

                    //Badge(navController = navController, newMessageCount = 5)

                }

                ,colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.DarkGray,
                    actionIconContentColor = Color.Transparent,


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
                        val alertDialogState = remember {
                            mutableStateOf(false)
                        }
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp)
                                .clickable { alertDialogState.value = true }
                            ,
                            colors = CardDefaults.elevatedCardColors(
                                contentColor = Color.Gray,
                            ),
                            shape = RoundedCornerShape(24.dp)

                        ) {

                            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                                Box(modifier = Modifier
                                    .size(50.dp)
                                    .padding(5.dp)
                                    .border(BorderStroke(0.5.dp, Color.Gray), CircleShape)
                                    , contentAlignment = Alignment.TopStart){
                                    if (postData.profilePhoto.isNotEmpty()){
                                        val painter = rememberAsyncImagePainter(
                                            ImageRequest.Builder(LocalContext.current).data(data = postData.profilePhoto).apply(block = fun ImageRequest.Builder.() {
                                                crossfade(true)
                                                transformations(CircleCropTransformation())
                                            }).build()
                                        )
                                        Image(modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                            ,painter = painter, contentDescription = null)

                                    }else{
                                        if (isSystemInDarkTheme()){
                                            Icon(modifier = Modifier
                                                .fillMaxSize()
                                                ,imageVector = Icons.Filled.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                                        }else{
                                            Icon(modifier = Modifier
                                                .fillMaxSize()
                                                ,imageVector = Icons.Filled.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                                        }

                                    }
                                }
                                Spacer(modifier = Modifier.padding(horizontal = 0.9.dp))
                                if (isSystemInDarkTheme()){
                                    Text(modifier = Modifier.padding(start = 5.dp, top = 7.dp),text = postData.username, color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = googleSans )
                                }else{
                                    Text(modifier = Modifier.padding(start = 5.dp, top = 7.dp),text = postData.username, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = googleSans )

                                }
                            }


                            if (isSystemInDarkTheme()){
                                Text(modifier = Modifier.padding(start = 15.dp, top = 1.dp, bottom = 3.dp),text = postData.post,color = Color.White, fontSize = 17.sp, fontFamily = googleSans)
                            }else{
                                Text(modifier = Modifier.padding(start = 15.dp, top = 1.dp, bottom = 3.dp),text = postData.post,color = MaterialTheme.colorScheme.onPrimary, fontSize = 17.sp, fontFamily = googleSans)

                            }
                            if (postData.postPhoto.isNotEmpty()){
                                val painter =
                                    rememberAsyncImagePainter(model = postData.postPhoto)
                                Image(modifier = Modifier
                                    .size(300.dp)
                                    .padding(5.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .clip(RoundedCornerShape(18.dp)),
                                    painter = painter,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                            if (myUserName.value != postData.username){

                                if (alertDialogState.value){
                                    AlertDialog(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        onDismissRequest = {
                                            alertDialogState.value = false
                                        },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                if (myUserName.value != postData.username){
                                                    navController.navigate(

                                                        "userprofile/${postData.username}"
                                                    )
                                                }else{
                                                    navController.navigate("profile_screen")
                                                }

                                            },
                                                colors = ButtonDefaults.textButtonColors(
                                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                                )) {
                                                Text(text = "Profile Git")
                                            }
                                        },
                                        icon = {

                                            if (isSystemInDarkTheme()) {
                                                Icon(modifier = Modifier.size(50.dp),
                                                    imageVector = Icons.Filled.Person,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.secondary
                                                )
                                            } else {
                                                Icon(modifier = Modifier.size(50.dp),
                                                    imageVector = Icons.Filled.Person,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }

                                        },
                                        title = {
                                            if (isSystemInDarkTheme()) {
                                                Text(
                                                    text = "Bu Kullanıcı İle Ne Yapmak İstiyorsun?",
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                            } else {
                                                Text(
                                                    text = "Bu Kullanıcı İle Ne Yapmak İstiyorsun?",
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )

                                            }

                                        },

                                        )
                                }

                            }

                        }

                    }
                }
            }
            Column (verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally){
                MainBottomBar(navController = navController, context = context)
            }
        }








    }
    val activity = (LocalContext.current as? Activity)
    BackHandler(true) {
        activity?.finish()
    }
}

@Composable
fun MainBottomBar(navController: NavHostController?,context: Context) {
    val gradient = Brush.linearGradient(listOf(Color.Cyan, Color.Green))
    val cameraGranted = remember {
        mutableStateOf(false)
    }

    cameraGranted.value = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    val requestPermissionCameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraGranted.value = true

        } else {
            Toast.makeText(
                context,
                "Gönderi Paylaşmanız İçin Kameraya İzin Vermelisiniz ",
                Toast.LENGTH_LONG
            ).show()
        }

    }
    Box(modifier = Modifier.height(80.dp).background(Color.Transparent), contentAlignment = Alignment.TopCenter) {
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(60.dp)
                .background(gradient, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp)),
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        ) {

            //ana ekran butonu
            NavigationBarItem(
                selected = true,
                onClick = { navController?.navigate("main_screen") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null
                    )
                },
                colors = NavigationBarItemDefaults
                    .colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    )
            )

            //Profil ekranına yönlendiren buton
            NavigationBarItem(selected = false,
                onClick = {
                    navController?.navigate("profile_screen")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                }, colors = NavigationBarItemDefaults.run {
                    colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        disabledIconColor = MaterialTheme.colorScheme.onSecondary
                    )
                })
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(60.dp)
                .clip(CircleShape),
            onClick = {
                if (!cameraGranted.value) {
                    requestPermissionCameraLauncher.launch(Manifest.permission.CAMERA)
                } else {
                    navController?.navigate("camera")
                }
            },
            content = {
                Icon(
                    modifier = Modifier.size(60.dp), imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            },
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}
