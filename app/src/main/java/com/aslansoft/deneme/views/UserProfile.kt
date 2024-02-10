package com.aslansoft.deneme.views

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserPost(
    val post: String,
    val postPhoto: String,
    val date: Timestamp,
    val profilePhoto: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(navController: NavHostController, username: String?) {
    Surface(
        Modifier
            .fillMaxSize()
        ,color = MaterialTheme.colorScheme.primary
    ) {
        val db = Firebase.firestore
        val userPostList = remember { mutableStateListOf<UserPost>() }
        val isLoading= remember { mutableStateOf(false) }
        val profilePhoto = remember { mutableStateOf("") }
        val context = LocalContext.current
        val userType = remember {
            mutableStateOf("")
        }

        LaunchedEffect(username) {
            isLoading.value = true
            db.collection("posts")
                .whereEqualTo("username", username)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    userPostList.clear() // Clear the list before adding new data
                    for (document in documents) {
                        val postData: Map<String, Any> = document.data
                        val currentPost = postData["post"].toString()
                        val currentDate = postData["date"] as Timestamp?
                        val postPhoto = postData["post_url"].toString()
                        val profilePhotom = postData["profile photo"].toString()
                        if (currentDate != null) {
                            val userPost = UserPost(currentPost, postPhoto ,currentDate,profilePhotom)
                            userPostList.add(userPost)
                        }
                        // Update profilePhoto outside the loop
                        if (documents.isEmpty) {
                            println("dünya boştır lo")
                        }else{
                            profilePhoto.value = documents.first().data["profile photo"].toString()
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

        val gradient = Brush.linearGradient(listOf(MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.onPrimary))
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = profilePhoto.value).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                transformations(CircleCropTransformation())
            }).build()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .background(gradient, RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp))
                .clip(
                    RoundedCornerShape(
                        10.dp
                    )
                ),
                title = {
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "Profil", fontFamily = googleSans
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.DarkGray,
                    actionIconContentColor = Color.Transparent,))

            Spacer(modifier = Modifier.padding(30.dp))
            Row(modifier = Modifier.height(150.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .border(BorderStroke(2.dp,gradient), CircleShape)
                ) {
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
                            colorFilter = ColorFilter.tint(
                                if (isSystemInDarkTheme()) {
                                    MaterialTheme.colorScheme.background
                                }
                                else{
                                    Color.DarkGray
                                }
                            )
                        )
                    }
                }
            }


            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                Divider( modifier = Modifier.width(50.dp),thickness = 1.dp, color = MaterialTheme.colorScheme.background)
                username?.let {
                    Text(
                        text = it,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = googleSans
                    )
                }
                Divider( modifier = Modifier.width(50.dp),thickness = 1.dp,color = MaterialTheme.colorScheme.background)
            }
            Spacer(modifier = Modifier.padding(30.dp))

            //kendi paylaştığın gönderiler
            if (userPostList.isEmpty() && !isLoading.value){
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
                    items(userPostList.size){index ->
                        val postData = userPostList[index]
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 3.dp, end = 3.dp, start = 3.dp),
                            colors = CardDefaults.elevatedCardColors(
                                contentColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            val timestamp = postData.date.seconds + postData.date.nanoseconds / 1000000000
                            val dateData = Date(timestamp * 1000L)
                            val format = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
                            val formattedDate = format.format(dateData)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(5.dp)
                                        .border(BorderStroke(0.5.dp, Color.Gray), CircleShape),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    if (profilePhoto.value.isNotEmpty()) {
                                        val painter = rememberAsyncImagePainter(
                                            ImageRequest.Builder(LocalContext.current)
                                                .data(data = profilePhoto.value)
                                                .apply(block = fun ImageRequest.Builder.() {
                                                    crossfade(true)
                                                    transformations(CircleCropTransformation())
                                                }).build()
                                        )
                                        Image(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            painter = painter,
                                            contentDescription = null
                                        )

                                    } else {
                                        Icon(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            imageVector = Icons.Filled.AccountCircle,
                                            contentDescription = null,
                                            tint = if (isSystemInDarkTheme()) {
                                                MaterialTheme.colorScheme.background
                                            }
                                            else{
                                                Color.DarkGray
                                            }
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.padding(horizontal = 0.9.dp))
                                username?.let {
                                    Text(
                                        modifier = Modifier.padding(start = 5.dp, top = 7.dp),
                                        text = it,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        fontFamily = googleSans
                                    )
                                }


                            }

                            Text(
                                modifier = Modifier.padding(
                                    start = 15.dp,
                                    top = 1.dp,
                                    bottom = 3.dp
                                ),
                                text = postData.post,
                                color = MaterialTheme.colorScheme.background,
                                fontSize = 17.sp,
                                fontFamily = googleSans
                            )


                            Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.End) {

                                if (postData.postPhoto.isNotEmpty()){
                                    val painter = rememberAsyncImagePainter(model = postData.postPhoto)
                                    Image(modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .size(300.dp)
                                        .align(Alignment.CenterHorizontally),
                                        painter = painter,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null
                                    )
                                    if (isSystemInDarkTheme()){
                                        Text(modifier = Modifier.padding(top = 6.dp, end = 7.dp), text = formattedDate, color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp , fontFamily = googleSans)
                                    }else{
                                        Text(modifier = Modifier.padding(top = 6.dp, end = 7.dp), text = formattedDate, color = MaterialTheme.colorScheme.onPrimary, fontSize = 10.sp , fontFamily = googleSans)

                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}