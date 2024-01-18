package com.aslansoft.deneme.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(navController: NavHostController, username: String?) {
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
        val profilePhoto = remember { mutableStateOf("") }
        val context = LocalContext.current

        currentUser?.email?.let {

            db.collection("users").document(it).get().addOnSuccessListener {
                val data = it.data
                username.value = data?.get("username") as? String ?: ""
                profilePhoto.value = data?.get("profilePhoto") as? String ?: ""
            }
        }
        LaunchedEffect(username.value) {
            isLoading.value = true
            db.collection("posts")
                .whereEqualTo("username", username.value)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    myPostList.clear() // Clear the list before adding new data
                    for (document in documents) {
                        val postData: Map<String, Any> = document.data
                        val currentPost = postData["post"].toString()
                        val currentDate = postData["date"] as Timestamp?
                        val postPhoto = postData["post_url"].toString()
                        if (currentDate != null) {
                            val myPost = MyPost(currentPost, postPhoto ,currentDate)
                            myPostList.add(myPost)
                        }
                    }

                    isLoading.value = false
                }
                .addOnFailureListener {
                    println("Veri hatası:" + it.message)
                    isLoading.value = false
                    Toast.makeText(context,it.message, Toast.LENGTH_LONG).show()
                    // Set to true in case of failure
                }
        }
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = profilePhoto.value).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                transformations(CircleCropTransformation())
            }).build()
        )


        Column(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)), horizontalAlignment = Alignment.CenterHorizontally) {
            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp)),
                title = { Text(modifier = Modifier.
                padding(top = 12.dp),
                    text = "Profil", fontFamily = googleSans
                )
                }
                ,colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary),
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary

                ))
            Spacer(modifier = Modifier.padding(30.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.CenterHorizontally)){
                if (profilePhoto.value.isNotEmpty()) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painter,
                        contentDescription = null
                    )
                } else {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }

            Text(text = username.value , fontStyle = FontStyle.Italic, fontSize = 45.sp,color = MaterialTheme.colorScheme.onPrimary, fontFamily = googleSans)
            //kendi paylaştığın gönderiler
            Spacer(modifier = Modifier.padding(30.dp))
            if (myPostList.isEmpty() && !isLoading.value){
                if (isLoading.value) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Henüz Gönderi Yok",
                            color = MaterialTheme.colorScheme.onPrimary, fontFamily = googleSans
                        )
                    }
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
                            Row (modifier = Modifier.fillMaxWidth()){
                                if (profilePhoto.value.isNotEmpty()){
                                    Image(modifier = Modifier
                                        .size(25.dp)
                                        .padding(start = 3.dp, top = 3.dp),painter = painter, contentDescription = null)
                                }
                                else {
                                    Image(modifier = Modifier
                                        .size(25.dp)
                                        .padding(start = 3.dp, top = 3.dp, bottom = 2.dp),imageVector = Icons.Filled.AccountCircle , contentDescription = null, colorFilter = ColorFilter.tint(
                                        MaterialTheme.colorScheme.primary))
                                }

                                Text(modifier = Modifier.padding(start = 3.dp, bottom = 2.dp, top = 2.dp),text = username.value, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontFamily = googleSans)

                            }
                            Row (modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, bottom = 3.dp)){
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(text = postData.post, color = MaterialTheme.colorScheme.secondary, fontFamily = googleSans)

                                }

                                Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.End) {

                                    Text(modifier = Modifier.padding(top = 6.dp, end = 7.dp), text = formattedDate, color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp , fontFamily = googleSans)
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}