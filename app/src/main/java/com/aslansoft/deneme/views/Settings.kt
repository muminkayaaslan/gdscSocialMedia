package com.aslansoft.deneme.views

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavHostController) {

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp,
                        topStart = 10.dp,
                        topEnd = 10.dp
                    )
                ),
                title = { Text(modifier = Modifier.
                padding(top = 12.dp),
                    text = "Ayarlar", fontFamily = googleSans
                )
                }
                , navigationIcon = {

                   Icon(modifier = Modifier
                       .size(40.dp)
                       .padding(top = 10.dp),imageVector = Icons.Filled.Settings,
                       contentDescription = null,
                       tint = MaterialTheme.colorScheme.secondary )

                }

                ,colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary),
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary

                ))
            val switchState = remember {
                mutableStateOf(false)
            }
            LazyColumn{
                item {
                    Row (modifier = Modifier.fillMaxWidth()){
                        Text(text = "Bildirimler")
                        Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd){
                            Switch(checked = switchState.value, onCheckedChange = {
                                switchState.value = it
                            })
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary)
                }



                item {     Row (modifier = Modifier.fillMaxWidth()){
                    Text(text = "Bildirimler")
                    Switch(checked = switchState.value, onCheckedChange = {
                        switchState.value = it
                    })
                }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary) }



                item {     Row (modifier = Modifier.fillMaxWidth()){
                    Text(text = "Bildirimler")
                    Switch(checked = switchState.value, onCheckedChange = {
                        switchState.value = it
                    })
                }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary) }


                item {     Row (modifier = Modifier.fillMaxWidth()){
                    Text(text = "Bildirimler")
                    Switch(checked = switchState.value, onCheckedChange = {
                        switchState.value = it
                    })
                }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary) }


                item {     Row (modifier = Modifier.fillMaxWidth()){
                    Text(text = "Bildirimler")
                    Switch(checked = switchState.value, onCheckedChange = {
                        switchState.value = it
                    })
                }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary) }

            }
            Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally){
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.padding(vertical = 1.dp))
                Image(modifier = Modifier.size(20.dp),bitmap = ImageBitmap.imageResource(id = R.drawable.tr_background_logo), contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary))
                Text(text = "ASLAN Software Studio" , color = MaterialTheme.colorScheme.onPrimary, fontSize = 10.sp, fontFamily = googleSans)
            }

        }
    }
}