@file:Suppress("NAME_SHADOWING")

package com.aslansoft.deneme.views

import android.annotation.SuppressLint
import android.webkit.WebSettings.TextSize
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MyPost(
    val post: String,
    val postPhoto: String,
    val date: Timestamp
)
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    Surface(
        Modifier
            .fillMaxSize(), color = MaterialTheme.colorScheme.primary
    ) {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        val username = remember { mutableStateOf("") }
        val sheetState = rememberModalBottomSheetState()
        val myPostList = remember { mutableStateListOf<MyPost>() }
        val isLoading = remember { mutableStateOf(false) }
        var bottomSheetIsOpen by remember { mutableStateOf(false) }
        val profilePhoto = remember { mutableStateOf("") }
        val context = LocalContext.current

        currentUser?.email?.let {

            db.collection("users").document(it).get().addOnSuccessListener { snapShot ->
                val data = snapShot.data
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
                        val postPhoto = postData["post_url"].toString()
                        val currentDate = postData["date"] as Timestamp?

                        if (currentDate != null) {
                            val myPost = MyPost(currentPost, postPhoto, currentDate)
                            myPostList.add(myPost)
                        }
                    }

                    isLoading.value = false
                }
                .addOnFailureListener {
                    println("Veri hatası:" + it.message)
                    isLoading.value = false
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    // Set to true in case of failure
                }
        }
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = profilePhoto.value)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }).build()
        )

        val gradient = Brush.linearGradient(listOf(MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.onPrimary))


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
                }, actions = {
                    Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.CenterEnd) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                bottomSheetIsOpen = true
                            }, tint = Color.Black
                        )
                    }
                    //Badge(navController = navController, newMessageCount = 5)

                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.DarkGray,
                    actionIconContentColor = Color.Transparent,


                    )
            )

            //kendi paylaştığın gönderiler
            Spacer(modifier = Modifier.padding(10.dp))
            /*
            if (myPostList.isEmpty() && !isLoading.value) {
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
            } else {*/
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        Spacer(modifier = Modifier.padding(30.dp))
                        Row(modifier = Modifier.height(150.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .border(BorderStroke(2.dp,gradient), CircleShape)
                            ) {
                                if (profilePhoto.value.isNotEmpty()) {
                                    Image(
                                        modifier = Modifier.fillMaxSize().animateContentSize() // Yeni resim yüklendiğinde bileşeni yeniden boyutlandır
                                            .alpha(
                                                animateFloatAsState(
                                                targetValue = if (profilePhoto.value.isNotEmpty()) 1f else 0f,
                                                animationSpec = TweenSpec(durationMillis = 300),
                                                label = "" // Fade süresi
                                            ).value),
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
                        }
                        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                            Divider( modifier = Modifier.width(50.dp),thickness = 1.dp, color = MaterialTheme.colorScheme.background)
                            Text(
                                text = username.value,
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = googleSans
                            )
                            Divider( modifier = Modifier.width(50.dp),thickness = 1.dp,color = MaterialTheme.colorScheme.background)
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    items(myPostList.size) { index ->
                        val postData = myPostList[index]
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
                            val timestamp =
                                postData.date.seconds + postData.date.nanoseconds / 1000000000
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

                                Text(
                                    modifier = Modifier.padding(start = 5.dp, top = 7.dp),
                                    text = username.value,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    fontFamily = googleSans
                                )

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

                            if (postData.postPhoto.isNotEmpty()) {
                                val painter =
                                    rememberAsyncImagePainter(model = postData.postPhoto)
                                Image(
                                    modifier = Modifier
                                        .size(300.dp)
                                        .padding(5.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .clip(RoundedCornerShape(18.dp)),
                                    painter = painter,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        // Alttan açılan Menü
        if (bottomSheetIsOpen) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { bottomSheetIsOpen = false },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clickable {
                            bottomSheetIsOpen = false
                            navController.navigate("setting_screen")
                        }) {

                        Spacer(modifier = Modifier.padding(10.dp))
                        Image(
                            modifier = Modifier.fillMaxHeight(),
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.background)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 5.dp),
                            text = "Ayarlar",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = googleSans
                        )
                        Spacer(modifier = Modifier.padding(1.dp))
                    }
                    Spacer(modifier = Modifier.padding(3.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clickable {
                            bottomSheetIsOpen = false
                            navController.navigate("profileEdit_screen")
                        }) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Image(
                            modifier = Modifier.fillMaxHeight(),
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.background
                            }
                            else{
                                Color.DarkGray
                            })
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 5.dp),
                            text = "Profili Düzenle",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = googleSans
                        )
                        Spacer(modifier = Modifier.padding(1.dp))
                    }
                    Spacer(modifier = Modifier.padding(vertical = 20.dp))
                    Spacer(modifier = Modifier.padding(3.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clickable {
                                bottomSheetIsOpen = false
                                Firebase.auth.signOut()
                                navController.navigate("login_screen")
                            }) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Image(
                            modifier = Modifier
                                .size(25.dp)
                                .fillMaxHeight(),
                            bitmap = ImageBitmap.imageResource(R.drawable.logout),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = Color.Red)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 5.dp),
                            text = "Çıkış Yap",
                            color = Color.Red,
                            fontFamily = googleSans
                        )
                        Spacer(modifier = Modifier.padding(1.dp))
                    }
                    Spacer(Modifier.padding(15.dp))
                }
            }
        }


    }
}
