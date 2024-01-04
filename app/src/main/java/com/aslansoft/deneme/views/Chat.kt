package com.aslansoft.deneme.views

import android.content.BroadcastReceiver
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

data class Message(
        val senderId: String?,
        val receiverId: String?,
        val date: Timestamp,
        val message: String?,
        val isRead: Boolean = false
        )
data class ChatUser(
    val username: String = "",
    val profile_photo: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController, username: String?) {
    val myMessage = remember {
        mutableStateOf("")
    }
    val profile_photo = remember {
        mutableStateOf("")
    }
    val message = remember {
        mutableStateOf("")
    }
    val date = remember {
        mutableStateOf<Timestamp>(Timestamp.now())
    }
    val senderId = remember {
        mutableStateOf("")
    }
    val receiverId = remember {
        mutableStateOf("")
    }
    val myUsername = remember {
        mutableStateOf("")
    }
    val myProfilePhoto = remember {
        mutableStateOf("")
    }
    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    currentUser?.email?.let { db.collection("users").document(it).get().addOnSuccessListener { document ->
        val data = document.data
        myUsername.value = data?.get("username") as? String ?: ""
    } }
    val users = listOf(myUsername.value,username.toString()).sorted()
    if (myUsername.value.isNotEmpty()){
    val conversationId = "${users[0]}_${users[1]}"
    Surface( modifier = Modifier.fillMaxSize() ,color = MaterialTheme.colorScheme.primary) {
        println(conversationId)
        Column (modifier = Modifier.fillMaxSize()){
            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp)),
                title = {
                    Row {
                        Box(modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterVertically)){
                            Image(modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),bitmap = ImageBitmap.imageResource(R.drawable.numan) , contentDescription = null, contentScale = ContentScale.Crop  )

                        }
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        Text(modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxHeight(),
                            text = username.toString(), fontFamily = googleSans, color = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                navigationIcon = {
                    Icon(modifier = Modifier
                        .fillMaxHeight()
                        .width(30.dp)
                        .clickable {
                            navController.navigateUp()
                        },imageVector = Icons.Filled.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.secondary)
                }
                ,colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary),
                ))
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter){
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth(),
                value = myMessage.value,
                onValueChange ={
                myMessage.value = it
            }, trailingIcon ={
                            Icon(modifier = Modifier.clickable {
                                          db.collection("conversations").document()
                            },imageVector = Icons.Filled.Send,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary)
            },
                placeholder = {
                              Text(text = "Mesaj Yaz...")
                },
                colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                selectionColors = TextSelectionColors(handleColor = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = MaterialTheme.colorScheme.primary)
            ),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
        }
    }
    BackHandler(true) {
        navController.navigateUp()
    }
}
}