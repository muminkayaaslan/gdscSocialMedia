package com.aslansoft.deneme.views

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.aslansoft.deneme.CameraPreview
import com.aslansoft.deneme.R
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun Camera(navController: NavHostController) {
        val uriState = remember {
            mutableStateOf<Uri?>(null)
        }
        val context = LocalContext.current
        val controller = remember{
            LifecycleCameraController(context).apply {
                setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE
                )
            }
        }
        val pickPhotoLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){uri ->
        if(uri  != null){
            uriState.value = uri
            val photoUri = Uri.encode(uri.toString())
            navController.navigate("post_screen/$photoUri")
        }

        }
        val multiplePermissionsLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val myVersion = Build.VERSION.SDK_INT
            if (myVersion >= Build.VERSION_CODES.TIRAMISU) {
                if (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true) {
                    pickPhotoLauncher.launch("image/*")
                } else {
                    Toast.makeText(
                        context,
                        "Dosya Erişimi İçin İzin Vermelisin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else { // myVersion > Build.VERSION_CODES.R
                if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                    pickPhotoLauncher.launch("image/*")
                } else {
                    Toast.makeText(
                        context,
                        "Dosya Erişimi İçin İzin Vermelisin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
        Surface(Modifier.fillMaxSize()) {

                Box(modifier = Modifier.fillMaxSize()){
                    CameraPreview(
                        controller = controller,
                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        onClick = {
                                controller.cameraSelector =
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {

                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }
                                  },
                        ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            bitmap = ImageBitmap.imageResource(R.drawable.cameraswitch),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )

                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 30.dp)
                            .size(50.dp),
                        onClick = {

                        },
                    ) {
                        Icon(modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                controller.takePicture(
                                    ContextCompat.getMainExecutor(context),
                                    object : OnImageCapturedCallback() {
                                        override fun onCaptureSuccess(image: ImageProxy) {
                                            super.onCaptureSuccess(image)
                                            val bitmap = image.toBitmap()

                                            // Bitmap'i bir dosyaya kaydetmek için bir dosya oluştur
                                            val file = File(
                                                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                                "photo_${System.currentTimeMillis()}.jpg"
                                            )

                                            // Dosyaya yazma işlemi için bir çıkış akışı oluştur
                                            val outputStream = FileOutputStream(file)

                                            // Bitmap'i JPEG formatında dosyaya yaz
                                            bitmap.compress(
                                                Bitmap.CompressFormat.JPEG,
                                                90,
                                                outputStream
                                            )

                                            // Dosyaya yazma işlemini bitir ve kaynakları serbest bırak
                                            outputStream.close()

                                            // Fotoğraf çekildikten sonra yapılacak diğer işlemler

                                            println("Başarıyla kaydedildi: ${file.absolutePath}")

                                            // ImageProxy'yi kapat
                                            image.close()

                                            // Dosyanın URI'sini al
                                            val photoUri = FileProvider.getUriForFile(
                                                context,
                                                "com.aslansoft.deneme" + ".provider",
                                                file
                                            )

                                            // Elde edilen URI'yi kullanarak fotoğrafı kaydetme veya paylaşma işlemleri yapabilirsiniz.
                                            val encodedPhotoUri = Uri.encode(photoUri.toString())
                                            navController.navigate("post_screen/$encodedPhotoUri")
                                            println("succes")

                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            super.onError(exception)
                                            println(exception.message)
                                        }
                                    }
                                )
                            }
                           ,imageVector = ImageVector.vectorResource(R.drawable.camera_capture),
                            contentDescription = null,
                            tint = MaterialTheme
                                .colorScheme
                                .secondary)

                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 30.dp)
                            .size(50.dp),
                        onClick = {

                        },
                    ) {
                        Icon(modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                val myVersion = Build.VERSION.SDK_INT

                                if (myVersion >= Build.VERSION_CODES.TIRAMISU) {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.READ_MEDIA_IMAGES
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        pickPhotoLauncher.launch("image/*")
                                    } else {
                                        multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                                    }
                                } else {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.READ_EXTERNAL_STORAGE
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        pickPhotoLauncher.launch("image/*")
                                    } else {
                                        multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                                    }
                                }

                            },
                            imageVector = ImageVector.vectorResource(R.drawable.add_a_photo),
                            contentDescription = null,
                            tint = MaterialTheme
                                .colorScheme
                                .secondary)

                    }
                }

            }
        }
