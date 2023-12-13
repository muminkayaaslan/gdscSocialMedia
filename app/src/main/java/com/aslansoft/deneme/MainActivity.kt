package com.aslansoft.deneme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aslansoft.deneme.ui.theme.DenemeTheme
import com.aslansoft.deneme.views.InputScreen
import com.aslansoft.deneme.views.LoginScreen
import com.aslansoft.deneme.views.MainScreen
import com.aslansoft.deneme.views.RegisterScreen
import com.aslansoft.deneme.views.SeePost
import com.aslansoft.deneme.views.SendPostScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            window.statusBarColor = getColor(R.color.background)
            window.navigationBarColor = getColor(R.color.background)
            DenemeTheme {
                val db = Firebase.firestore
                val auth = FirebaseAuth.getInstance()
                val startDestination = remember {
                    mutableStateOf("")
                }
                if (auth.currentUser != null){
                    startDestination.value = "main_screen"
                }else{
                    startDestination.value = "input_screen"
                }
                val navController  = rememberNavController()
                NavHost(navController = navController , startDestination = startDestination.value){
                    composable("input_screen"){
                            InputScreen(navController = navController)
                    }
                    composable("main_screen"){
                            MainScreen(navController = navController)
                    }
                    composable("login_screen"){
                        LoginScreen(navController = navController)
                    }
                    composable("register_screen"){
                        RegisterScreen(navController = navController)
                    }
                    composable("post_screen"){
                        SendPostScreen(navController = navController)
                    }
                    composable("see_post_screen"){
                        SeePost(navController = navController)
                    }
                }

            }
        }
    }
}




