package com.aslansoft.deneme.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aslansoft.deneme.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(navController: NavHostController) {
    Surface(modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.primary) {
        val auth = Firebase.auth
        val db = Firebase.firestore
        val storage = Firebase.storage
        val currentUser = auth.currentUser
        val username = remember { mutableStateOf("") }
        val newUsername = remember { mutableStateOf("") }
        val profilePhoto = remember { mutableStateOf(false) }


        Column(modifier = Modifier.fillMaxSize()) {
            currentUser?.email?.let {
                db.collection("users").document(it).get().addOnSuccessListener {
                    val data = it.data
                    username.value = data?.get("username") as? String ?: ""
                }
            }
            CenterAlignedTopAppBar(modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp)),
                title = { Text(modifier = Modifier.
                padding(top = 12.dp),
                    text = "Profili Düzenle")
                }
                , navigationIcon = {
                    Image(modifier = Modifier
                        .size(40.dp)
                        .padding(top = 10.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                        ,imageVector = Icons.Filled.Edit, contentDescription = null)
                },colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary),
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary

                ))
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(30.dp)
                    )
                    .clip(RoundedCornerShape(30.dp))
            ) {
                if (profilePhoto.value == false)
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {

                        },
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )else{
                    Image(bitmap = ImageBitmap.imageResource(R.drawable.wolficon), contentDescription = null)
                }

            }
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(value = newUsername.value, onValueChange ={
                    newUsername.value = it
                },
                    trailingIcon = {
                                   Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    },
                    label = { Text(text = "Kullanıcı Adı")},
                    placeholder = { Text(text = username.value)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                        unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary
                    ))
            }
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(bottom = 5.dp)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                ,
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )) {
                Text(text = "KAYDET",color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}