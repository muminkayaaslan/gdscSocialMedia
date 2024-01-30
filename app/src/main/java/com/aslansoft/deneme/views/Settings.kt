package com.aslansoft.deneme.views

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.aslansoft.deneme.ui.theme.googleSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavHostController) {
    val sheetState = remember {
        mutableStateOf(false)
    }
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
            val contextInsta = LocalContext.current
            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Handle result if needed
            }
            LazyColumn{
                item {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally)){
                        Icon(modifier = Modifier
                            .align(Alignment.CenterVertically)
                           ,imageVector = Icons.Outlined.Notifications, contentDescription = null )
                        Text(modifier = Modifier
                            .align(Alignment.CenterVertically)
                            ,text = "Bildirimler", fontFamily = googleSans)
                        Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd){
                            Switch(checked = switchState.value, onCheckedChange = {
                                switchState.value = it
                            },
                                colors = SwitchDefaults.colors(
                                    checkedBorderColor = Color.Transparent,
                                    checkedIconColor = MaterialTheme.colorScheme.secondary,
                                    uncheckedBorderColor = Color.Transparent,
                                    checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                                    checkedThumbColor = Color.Red

                                ),
                                thumbContent =if (switchState.value) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }

                                })
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary)
                }



                item {
                    Row (modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        sheetState.value = true
                    }){
                    Icon(modifier = Modifier
                        .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.Call, contentDescription = null)
                    Text(modifier = Modifier
                        .align(Alignment.CenterVertically)
                        ,text = "Bize Ulaşın", fontFamily = googleSans)
                }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary)
                }


                item {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp).clickable {

                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "GDSC YOBU Topluluğunun Neler Yaptığını Sende Merak Ediyorsan Aramıza Katıl: https://play.google.com/store/apps/details?id=com.aslansoft.deneme")
                                type = "text/plain"
                            }

                            try {
                                launcher.launch(Intent.createChooser(shareIntent, "Paylaş"))
                            } catch (e: Exception) {
                                // Handle exception if sharing is not possible
                                // You can provide fallback behavior or show an error message to the user
                            }
                        }){
                    Icon(modifier = Modifier
                        .align(Alignment.CenterVertically)
                       ,imageVector = Icons.Outlined.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary )
                    Text(modifier = Modifier
                        .align(Alignment.CenterVertically)
                        ,text = "Davet Edin", fontFamily = googleSans)

                }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary)
                }

               item {
                   Row (modifier = Modifier
                       .fillMaxWidth()
                       .height(50.dp).clickable {
                           val wpUri = Uri.parse("https://docs.google.com/document/d/10DPHfIqJ5liYS_SBtnMIb43Efkyo_Uoz3zGwk2UtrQc/edit?usp=drive_link")
                           val intent = Intent(Intent.ACTION_VIEW, wpUri)

                           intent.setPackage("com.google.android.apps.docs")

                           try {
                               contextInsta.startActivity(intent)
                           } catch (e: Exception) {
                               contextInsta.startActivity(
                                   Intent(
                                       Intent.ACTION_VIEW,
                                       Uri.parse("https://docs.google.com/document/d/10DPHfIqJ5liYS_SBtnMIb43Efkyo_Uoz3zGwk2UtrQc/edit?usp=drive_link"))
                               )
                           }
                       }){
                       Icon(modifier = Modifier
                           .align(Alignment.CenterVertically)
                           ,imageVector = Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary )
                       Text(modifier = Modifier
                           .align(Alignment.CenterVertically)
                           ,text = "Kullanıcı Ve Gizlilik Sözleşmesi", fontFamily = googleSans)

                   }
                   Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary)
               }
            }

            if (sheetState.value){
                ModalBottomSheet(onDismissRequest = { sheetState.value = false }) {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 10.dp).clickable {
                            val wpUri = Uri.parse("https://whatsapp.com/channel/0029Va7kPQzHbFVBS1vbcj40")
                            val intent = Intent(Intent.ACTION_VIEW, wpUri)

                            intent.setPackage("com.whatsapp")

                            try {
                                contextInsta.startActivity(intent)
                            } catch (e: Exception) {
                                contextInsta.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://whatsapp.com/channel/0029Va7kPQzHbFVBS1vbcj40"))
                                )
                            }
                        }, verticalAlignment = Alignment.CenterVertically){
                        Icon(modifier = Modifier.size(40.dp),bitmap = ImageBitmap.imageResource(R.drawable.whatsapp), contentDescription = null )
                        Text(modifier = Modifier.padding(start = 5.dp),text = "WhatsApp", fontFamily = googleSans)
                    }
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 10.dp).clickable {
                            val instagramUri = Uri.parse("https://www.instagram.com/gdscyobu?igsh=eWU2aThlcTV1Znhs")
                            val intent = Intent(Intent.ACTION_VIEW, instagramUri)

                            intent.setPackage("com.instagram.android")

                            try {
                                contextInsta.startActivity(intent)
                            } catch (e: Exception) {
                                contextInsta.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.instagram.com/gdscyobu?igsh=eWU2aThlcTV1Znhs"))
                                )
                            }
                        }, verticalAlignment = Alignment.CenterVertically){
                        Icon(modifier = Modifier.size(30.dp),bitmap = ImageBitmap.imageResource(R.drawable.instagram), contentDescription = null )
                        Text(modifier = Modifier.padding(start = 5.dp),text = "Instagram", fontFamily = googleSans)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                }
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

