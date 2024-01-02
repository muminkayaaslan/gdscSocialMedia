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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.Timestamp

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
data class Conversation(
    val participants: List<String> = listOf(),
    val lastMessage: Message = Message(),

    )
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController, username: String?) {
    val message = remember {
        mutableStateOf("")
    }
    Surface( modifier = Modifier.fillMaxSize() ,color = MaterialTheme.colorScheme.primary) {
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
        Box(modifier = Modifier.fillMaxWidth()){
            OutlinedTextField(value = message.value, onValueChange ={
                message.value
            } )
        }
    }
    BackHandler(true) {
        navController.navigateUp()
    }
}