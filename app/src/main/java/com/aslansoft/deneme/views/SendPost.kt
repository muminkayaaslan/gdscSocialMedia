package com.aslansoft.deneme.views


import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.activity.result.contract.ActivityResultContracts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendPostScreen(navController: NavHostController) {
    val post = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val auth = Firebase.auth
    val db = Firebase.firestore
    val currentUser = auth.currentUser
    val email = currentUser?.email
    val username = remember {
        mutableStateOf("")
    }
    val timestamp: Timestamp = Timestamp.now()
    db.collection("users").document(email!!).get().addOnSuccessListener {
        val data = it.data
        username.value =data?.get("username") as? String?: ""

    }.addOnFailureListener{
        println(it.message)
    }
    val postMap: HashMap<String,Any> = hashMapOf(
        "post" to post.value,
        "username" to username.value,
        "date" to timestamp
    )
    //Camera Permission Start
    val cameraGranted = remember {
        mutableStateOf(false)
    }
    val alertDialogState = remember {
        mutableStateOf(false)
    }
    cameraGranted.value = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    if (!cameraGranted.value){
        alertDialogState.value = true
    }
    val requestPermissionCameraLauncher = rememberLauncherForActivityResult(
         ActivityResultContracts.RequestPermission()){ isGranted ->
        if (isGranted){
            cameraGranted.value = true

        }else{
            Toast.makeText(context,"Gönderi Paylaşmanız İçin Kameraya İzin Vermelisiniz ",Toast.LENGTH_LONG).show()
        }

    }


    //Camera Permission End

    Surface(modifier = Modifier
        .fillMaxSize()
        , color = MaterialTheme.colorScheme.primary) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                val containerColor = if (isSystemInDarkTheme()){
                    Color(49, 49, 49, 255)
                } else {
                    Color.LightGray
                }
            if (alertDialogState.value == true){
                AlertDialog(containerColor = containerColor,
                    onDismissRequest = { alertDialogState.value = false },
                    text = {
                        if (isSystemInDarkTheme()){
                            Text(text = "Kamerayı Kullanmak ve Gönderi Paylaşabilmek İçin Erişim İzni Vermeniz Gerekmektedir.",
                                color = MaterialTheme.colorScheme.secondary)
                        }else{
                            Text(text = "Kamerayı Kullanmak ve Gönderi Paylaşabilmek İçin Erişim İzni Vermeniz Gerekmektedir.",
                                color = MaterialTheme.colorScheme.onPrimary)
                        }

                    },
                    confirmButton = {

                        if (isSystemInDarkTheme()){
                            TextButton(onClick = {
                                requestPermissionCameraLauncher.launch(android.Manifest.permission.CAMERA)
                            alertDialogState.value = false
                            }) {
                                Text(text = "İzin Ver",
                                    color = MaterialTheme.colorScheme.secondary)
                            }
                        }else{
                            TextButton(onClick = { android.Manifest.permission.CAMERA
                                alertDialogState.value = false}) {
                                Text(text = "İzin Ver",
                                    color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    },
                    dismissButton = {
                        if (isSystemInDarkTheme()){
                            TextButton(onClick = { cameraGranted.value = false
                            alertDialogState.value = false }) {
                                Text(text = "İzin Verme",
                                    color = MaterialTheme.colorScheme.secondary)
                            }
                        }else{
                            TextButton(onClick = { cameraGranted.value = false
                                alertDialogState.value = false}) {
                                Text(text = "İzin Verme",
                                    color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                    , icon = {
                        if (isSystemInDarkTheme()){
                            Image(modifier = Modifier.size(20.dp),bitmap = ImageBitmap.imageResource(R.drawable.camera),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary) )

                        }else{
                            Image(modifier = Modifier.size(20.dp),bitmap = ImageBitmap.imageResource(R.drawable.camera),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary) )

                        }
                    })
            }else{
                if (cameraGranted.value == false) alertDialogState.value = true else alertDialogState.value = false

                OutlinedTextField(value = post.value,
                    onValueChange = {
                        post.value = it
                    }
                    ,placeholder = { Text(text = "Durum Paylaş...",
                        color = Color.LightGray,
                        fontFamily = googleSans)},
                    maxLines = 5,
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary
                    ))
                Spacer(modifier = Modifier.padding(10.dp))
                OutlinedButton(onClick = {
                    db.collection("posts")
                        .add(postMap)
                        .addOnCompleteListener{
                            Toast.makeText(context,
                                "Gönderi başarı ile paylaşıldı",
                                Toast.LENGTH_SHORT).show()
                            navController.navigate("main_screen")
                        }.addOnFailureListener{
                            println(it.localizedMessage)
                            Toast.makeText(context,
                                "Gönderi paylaşırken bir hata oluştu",
                                Toast.LENGTH_LONG).show()
                        }
                }, border = BorderStroke(1.dp,MaterialTheme.colorScheme.onPrimary)) {
                    Text(text = "Paylaş",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = googleSans)
                }
            }
        }

    }
}