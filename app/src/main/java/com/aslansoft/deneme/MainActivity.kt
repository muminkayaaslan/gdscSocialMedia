package com.aslansoft.deneme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aslansoft.deneme.ui.theme.DenemeTheme
import com.aslansoft.deneme.views.LoginScreen
import com.aslansoft.deneme.views.MainBottomBar
import com.aslansoft.deneme.views.MainScreen
import com.aslansoft.deneme.views.MessageList
import com.aslansoft.deneme.views.ProfileEditScreen
import com.aslansoft.deneme.views.ProfileScreen
import com.aslansoft.deneme.views.RegisterScreen
import com.aslansoft.deneme.views.SendPostScreen
import com.aslansoft.deneme.views.Settings
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            if(isSystemInDarkTheme()){
                window.statusBarColor = getColor(R.color.background)
                window.navigationBarColor = getColor(R.color.background)
            }
            else{
                window.statusBarColor = getColor(R.color.lightbackground)
                window.navigationBarColor = getColor(R.color.lightbackground)
            }
            DenemeTheme {
                val db = Firebase.firestore
                val auth = FirebaseAuth.getInstance()
                val startDestination = remember {
                    mutableStateOf("")
                }
                if (auth.currentUser != null){
                    startDestination.value = "main_screen"
                }else{
                    startDestination.value = "login_screen"
                }
                val navController  = rememberNavController()
                NavHost(navController = navController , startDestination = startDestination.value){
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
                    composable("main_bottom_bar"){
                        MainBottomBar(navController = navController)
                    }
                    composable("profile_screen"){
                        ProfileScreen(navController = navController)
                    }
                    composable("massagelist_screen"){
                        MessageList(navController = navController)
                    }
                    composable("setting_screen"){
                        Settings(navController = navController)
                    }
                    composable("profileEdit_screen"){
                        ProfileEditScreen(navController = navController)
                    }
                }


            }
        }
    }
}




