package com.aslansoft.deneme.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.checkerframework.checker.units.qual.UnitsRelations
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MyPost(
    val post: String,
    val date: Timestamp
        )
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    Surface(
        Modifier
        .fillMaxSize()
        ,color = MaterialTheme.colorScheme.primary
        ) {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        val username = remember { mutableStateOf("") }
        val sheetState = rememberModalBottomSheetState()
        var bottomSheetIsOpen by rememberSaveable { mutableStateOf(false) }
        val myPostList = remember { mutableStateListOf<MyPost>() }
        val post = remember { mutableStateOf("") }
        val time = remember { mutableStateOf<Timestamp?>(null) }
        val isLoading= remember { mutableStateOf(false) }



        currentUser?.email?.let {
            db.collection("users").document(it).get().addOnSuccessListener {
                val data = it.data
                username.value = data?.get("username") as? String ?: ""

            }
        }
        LaunchedEffect(username.value){
            isLoading.value = true
            println(username.value)
            db.collection("posts").
            whereEqualTo("username",username.value).
            orderBy("date",Query.Direction.DESCENDING).
            get().
            addOnSuccessListener { documents ->
                for (document in documents){
                    val postData: Map<String,Any> = document.data
                    val currentPost = postData["post"].toString()
                    val currentDate = postData["date"] as Timestamp?

                    if (currentDate != null) {
                        post.value = currentPost
                        time.value = currentDate

                        val myPost = MyPost(currentPost, currentDate)
                        myPostList.add(myPost)
                        isLoading.value = false
                    }
                    println(postData.toString())
                    }

                }.addOnFailureListener{
                println("Veri hatası:" + it.message)
            }
        }



        Column(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)), horizontalAlignment = Alignment.CenterHorizontally) {
            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp)),
                title = { Text(modifier = Modifier.
                padding(top = 12.dp),
                    text = "Profil")}
                , actions = {
                    Icon(modifier = Modifier
                        .size(40.dp)
                        .padding(top = 10.dp)
                        .clickable {
                            bottomSheetIsOpen = true
                        }
                        ,imageVector = Icons.Filled.Menu, contentDescription = null)
                },colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary),
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary

                ))
            Spacer(modifier = Modifier.padding(30.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.CenterHorizontally)){
                Image(modifier = Modifier.fillMaxSize(),imageVector = Icons.Filled.AccountCircle, contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary))
            }

            Text(text = username.value , fontStyle = FontStyle.Italic, fontSize = 45.sp,color = MaterialTheme.colorScheme.secondary)
            //kendi paylaştığın gönderiler
            Spacer(modifier = Modifier.padding(30.dp))
            if (isLoading.value){
                Box(modifier = Modifier.weight(1f).fillMaxWidth()){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            else{
                LazyColumn(modifier = Modifier.weight(1f)){
                    items(myPostList.size){index ->
                        val postData = myPostList[index]
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(bottom = 3.dp)
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                RoundedCornerShape(10.dp)
                            )
                            .clip(
                                RoundedCornerShape(10.dp)
                            ), verticalArrangement = Arrangement.Center) {
                            val timestamp = postData.date.seconds + postData.date.nanoseconds / 1000000000
                            val dateData = Date(timestamp * 1000L)
                            val format = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
                            val formattedDate = format.format(dateData)
                            Text(modifier = Modifier.padding(start = 10.dp, bottom = 2.dp),text = username.value, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                            Row (modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, bottom = 3.dp)){
                                Text(text = postData.post, color = MaterialTheme.colorScheme.secondary)
                                Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.End) {
                                    Text(modifier = Modifier.padding(top = 6.dp, end = 7.dp), text = formattedDate, color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp )
                                }
                            }
                        }

                    }
                }
            }




        }
        // Alttan açılan Menü
        if (bottomSheetIsOpen){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ){
                ModalBottomSheet(sheetState = sheetState,
                    onDismissRequest = { bottomSheetIsOpen = false },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clickable {
                            navController.navigate("setting_screen")
                        }){
                        Spacer(modifier = Modifier.padding(10.dp))
                        Image(modifier = Modifier.fillMaxHeight(),imageVector = Icons.Filled.Settings, contentDescription = null , colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary))
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(modifier = Modifier.fillMaxHeight(),text = "Ayarlar", color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.padding(1.dp))
                    }
                    Spacer(modifier = Modifier.padding(3.dp))
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clickable {
                            bottomSheetIsOpen = false
                            navController.navigate("profileEdit_screen")
                        }){
                        Spacer(modifier = Modifier.padding(10.dp))
                        Image(modifier =  Modifier.fillMaxHeight(),imageVector = Icons.Filled.AccountCircle, contentDescription = null , colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary))
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(modifier = Modifier.fillMaxHeight(),text = "Profili Düzenle", color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.padding(1.dp))
                    }
                    Spacer(modifier = Modifier.padding(vertical =  20.dp))
                    Spacer(modifier = Modifier.padding(3.dp))
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clickable {
                                bottomSheetIsOpen = false
                                Firebase.auth.signOut()
                                navController.navigate("input_screen")
                            }){
                        Spacer(modifier = Modifier.padding(10.dp))
                        Image(modifier = Modifier
                            .size(25.dp)
                            .fillMaxHeight(),
                            bitmap = ImageBitmap.imageResource(R.drawable.logout),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = Color.Red)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(modifier = Modifier.fillMaxHeight(),text = "Çıkış Yap", color = Color.Red)
                        Spacer(modifier = Modifier.padding(1.dp))
                    }
                    Spacer(Modifier.padding(15.dp))
                }
            }
        }


        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom , horizontalAlignment = Alignment.CenterHorizontally) {
            ProfileBottomBar(navController = navController)
        }
    }
}

@Composable
fun ProfileBottomBar(navController: NavHostController?) {


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
        contentColor = Color.Transparent
    ){


        //ana ekran butonu
        NavigationBarItem(selected = false,
            onClick = { navController?.navigate("main_screen") },
            icon = { Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null) },
            colors = NavigationBarItemDefaults
                .colors(selectedIconColor = Color.White,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = Color.Transparent)
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
        NavigationBarItem(selected = true ,
            onClick = { navController?.
            navigate("profile_screen") },
            icon = { Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null
            ) },colors = NavigationBarItemDefaults
                .colors(selectedIconColor = Color.White,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.onPrimary))
    }


}

