package com.aslansoft.deneme.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.ui.theme.googleSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavHostController) {

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
        Column {
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
        }
    }
}