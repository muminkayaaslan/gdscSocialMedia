package com.aslansoft.deneme

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aslansoft.deneme.ui.theme.DenemeTheme
import com.aslansoft.deneme.views.Badge
import com.aslansoft.deneme.views.Camera
import com.aslansoft.deneme.views.ChatScreen
import com.aslansoft.deneme.views.LoginScreen
import com.aslansoft.deneme.views.MainBottomBar
import com.aslansoft.deneme.views.MainScreen
import com.aslansoft.deneme.views.MessageList
import com.aslansoft.deneme.views.ProfileEditScreen
import com.aslansoft.deneme.views.ProfileScreen
import com.aslansoft.deneme.views.RegisterScreen
import com.aslansoft.deneme.views.SendPostScreen
import com.aslansoft.deneme.views.Settings
import com.aslansoft.deneme.views.UserProfile
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        setContent {
            DenemeTheme {
                //Status Bar
                val systemUiController = rememberSystemUiController()
                if(isSystemInDarkTheme()){
                    systemUiController.setSystemBarsColor(
                        color = MaterialTheme.colorScheme.primary
                    )
                }else{
                    systemUiController.setSystemBarsColor(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                //Start Destination
                val auth = FirebaseAuth.getInstance()
                val startDestination = remember {
                    mutableStateOf("")
                }
                if (auth.currentUser != null){
                    startDestination.value = "main_screen"
                }else{
                    startDestination.value = "login_screen"
                }
                //Navigation Fragment
                val navController  = rememberNavController()
                NavHost(navController = navController,
                    startDestination = startDestination.value,
                    enterTransition = { fadeIn(animationSpec = tween(0)) /*+ slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,tween(3000))*/},
                    exitTransition = { fadeOut(animationSpec = tween(0)) }){
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
                    composable("chat_screen/{username}", enterTransition = { fadeIn(animationSpec = tween(0)) /*+ slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,tween(3000))*/},
                        exitTransition = { fadeOut(animationSpec = tween(0)) }){ backStackEntry ->
                        ChatScreen(navController = navController,username = backStackEntry.arguments?.getString("username"))
                    }
                    composable("badge"){
                        Badge(navController = navController)
                    }
                    composable("camera"){
                        Camera(navController = navController)
                    }
                    composable("userprofile/{username}"){backStackEntry ->
                        UserProfile(navController = navController,username = backStackEntry.arguments?.getString("username"))
                    }
                }


            }
        }
    }
}





