package com.aslansoft.deneme.views

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageList(navController: NavHostController) {
    val db = Firebase.firestore
    val myUsername = remember {
        mutableStateOf("")
    }
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val email = currentUser?.email





    //DATABASE
    db.collection("users").document(email!!).get().addOnSuccessListener {
        val data = it.data
        myUsername.value = data?.get("username") as? String ?: ""
    }
    db.collection("conversation").get()
    val documentId = listOf(myUsername.value,)
    db.collection("conversation").document()




    //USERINTERFACE
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp)),
                title = {
                    Row {
                        Icon(modifier = Modifier
                            .size(40.dp)
                            .padding(top = 10.dp)
                            ,imageVector = Icons.Filled.Email, contentDescription = null)
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        Text(modifier = Modifier.
                        padding(top = 12.dp),
                            text = "Mesajlar", fontFamily = googleSans
                        )
                    }
                }
                ,colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary),
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary

                ))
            Spacer(modifier = Modifier.padding(vertical = 3.dp))


        }
    }
}

@Composable
fun SimpleLineDivider() {
    if (isSystemInDarkTheme()){
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = MaterialTheme.colorScheme.secondary)
    }else
    {
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = MaterialTheme.colorScheme.onPrimary)
    }

}