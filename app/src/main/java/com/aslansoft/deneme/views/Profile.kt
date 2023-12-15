package com.aslansoft.deneme.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(navController: NavHostController) {
    Surface(
        Modifier
        .fillMaxSize()
        ,color = MaterialTheme.colorScheme.primary) {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        val email = currentUser?.email

        db.collection("users").document()
        Column {

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
                topEnd = 30.dp,
                topStart = 30.dp,
                bottomEnd = 30.dp,
                bottomStart = 30.dp
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
