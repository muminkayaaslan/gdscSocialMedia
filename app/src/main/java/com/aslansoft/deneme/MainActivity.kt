package com.aslansoft.deneme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aslansoft.deneme.ui.theme.DenemeTheme
import com.aslansoft.deneme.ui.theme.googleSans
import com.aslansoft.deneme.views.Camera
import com.aslansoft.deneme.views.LoginScreen
import com.aslansoft.deneme.views.MainBottomBar
import com.aslansoft.deneme.views.MainScreen
import com.aslansoft.deneme.views.ProfileEditScreen
import com.aslansoft.deneme.views.ProfileScreen
import com.aslansoft.deneme.views.RegisterScreen
import com.aslansoft.deneme.views.SendPostScreen
import com.aslansoft.deneme.views.Settings
import com.aslansoft.deneme.views.SplashScreen
import com.aslansoft.deneme.views.UserProfile
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
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

                //Navigation Fragment
                val context = LocalContext.current
                val navController  = rememberNavController()
                val currentDestinationText = remember { mutableStateOf("") }

                Box(modifier = Modifier.fillMaxSize()) {


                    Scaffold(
                        modifier = Modifier.background(Color.Transparent),
                        bottomBar = {

                            AnimatedVisibility(
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it }),
                                visible = currentDestinationText.value == "main_screen" || currentDestinationText.value == "profile_screen",
                            )  {
                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                                    MainBottomBar(navController = navController, context = context)
                                }
                            }
                        },

                        ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            NavHost(navController = navController,
                                startDestination = "splash_screen",
                                enterTransition = {EnterTransition.None},
                                exitTransition = {ExitTransition.None}){

                                composable("main_screen"){
                                    MainScreen(navController = navController)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("login_screen"){
                                    LoginScreen(navController = navController)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("register_screen"){
                                    RegisterScreen(navController = navController)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("post_screen/{uri}"){navBackStackEntry ->
                                    SendPostScreen(navController = navController,uri = navBackStackEntry.arguments?.getString("uri"))
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("main_bottom_bar"){
                                    MainBottomBar(navController = navController, context = LocalContext.current)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("profile_screen"){
                                    ProfileScreen(navController = navController)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }

                                composable("setting_screen"){
                                    Settings(navController = navController)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("profileEdit_screen"){
                                    ProfileEditScreen(navController = navController)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("camera"){
                                    Camera(navController = navController)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("userprofile/{username}"){backStackEntry ->
                                    UserProfile(navController = navController,username = backStackEntry.arguments?.getString("username"))
                                    currentDestinationText.value = navController.currentDestination?.route.toString()
                                }
                                composable("splash_screen"){
                                    SplashScreen(navController = navController)
                                    currentDestinationText.value = navController.currentDestination?.route.toString()

                                }


                            }


                        }
                    }
                    AnimatedVisibility(
                        visible = currentDestinationText.value == "main_screen" || currentDestinationText.value == "profile_screen",
                        enter = slideInVertically(initialOffsetY = { it }), // Slide in from bottom
                        exit = slideOutVertically(targetOffsetY = { it }),  // Slide out to bottom
                    ){
                        Column (modifier = Modifier.fillMaxSize().background(Color.Transparent), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(100.dp).offset(y=-(17.5).dp).background(Color.Transparent), contentAlignment = Alignment.BottomCenter) {
                                val cameraGranted = remember {
                                    mutableStateOf(false)
                                }

                                cameraGranted.value = ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
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
                                FloatingActionButton(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .border(BorderStroke(1.dp, Color.LightGray), CircleShape),
                                    onClick = {
                                        if (!cameraGranted.value){
                                            requestPermissionCameraLauncher.launch(Manifest.permission.CAMERA)
                                        }else{
                                            navController.navigate("camera")
                                        }
                                    },
                                    content = {
                                        Image(
                                            modifier = Modifier.size(35.dp),
                                            imageVector = ImageVector.vectorResource(R.drawable.photocameraa),
                                            contentDescription = null,
                                            colorFilter = ColorFilter.tint(Color(227,226,201,255))
                                        )
                                    },
                                    containerColor = Color.DarkGray,
                                    contentColor = Color.Cyan,
                                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                                )
                            }
                        }
                    }


                }

            }
        }
    }
}





