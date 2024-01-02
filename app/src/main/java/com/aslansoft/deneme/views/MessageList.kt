package com.aslansoft.deneme.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageList(navController: NavHostController) {

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
            LazyColumn(modifier = Modifier.weight(1f)){
                item {
                    Spacer(modifier = Modifier.padding(vertical = 3.dp))
                }
                items(4){
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable {
                                navController.navigate("chat_screen/${"Denizhan"}")
                            }
                            .clip(RoundedCornerShape(10.dp)),){
                        Box(modifier = Modifier.size(40.dp)){
                            Image(modifier = Modifier.clip(CircleShape),bitmap = ImageBitmap.imageResource(R.drawable.numan) , contentScale = ContentScale.Crop, contentDescription = "numan")
                        }
                        if (isSystemInDarkTheme()){
                            Column {
                                Text(modifier = Modifier.padding(start = 3.dp,top = 2.dp),text = "Numan",color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp, fontFamily = googleSans, fontWeight = FontWeight.Bold)
                                Text(modifier = Modifier.padding(start = 3.dp, bottom = 2.dp), text = "I'm TransIt", color = MaterialTheme.colorScheme.secondary)
                            }
                        }else{
                            Column {
                                Text(modifier = Modifier.padding(start = 3.dp,top = 2.dp),text = "Numan",color = MaterialTheme.colorScheme.onPrimary, fontSize = 10.sp, fontFamily = googleSans, fontWeight = FontWeight.Bold)
                                Text(modifier = Modifier.padding(start = 3.dp, bottom = 2.dp), text = "I'm TransIt", color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }


                    }
                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                    SimpleLineDivider()
                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                }
            }
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